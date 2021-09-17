package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
import org.e792a8.acme.utils.FileSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

public class TestPointsView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.testpoints.TestPointsView";
	Composite testsArea;
	private ScrolledComposite scrolledComposite;
	Button btnAdd;
	Button btnRunAll;
	private final TestPointsViewController controller = new TestPointsViewController(this);
	private DirectoryConfig directory;
	private static TestPointsView instance;
	protected List<TestPointComposite> composites = new LinkedList<>();
	CLabel lblResult;

	public TestPointsView() {
		super();
		instance = this;
	}

	public static void openDirectory(DirectoryConfig config) {
		instance.controller.openDirectory(config);
		instance.directory = config;
	}

	public void setResultText(String txt) {
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

	public DirectoryConfig getDirectory() {
		return directory;
	}

	public void refresh() {
		testsArea.layout();
		updateSize();
	}

	void addTestPointToView(TestPointConfig config) {
		TestPointComposite comp = new TestPointComposite(testsArea, SWT.NONE, this, config, composites.size() + 1);
		composites.add(comp);
		comp.setInput(FileSystem.read(config.directory.absPath.append(config.in).toFile(), 4096));
		comp.setAnswer(FileSystem.read(config.directory.absPath.append(config.ans).toFile(), 4096));
		refresh();
	}

	void deleteTestPoint(int i) {
		TestPointComposite c = composites.get(i);
		c.controller.deleteTestPoint();
		c.controller.dispose();
		c.dispose();
		composites.remove(c);
		Control[] controls = testsArea.getChildren();
		int j = 0;
		for (TestPointComposite co : composites) {
			++j;
			co.setIndex(j);
		}
		refresh();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void setFocus() {
		// TODO Set the focus to control
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite viewArea = new Composite(parent, SWT.NONE);
		viewArea.setLayout(new FormLayout());

		Composite header = new Composite(viewArea, SWT.NONE);
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
		btnAdd.addListener(SWT.MouseDown, controller.new AddTestPointAction());

		btnRunAll = new Button(buttonsArea, SWT.NONE);
		btnRunAll.setText("Run All");
		btnRunAll.addListener(SWT.MouseDown, controller.new RunAllTestsAction());

		Composite body = new Composite(viewArea, SWT.NONE);
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

		ColumnLayout cl_testsArea = new ColumnLayout();
		cl_testsArea.maxNumColumns = 4;
		testsArea.setLayout(cl_testsArea);

	}

	private void updateSize() {
		ScrollBar bar = scrolledComposite.getVerticalBar();
		testsArea.setSize(testsArea.computeSize(
			scrolledComposite.getSize().x - (bar != null && bar.isVisible() ? bar.getSize().x : 0),
			SWT.DEFAULT));
	}
}
