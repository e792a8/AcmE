package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewItemWizard extends Wizard implements INewWizard {
	private DirectoryWizardPage page;
	private IPath parentPath;

	public NewItemWizard() {
		super();
	}

	public NewItemWizard(IPath parent) {
		super();
		this.parentPath = parent;
	}

	@Override
	public void addPages() {
		page = new DirectoryWizardPage(parentPath);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		DirectoryConfig config = page.getDirectoryConfig();
		if (config.absPath.toFile().exists()) {
			// TODO duplicate name resolution
			return false;
		}
		doFinish(config);
		return true;
	}

	private void doFinish(DirectoryConfig config) {
		WorkspaceManager.addDirectory(config);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		parentPath = (IPath) selection.getFirstElement();
		if (parentPath == null) {
			parentPath = WorkspaceManager.readRoot().absPath;
		}
	}
}
