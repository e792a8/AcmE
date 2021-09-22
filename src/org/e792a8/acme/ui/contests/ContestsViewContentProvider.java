package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.AcmeWorkspace;
import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IRootGroup;
import org.eclipse.jface.viewers.ITreeContentProvider;

class ContestsViewContentProvider implements ITreeContentProvider {

	/**
	 * the Contests View this provider is attached to
	 */
	private final ContestsView contestsView;

	/**
	 * @param contestsView
	 */
	ContestsViewContentProvider(ContestsView contestsView) {
		this.contestsView = contestsView;
	}

	private IRootGroup invisibleRoot;

	@Override
	public Object[] getElements(Object parent) {
		if (parent.equals(contestsView.getViewSite())) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof IDirectory) {
			IDirectory d = (IDirectory) child;
			if (d.equals(d.getParentGroup())) {
				return null;
			}
			return d.getParentGroup();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof IDirectory) {
			IDirectory dir = (IDirectory) parent;
			if (dir.isGroup()) {
				return dir.toGroup().getSubDirectories().toArray();
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof IDirectory) {
			IDirectory dir = (IDirectory) parent;
			if (dir.isGroup() && dir.toGroup().getSubDirectories().size() > 0) {
				return true;
			}
		}
		return false;
	}

	private void initialize() {
		invisibleRoot = AcmeWorkspace.getRootGroup();
	}
}