package org.e792a8.acme.ui.dashboard;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.PagedView;
import org.eclipse.swt.widgets.Composite;

public class DashboardView extends PagedView {

	public static final String ID = "org.e792a8.acme.ui.dashboard.DashboardView";

	public DashboardView() {
		super("Nothing to show.");
	}

	@Override
	protected Composite createPage(Composite parent, IDirectory directory) {
		return new DashboardPage(parent, directory);
	}

}