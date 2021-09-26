package org.e792a8.acme.ui.dashboard;

import org.e792a8.acme.core.web.ContestParserFactory;
import org.e792a8.acme.core.web.ProblemParserFactory;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class DashboardPart extends Composite {
	private Composite parent;
	private CLabel lbl1;
	private CLabel lbl2;
	private CLabel lbl3;
	private Composite labels;
	private CLabel lblContestName;
	private CLabel lblProblemTitle;
	private CLabel lblUrl;
	private IDirectory directory;
	private Composite buttons;
	private ParseUrlAction parseUrlAction = new ParseUrlAction();

	private class ParseUrlAction extends Action implements Listener {

		@Override
		public void run() {
			if (directory.isGroup()) {
				ContestParserFactory.createContestParser(directory.getUrl()).parseTo(directory.toGroup());
			} else if (directory.isProblem()) {
				ProblemParserFactory.createProblemParser(directory.getUrl()).parseTo(directory.toProblem());
			}
			AcmeUI.fireOpenDirectory(directory);
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

	public DashboardPart(Composite parent, IDirectory directory) {
		super(parent, SWT.NONE);
		this.parent = parent;
		this.directory = directory;

		initializeComposite();
	}

	private void initializeComposite() {
		setLayout(new FormLayout());

		Composite panel = new Composite(this, SWT.NONE);
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
		labels.setLayoutData(fd_labels);
		if (directory.isGroup()) {
			lbl1 = new CLabel(labels, SWT.NONE);
			lbl1.setText("Contest Name");

			lblContestName = new CLabel(labels, SWT.NONE);
			lblContestName.setText(directory.getName());
		}
		if (directory.isProblem()) {
			lbl2 = new CLabel(labels, SWT.NONE);
			lbl2.setText("Problem Name");

			lblProblemTitle = new CLabel(labels, SWT.NONE);
			lblProblemTitle.setText(directory.getName());
		}
		lbl3 = new CLabel(labels, SWT.NONE);
		lbl3.setText("URL");

		lblUrl = new CLabel(labels, SWT.NONE);
		lblUrl.setText(directory.getUrl());

		buttons = new Composite(panel, SWT.NONE);
		buttons.setLayout(new RowLayout(SWT.HORIZONTAL));
		FormData fd_buttons = new FormData();
		fd_buttons.right = new FormAttachment(100);
		fd_buttons.left = new FormAttachment(0);
		fd_buttons.top = new FormAttachment(labels);
		buttons.setLayoutData(fd_buttons);

		Button btnParse = new Button(buttons, SWT.NONE);
		btnParse.setText("Parse URL");
		btnParse.addListener(SWT.MouseDown, parseUrlAction);

	}

	public IDirectory getDirectory() {
		return directory;
	}
}
