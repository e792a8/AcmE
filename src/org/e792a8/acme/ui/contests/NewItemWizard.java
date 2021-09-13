package org.e792a8.acme.ui.contests;

import java.util.LinkedList;

import org.e792a8.acme.core.workspace.DirectoryConfig;
import org.e792a8.acme.core.workspace.SolutionConfig;
import org.e792a8.acme.core.workspace.TestPointConfig;
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
		boolean selectGroup = page.getSelectGroup();
		String name = page.getName();
		String path = page.getPath();
		String url = page.getUrl();
		doFinish(selectGroup, name, path, url);
		return true;
	}

	private void doFinish(boolean selectGroup, String name, String path, String url) {
		if (parentPath.append(path).toFile().exists()) {
			path += '_';
			int i = 2;
			while (parentPath.append(path + i).toFile().exists()) {
				++i;
			}
			path += i;
			name += " (" + i + ")";
		}
		DirectoryConfig parentHandle = WorkspaceManager.readDirectory(parentPath);
		parentHandle.children.add(path);
		WorkspaceManager.writeDirectory(parentHandle);
		DirectoryConfig handle = new DirectoryConfig();
		handle.absPath = parentPath.append(path);
		handle.name = name;
		handle.url = url;
		handle.type = (selectGroup ? "group" : "problem");
		if (selectGroup) {
			handle.children = new LinkedList<>();
		} else {
			SolutionConfig sol = new SolutionConfig();
			sol.lang = "cpp";
			sol.path = "sol.cpp";
			handle.solution = sol;
			handle.testPoints = new LinkedList<>();
			TestPointConfig test = new TestPointConfig();
			test.in = "in1.txt";
			test.ans = "ans1.txt";
			handle.testPoints.add(test);
		}
		WorkspaceManager.writeDirectory(handle);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		parentPath = (IPath) selection.getFirstElement();
		if (parentPath == null) {
			parentPath = WorkspaceManager.readRoot().absPath;
		}
	}
}
