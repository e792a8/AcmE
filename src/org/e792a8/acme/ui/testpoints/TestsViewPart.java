package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wb.swt.SWTResourceManager;

class TestsViewPart extends Composite {
	private IProblem problem;
	private CLabel lblResult;
	private ScrolledComposite scrolledComposite;
	private Composite testsArea;
	private Button btnAdd;
	private Button btnRunAll;
	private GridLayout testsAreaLayout;
	private List<TestPointComposite> composites = new LinkedList<>();

	private AddTestPointAction addTestPointAction = new AddTestPointAction();
	private RunAllTestsAction runAllTestsAction = new RunAllTestsAction();

	private class AddTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			TestPointComposite comp = new TestPointComposite(TestsViewPart.this,
				getProblem().addTestPoint(), composites.size() + 1);
			composites.add(comp);
			refresh();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	private class RunAllTestsAction extends Action implements Listener {
		@Override
		public void run() {
			if (getProblem() == null) {
				return;
			}
			AcmeUI.fireBeforeRun(getProblem());
			List<TestPointRequest> requests = new LinkedList<>();
			for (TestPointComposite c : composites) {
				requests.add(c.getTestPointRequest());
			}
			setResultText("--");
			saveTestPoints();
			clearOutputs();
			RunnerFactory.createRunner(getProblem().getSolution(),
				requests, new IRunnerCallback() {

					@Override
					public void start() {
						setResultText("**");
					}

					@Override
					public void finish(TestResult result) {
						setResultText(result.resultCode);
						AcmeUI.fireAfterRun(getProblem());
					}
				}).launch();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	public TestsViewPart(Composite parent, IProblem problem) {
		super(parent, SWT.NONE);
		this.problem = problem;
		initializeComposite();
	}

	private void initializeComposite() {
		setLayout(new FormLayout());
		Composite header = new Composite(this, SWT.NONE);
		header.setLayout(new FormLayout());
		FormData fd_header = new FormData();
		fd_header.left = new FormAttachment(0);
		fd_header.right = new FormAttachment(100);
		fd_header.top = new FormAttachment(0);
		header.setLayoutData(fd_header);

		Composite buttonsArea = new Composite(header, SWT.NONE);
		FormData fd_buttonsArea = new FormData();
		fd_buttonsArea.bottom = new FormAttachment(100);
		fd_buttonsArea.right = new FormAttachment(100);
		fd_buttonsArea.top = new FormAttachment(0);
		buttonsArea.setLayoutData(fd_buttonsArea);
		RowLayout rl_buttonsArea = new RowLayout(SWT.HORIZONTAL);
		rl_buttonsArea.pack = false;
		rl_buttonsArea.center = true;
		buttonsArea.setLayout(rl_buttonsArea);

		btnAdd = new Button(buttonsArea, SWT.NONE);
		btnAdd.setText("Add");
		btnAdd.addListener(SWT.MouseDown, addTestPointAction);

		btnRunAll = new Button(buttonsArea, SWT.NONE);
		btnRunAll.setText("Run All");
		btnRunAll.addListener(SWT.MouseDown, runAllTestsAction);

		Composite body = new Composite(this, SWT.NONE);
		body.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_body = new FormData();
		fd_body.top = new FormAttachment(header);

		Composite composite = new Composite(header, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.right = new FormAttachment(buttonsArea);
		composite.setLayoutData(fd_composite);

		CLabel lblTestPoints = new CLabel(composite, SWT.NONE);
		lblTestPoints.setText("Test Points");

		lblResult = new CLabel(composite, SWT.NONE);
		lblResult.setText("--");
		fd_body.bottom = new FormAttachment(100);
		fd_body.right = new FormAttachment(100);
		fd_body.left = new FormAttachment(0);
		body.setLayoutData(fd_body);

		scrolledComposite = new ScrolledComposite(body, SWT.BORDER | SWT.V_SCROLL);

		testsArea = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(testsArea);
		scrolledComposite.addListener(SWT.Resize, event -> {
			updateSize();
		});

		testsAreaLayout = new GridLayout();
		testsAreaLayout.makeColumnsEqualWidth = true;
		testsArea.setLayout(testsAreaLayout);

	}

	private void setResultText(String txt) {
		lblResult.getDisplay().asyncExec(() -> {
			try {
				lblResult.setText(txt);
				int color = SWT.COLOR_DARK_GRAY;
				if ("AC".equals(txt)) {
					color = SWT.COLOR_DARK_GREEN;
				} else if ("--".equals(txt)) {
					color = SWT.COLOR_DARK_GRAY;
				} else if ("**".equals(txt)) {
					color = SWT.COLOR_BLACK;
				} else {
					color = SWT.COLOR_RED;
				}
				lblResult.setForeground(SWTResourceManager.getColor(color));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void saveTestPoints() {
		for (TestPointComposite c : composites) {
			c.saveTestPoint();
		}
	}

	private void clearOutputs() {
		for (TestPointComposite c : composites) {
			c.clearOutput();
		}
	}

	private void refresh() {
		testsArea.layout();
		updateSize();
	}

	private void updateSize() {
		final int MAX_CELL_WIDTH = 480;
		ScrollBar bar = scrolledComposite.getVerticalBar();
		int width = scrolledComposite.getSize().x - (bar != null && bar.isVisible() ? bar.getSize().x : 0);
		testsAreaLayout.numColumns = width / MAX_CELL_WIDTH + (width % MAX_CELL_WIDTH > 0 ? 1 : 0);
		testsArea.setSize(testsArea.computeSize(width, SWT.DEFAULT));
	}

	IProblem getProblem() {
		return problem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestsViewPart) {
			return getProblem().equals(((TestsViewPart) obj).getProblem());
		}
		return false;
	}

	void removeComposite(TestPointComposite comp) {
		composites.remove(comp);
		refresh();
	}

}
