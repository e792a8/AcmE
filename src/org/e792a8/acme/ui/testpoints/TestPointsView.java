package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
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

	public TestPointsView() {
		super();
		instance = this;
	}

	public static void openDirectory(DirectoryConfig config) {
		instance.controller.openDirectory(config);
		instance.setDirectory(config);
	}

	public void setDirectory(DirectoryConfig config) {
		directory = config;
	}

	public DirectoryConfig getDirectory() {
		return directory;
	}

	public void refresh() {
		testsArea.layout();
		updateSize();
	}

	void addTestPointToView(TestPointConfig config) {
		composites.add(new TestPointComposite(testsArea, SWT.NONE, this, config, composites.size() + 1));
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

		CLabel lblTestPoints = new CLabel(header, SWT.NONE);
		FormData fd_lblTestPoints = new FormData();
		fd_lblTestPoints.bottom = new FormAttachment(100);
		fd_lblTestPoints.top = new FormAttachment(0);
		fd_lblTestPoints.left = new FormAttachment(0);
		lblTestPoints.setLayoutData(fd_lblTestPoints);
		lblTestPoints.setText("Test Points");

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
