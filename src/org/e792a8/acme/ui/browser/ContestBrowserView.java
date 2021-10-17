package org.e792a8.acme.ui.browser;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.PagedView;
import org.eclipse.swt.widgets.Composite;

public class ContestBrowserView extends PagedView {
	public ContestBrowserView() {
		super("No web page to show.");
	}

	public static final String ID = "org.e792a8.acme.ContestBrowserView";

	private boolean isValidUrl(String url) {
		if (url.length() > 0)
			return true;
		return false;
	}

	@Override
	protected Composite createPage(Composite parent, IDirectory directory) {
		if (isValidUrl(directory.getUrl())) {
			return new ContestBrowserPage(parent, directory);
		}
		return null;
	}

}
