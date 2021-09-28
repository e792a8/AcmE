package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.IDirectory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class ContestsViewLabelProvider extends LabelProvider {

	@Override
	public String getText(Object obj) {
		if (obj instanceof IDirectory) {
			IDirectory d = (IDirectory) obj;
			return d.getName();
		}
		return null;
	}

	@Override
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_FILE;
		if (obj instanceof IDirectory) {
			IDirectory dir = (IDirectory) obj;
			if (dir.isGroup()) {
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			} else if (dir.isProblem()) {
				imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			}
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}