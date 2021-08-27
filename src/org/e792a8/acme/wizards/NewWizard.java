package org.e792a8.acme.wizards;

import java.util.LinkedList;

import org.e792a8.acme.control.ContestManager;
import org.e792a8.acme.workspace.DirectoryHandle;
import org.e792a8.acme.workspace.SolutionHandle;
import org.e792a8.acme.workspace.TestPointHandle;
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

	public NewWizard(IPath parent) {
		super();
		setNeedsProgressMonitor(true);
	}

	public NewWizard(IStructuredSelection selection) {
		super();
		setNeedsProgressMonitor(true);
		parentPath = (IPath) selection.getFirstElement();
		if (parentPath == null) {
			parentPath = WorkspaceParser.getRoot();
		}
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
		if (parentPath.append(path).toFile().exists()) {
			path += '_';
			int i = 2;
			while (parentPath.append(path + i).toFile().exists()) {
				++i;
			}
			path += i;
			name += " (" + i + ")";
		}
		DirectoryHandle parentHandle = ContestManager.readDirectory(parentPath);
		parentHandle.children.add(path);
		ContestManager.writeDirectory(parentHandle);
		DirectoryHandle handle = new DirectoryHandle();
		handle.absPath = parentPath.append(path);
		handle.name = name;
		handle.url = url;
		handle.type = (selectGroup ? "group" : "problem");
		if (selectGroup) {
			handle.children = new LinkedList<>();
		} else {
			handle.solutions = new LinkedList<>();
			SolutionHandle sol = new SolutionHandle();
			sol.lang = "cpp";
			sol.path = "sol.cpp";
			handle.solutions.add(sol);
			handle.testPoints = new LinkedList<>();
			TestPointHandle test = new TestPointHandle();
			test.in = "in1.txt";
			test.ans = "ans1.txt";
			handle.testPoints.add(test);
		}
		ContestManager.writeDirectory(handle);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		parentPath = (IPath) selection.getFirstElement();
		if (parentPath == null) {
			parentPath = WorkspaceParser.getRoot();
		}
	}
}
