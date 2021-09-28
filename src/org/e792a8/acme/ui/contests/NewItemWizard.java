package org.e792a8.acme.ui.contests;

import java.io.IOException;

import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.core.workspace.IGroup;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewItemWizard extends Wizard implements INewWizard {
	private DirectoryWizardPage page;
	private IGroup parent;

	public NewItemWizard() {
		super();
	}

	public NewItemWizard(IGroup parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void addPages() {
		page = new DirectoryWizardPage(parent, true);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		IDirectoryBuilder builder = page.getDirectoryBuilder();
		try {
			builder.finish();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
}
