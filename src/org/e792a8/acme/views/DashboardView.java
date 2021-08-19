package org.e792a8.acme.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

public class DashboardView extends ViewPart {

	public static final String ID = "org.e792a8.acme.views.DashboardView";

	public DashboardView() {
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

		Composite messages = new Composite(viewArea, SWT.NONE);
		messages.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_messages = new FormData();
		fd_messages.left = new FormAttachment(0);
		fd_messages.right = new FormAttachment(100);
		fd_messages.top = new FormAttachment(panel);
		fd_messages.bottom = new FormAttachment(100);
		messages.setLayoutData(fd_messages);

		Composite labels = new Composite(panel, SWT.NONE);
		labels.setLayout(new FillLayout(SWT.VERTICAL));
		FormData fd_labels = new FormData();
		fd_labels.left = new FormAttachment(0);
		fd_labels.top = new FormAttachment(0);
		fd_labels.bottom = new FormAttachment(100);
		labels.setLayoutData(fd_labels);

		CLabel lblContestName = new CLabel(labels, SWT.NONE);
		lblContestName.setText("Contest Name");

		CLabel lblProblemTitle = new CLabel(labels, SWT.NONE);
		lblProblemTitle.setText("Problem Title");

		Composite actions = new Composite(panel, SWT.NONE);
		FormData fd_actions = new FormData();
		fd_actions.right = new FormAttachment(100);
		fd_actions.top = new FormAttachment(0);
		fd_actions.bottom = new FormAttachment(100);
		actions.setLayoutData(fd_actions);

		StyledText messageBox = new StyledText(messages, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		messageBox.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		messageBox.setAlwaysShowScrollBars(true);
		messageBox.setWordWrap(true);
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
