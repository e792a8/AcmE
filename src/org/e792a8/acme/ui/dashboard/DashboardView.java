package org.e792a8.acme.ui.dashboard;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IOpenDirectoryObserver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DashboardView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.dashboard.DashboardView";
	private CLabel lbl1;
	private CLabel lbl2;
	private CLabel lbl3;
	Composite labels;
	CLabel lblContestName;
	CLabel lblProblemTitle;
	CLabel lblUrl;
	private DirectoryConfig directoryConfig;
	private IOpenDirectoryObserver openDirectoryObserver = (config) -> {
		directoryConfig = config;
		if (config == null) {
			lblContestName.setText("");
			lblProblemTitle.setText("");
			lblUrl.setText("");
			return;
		}
		DirectoryConfig pa = WorkspaceManager.readDirectory(WorkspaceManager.getParent(config.absPath));
		if (pa == null) {
			lblContestName.setText("");
		} else {
			lblContestName.setText(pa.name);
		}
		lblProblemTitle.setText(config.name);
		lblUrl.setText(config.url);

		lblContestName.requestLayout();
		lblProblemTitle.requestLayout();
		lblUrl.requestLayout();
	};

	public DashboardView() {
		AcmeUI.addOpenDirectoryObserver(openDirectoryObserver);
	}

	@Override
	public void dispose() {
		AcmeUI.deleteOpenDirectoryObserver(openDirectoryObserver);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite viewArea = new Composite(parent, SWT.NONE);
		viewArea.setLayout(new FormLayout());

		Composite panel = new Composite(viewArea, SWT.NONE);
		panel.setLayout(new FormLayout());
		FormData fd_panel = new FormData();
		fd_panel.left = new FormAttachment(0);
		fd_panel.right = new FormAttachment(100);
		fd_panel.top = new FormAttachment(0);
		panel.setLayoutData(fd_panel);

		labels = new Composite(panel, SWT.NONE);
		labels.setLayout(new GridLayout(2, false));
		FormData fd_labels = new FormData();
		fd_labels.left = new FormAttachment(0);
		fd_labels.top = new FormAttachment(0);
		fd_labels.bottom = new FormAttachment(100);
		labels.setLayoutData(fd_labels);

		lbl1 = new CLabel(labels, SWT.NONE);
		lbl1.setText("Contest Name");

		lblContestName = new CLabel(labels, SWT.NONE);
		lblContestName.setText("");

		lbl2 = new CLabel(labels, SWT.NONE);
		lbl2.setText("Problem Title");

		lblProblemTitle = new CLabel(labels, SWT.NONE);
		lblProblemTitle.setText("");

		lbl3 = new CLabel(labels, SWT.NONE);
		lbl3.setText("URL");

		lblUrl = new CLabel(labels, SWT.NONE);
		lblUrl.setText("");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
