package org.e792a8.acme.perspectives;

import org.e792a8.acme.views.DashboardView;
import org.e792a8.acme.views.TestPointsView;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class AcmePerspectiveFactory implements IPerspectiveFactory {

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
			IPageLayout.ID_PROJECT_EXPLORER,
		};
		final String[] viewsBottomLeft = {
			DashboardView.ID,
		};
		final String[] viewsBottom = {
			TestPointsView.ID,
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
	}

	private void addActionSets() {
		final String[] actionSets = {
			JavaUI.ID_ACTION_SET,
			JavaUI.ID_ELEMENT_CREATION_ACTION_SET,
			IPageLayout.ID_NAVIGATE_ACTION_SET,
		};
		for (String s : actionSets) {
			factory.addActionSet(s);
		}
	}

	private void addPerspectiveShortcuts() {
		final String[] perspectiveShortcuts = {
			"org.e792a8.acme.perspectives.AcmePerspective",
			"org.eclipse.ui.resourcePerspective",
		};
		for (String s : perspectiveShortcuts) {
			factory.addPerspectiveShortcut(s);
		}
	}

	private void addNewWizardShortcuts() {
		final String[] newWizardShortctus = {
			"org.eclipse.ui.wizards.new.folder",
			"org.eclipse.ui.wizards.new.file",
		};
		for (String s : newWizardShortctus) {
			factory.addNewWizardShortcut(s);
		}
	}

	private void addViewShortcuts() {
		final String[] viewShortcuts = {
			DashboardView.ID,
			TestPointsView.ID,
		};
		for (String s : viewShortcuts) {
			factory.addShowViewShortcut(s);
		}
	}

}
