package org.e792a8.acme.ui.browser;

import org.e792a8.acme.core.workspace.IDirectory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ContestBrowserPage extends Composite {

	private IDirectory directory;
	private Composite headBar;
	private Browser browser;
	private Text locationBox;

	public ContestBrowserPage(Composite parent, IDirectory directory) {
		super(parent, SWT.NONE);
		this.directory = directory;
		initializeComposite();
	}

	private void initializeComposite() {
		setLayout(new FormLayout());
		headBar = new Composite(this, SWT.NONE);
		headBar.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_headBar = new FormData();
		fd_headBar.top = new FormAttachment(0);
		fd_headBar.left = new FormAttachment(0);
		fd_headBar.right = new FormAttachment(100);
		headBar.setLayoutData(fd_headBar);

		browser = new Browser(this, SWT.NONE);
		FormData fd_browser = new FormData();
		fd_browser.top = new FormAttachment(headBar);
		fd_browser.left = new FormAttachment(0);
		fd_browser.right = new FormAttachment(100);
		fd_browser.bottom = new FormAttachment(100);
		browser.setLayoutData(fd_browser);

		locationBox = new Text(headBar, SWT.BORDER);
		locationBox.setBounds(0, 0, 59, 21);

		setUrl(directory.getUrl());
	}

	private void setUrl(String url) {
		locationBox.setText(url);
		browser.setUrl(url);
	}

	public IDirectory getDirectory() {
		return directory;
	}
}
