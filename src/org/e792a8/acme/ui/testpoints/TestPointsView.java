package org.e792a8.acme.ui.testpoints;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.e792a8.acme.control.ContestManager;
import org.e792a8.acme.utils.TextFile;
import org.e792a8.acme.workspace.DirectoryHandle;
import org.e792a8.acme.workspace.TestPointHandle;
import org.eclipse.core.runtime.IPath;
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
	private static TestPointsView INSTANCE;
	private Composite testsArea;
	private ScrolledComposite scrolledComposite;

	public TestPointsView() {
		INSTANCE = this;
	}

	private void readTestPointFiles() {
		DirectoryHandle handle = ContestManager.getCurrentDirectory();
		IPath path = handle.absPath;
		if ("problem".equals(handle.type)) {
			List<TestPointHandle> list = handle.testPoints;
			TestPointComposite[] comps = (TestPointComposite[]) testsArea.getChildren();
			int len = comps.length;
			while (len < list.size()) {
				++len;
				addTestPoint();
			}
			while (len > list.size()) {
				--len;
				removeTestPoint(0);
			}
			comps = (TestPointComposite[]) testsArea.getChildren();
			Iterator<TestPointHandle> it = list.iterator();
			len = 0;
			while (it.hasNext()) {
				TestPointHandle h = it.next();
				File f = path.append(h.in).toFile();
				comps[len].setInput(TextFile.read(f, 4096));
				f = path.append(h.ans).toFile();
				comps[len].setAnswer(TextFile.read(f, 4096));
				++len;
			}
		}
	}

	private void writeTestPointFiles() {
		DirectoryHandle handle = ContestManager.getCurrentDirectory();
		IPath path = handle.absPath;
		if ("problem".equals(handle.type)) {
			List<TestPointHandle> testHandles = handle.testPoints;
			TestPointComposite[] comps = (TestPointComposite[]) testsArea.getChildren();
			int len = 0;
			Iterator<TestPointHandle> it = testHandles.iterator();
			while (it.hasNext()) {
				TestPointHandle h = it.next();
				TextFile.write(path.append(h.in).toFile(), comps[len].getInput());
				TextFile.write(path.append(h.ans).toFile(), comps[len].getAnswer());
				++len;
			}
		}
	}

	public static void loadTestPoints() {
		INSTANCE.readTestPointFiles();
	}

	public static void unloadTestPoints() {
		INSTANCE.writeTestPointFiles();
	}

	public void refresh() {
		testsArea.layout();
		updateSize();
	}

	public void addTestPoint() {
		new TestPointComposite(testsArea, SWT.NONE, this, testsArea.getChildren().length + 1);
		refresh();
	}

	public void removeTestPoint(int i) {
		testsArea.getChildren()[i - 1].dispose();
		Control[] controls = testsArea.getChildren();
		for (int j = 0; j < controls.length; ++j) {
			((TestPointComposite) (controls[j])).setIndex(j + 1);
		}
		refresh();
	}

	public void runAllTests() {
	}

	private void updateSize() {
		ScrollBar bar = scrolledComposite.getVerticalBar();
		testsArea.setSize(testsArea.computeSize(
			scrolledComposite.getSize().x - (bar != null && bar.isVisible() ? bar.getSize().x : 0),
			SWT.DEFAULT));
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

		Button btnAdd = new Button(buttonsArea, SWT.NONE);
		btnAdd.setText("Add");
		btnAdd.addListener(SWT.MouseDown, event -> {
			addTestPoint();
		});

		Button btnRunAll = new Button(buttonsArea, SWT.NONE);
		btnRunAll.setText("Run All");
		btnRunAll.addListener(SWT.MouseDown, event -> {
			runAllTests();
		});

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
}
