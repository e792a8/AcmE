package org.e792a8.acme.ui.perspective;

import org.e792a8.acme.ui.browser.ContestBrowserView;
import org.e792a8.acme.ui.contests.ContestsView;
import org.e792a8.acme.ui.dashboard.DashboardView;
import org.e792a8.acme.ui.testpoints.TestPointsView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class AcmePerspectiveFactory implements IPerspectiveFactory {

	public static final String ID = "org.e792a8.acme.perspective.AcmePerspective";
	private IPageLayout factory;

	public AcmePerspectiveFactory() {
		super();
	}

	@Override
	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		final String[] viewsTopLeft = {
			ContestsView.ID,
		};
		final String[] viewsBottomLeft = {
			DashboardView.ID,
		};
		final String[] viewsBottom = {
			TestPointsView.ID,
		};
		final String[] viewsRight = {
			ContestBrowserView.ID,
		};

		IFolderLayout topLeft = factory.createFolder("topLeft", // NON-NLS-1
			IPageLayout.LEFT, 0.25f, factory.getEditorArea());
		for (String s : viewsTopLeft) {
			topLeft.addView(s);
		}
		IFolderLayout bottomLeft = factory.createFolder("bottomLeft", // NON-NLS-1
			IPageLayout.BOTTOM, 0.5f, "topLeft");
		for (String s : viewsBottomLeft) {
			bottomLeft.addView(s);
		}
		IFolderLayout bottom = factory.createFolder("bottomRight", // NON-NLS-1
			IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
		for (String s : viewsBottom) {
			bottom.addView(s);
		}
		IFolderLayout right = factory.createFolder("topRight",
			IPageLayout.RIGHT, 0.4f, "topRight");
		for (String s : viewsRight) {
			right.addView(s);
		}
	}

	private void addActionSets() {
		final String[] actionSets = {
			IPageLayout.ID_NAVIGATE_ACTION_SET,
		};
		for (String s : actionSets) {
			factory.addActionSet(s);
		}
	}

	private void addPerspectiveShortcuts() {
		final String[] perspectiveShortcuts = {
			ID,
		};
		for (String s : perspectiveShortcuts) {
			factory.addPerspectiveShortcut(s);
		}
	}

	private void addNewWizardShortcuts() {
		final String[] newWizardShortctus = {
		};
		for (String s : newWizardShortctus) {
			factory.addNewWizardShortcut(s);
		}
	}

	private void addViewShortcuts() {
		final String[] viewShortcuts = {
			ContestsView.ID,
			DashboardView.ID,
			TestPointsView.ID,
			ContestBrowserView.ID,
		};
		for (String s : viewShortcuts) {
			factory.addShowViewShortcut(s);
		}
	}

}
