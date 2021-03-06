package org.e792a8.acme.ui.testpoints;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ITestPoint;
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
			try {
				appendTestPoint(getProblem().addTestPoint());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refreshView();
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

					@Override
					public void handleException(Exception e) {
						// TODO Auto-generated method stub
						e.printStackTrace();
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

		loadTestPoints();
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

	Composite getTestsArea() {
		return testsArea;
	}

	private void appendTestPoint(ITestPoint tp) {
		TestPointComposite comp = new TestPointComposite(TestsViewPart.this,
			tp, composites.size() + 1);
		composites.add(comp);
	}

	private void loadTestPoints() {
		for (TestPointComposite c : composites) {
			c.dispose();
		}
		composites.clear();
		List<ITestPoint> tp = getProblem().getTestPoints();
		for (ITestPoint t : tp) {
			appendTestPoint(t);
		}
		refreshView();
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

	private void refreshView() {
		testsArea.layout();
		updateSize();
	}

	private void updateSize() {
		final int MIN_CELL_WIDTH = 360;
		ScrollBar bar = scrolledComposite.getVerticalBar();
		int width = scrolledComposite.getSize().x - (bar != null && bar.isVisible() ? bar.getSize().x : 0);
		testsAreaLayout.numColumns = Math.max(1, width / MIN_CELL_WIDTH);
		testsArea.setSize(testsArea.computeSize(width, SWT.DEFAULT));
	}

	IProblem getProblem() {
		return problem;
	}

	void removeComposite(TestPointComposite comp) {
		composites.remove(comp);
		refreshView();
	}

}
