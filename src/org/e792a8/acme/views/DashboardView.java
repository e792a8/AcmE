package org.e792a8.acme.views;

import org.e792a8.acme.control.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.ViewPart;

public class DashboardView extends ViewPart {

	public static final String ID = "org.e792a8.acme.views.DashboardView";
	private CLabel lblContestName;
	private CLabel lblProblemTitle;

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

		lblContestName = new CLabel(labels, SWT.NONE);
		lblContestName.setText("Contest Name");

		lblProblemTitle = new CLabel(labels, SWT.NONE);
		lblProblemTitle.setText("Problem Title");

		Composite actions = new Composite(panel, SWT.NONE);
		actions.setLayout(new FillLayout(SWT.VERTICAL));
		FormData fd_actions = new FormData();
		fd_actions.right = new FormAttachment(100);
		fd_actions.top = new FormAttachment(0);
		fd_actions.bottom = new FormAttachment(100);
		actions.setLayoutData(fd_actions);

		Composite actions_r1 = new Composite(actions, SWT.NONE);
		RowLayout rl_actions_r1 = new RowLayout(SWT.HORIZONTAL);
		rl_actions_r1.justify = true;
		rl_actions_r1.fill = true;
		rl_actions_r1.pack = false;
		actions_r1.setLayout(rl_actions_r1);

		Button btnNewContest = new Button(actions_r1, SWT.NONE);
		btnNewContest.setText("New Contest");

		Button btnNewProblem = new Button(actions_r1, SWT.NONE);
		btnNewProblem.setText("New Problem");

		Composite actions_r2 = new Composite(actions, SWT.NONE);
		RowLayout rl_actions_r2 = new RowLayout(SWT.HORIZONTAL);
		rl_actions_r2.pack = false;
		rl_actions_r2.justify = true;
		rl_actions_r2.fill = true;
		actions_r2.setLayout(rl_actions_r2);

		Button btnRunTests = new Button(actions_r2, SWT.NONE);
		btnRunTests.setText("Run Tests");

		Button btnClear = new Button(actions_r2, SWT.NONE);
		btnClear.setText("Clear");

		btnClear.addListener(SWT.MouseDown, event -> {
			MessageBox.clear();
		});

		MessageConsole console = new MessageConsole("Messages", null);
		TextConsoleViewer consoleViewer = new TextConsoleViewer(messages, console);

		MessageBox.setBox(console);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
