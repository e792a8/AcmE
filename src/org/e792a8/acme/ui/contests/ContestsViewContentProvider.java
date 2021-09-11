package org.e792a8.acme.ui.contests;

import java.util.List;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
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

	private IPath invisibleRoot;

	@Override
	public Object[] getElements(Object parent) {
		if (parent.equals(this.contestsView.getViewSite())) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		return WorkspaceManager.getParent((IPath) child);
	}

	@Override
	public Object[] getChildren(Object parent) {
		List<IPath> children = WorkspaceManager.getChildren((IPath) parent);
		return children.toArray();
	}

	@Override
	public boolean hasChildren(Object parent) {
		DirectoryConfig handle = WorkspaceManager.readDirectory((IPath) parent);
		if ("group".equals(handle.type) && handle.children != null && handle.children.size() > 0)
			return true;
		return false;
	}

	private void initialize() {
		invisibleRoot = WorkspaceManager.readRoot().absPath;
	}
}