package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class ContestsViewLabelProvider extends LabelProvider {

	/**
	 * the Contests View this provider is attached to
	 */
	private final ContestsView contestsView;

	/**
	 * @param contestsView
	 */
	ContestsViewLabelProvider(ContestsView contestsView) {
		this.contestsView = contestsView;
	}

	@Override
	public String getText(Object obj) {
		return WorkspaceManager.readDirectory((IPath) obj).name;
	}

	@Override
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if ("group".equals(WorkspaceManager.readDirectory((IPath) obj).type))
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}