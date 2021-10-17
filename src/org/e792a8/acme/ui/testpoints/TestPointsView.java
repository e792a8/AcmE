package org.e792a8.acme.ui.testpoints;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.PagedView;
import org.eclipse.swt.widgets.Composite;

public class TestPointsView extends PagedView {

	public static final String ID = "org.e792a8.acme.ui.testpoints.TestPointsView";

	public TestPointsView() {
		super("No problem to show.");
	}

	@Override
	protected Composite createPage(Composite parent, IDirectory directory) {
		if (directory.isProblem()) {
			return new TestPointsPage(parent, directory.toProblem());
		}
		return null;
	}
}
