package org.e792a8.acme.wizards;

import org.e792a8.acme.workspace.WorkspaceParser;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewWizard extends Wizard implements INewWizard {
	private NewWizardPage page;
	private IPath parentPath;

	public NewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public NewWizard(boolean isGroup) {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new NewWizardPage(parentPath);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		boolean selectGroup = page.getSelectGroup();
		String name = page.getName();
		String path = page.getPath();
		String url = page.getUrl();
		doFinish(selectGroup, name, path, url);
		return true;
	}

	private void doFinish(boolean selectGroup, String name, String path, String url) {
		IPath newPath = parentPath.append(path);
		if (parentPath.append(path).toFile().exists()) {
			path += '_';
			int i = 2;
			while (parentPath.append(path + i).toFile().exists()) {
				++i;
			}
			name += " (" + i + ")";
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		parentPath = (IPath) selection.getFirstElement();
		if (parentPath == null) {
			parentPath = WorkspaceParser.getRoot();
		}
	}
}
