package org.e792a8.acme.ui.editor;

import java.io.File;

import org.e792a8.acme.core.workspace.SolutionConfig;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class CodeEditorInput implements IEditorInput, IPersistableElement {

	private static class WorkbenchAdapter implements IWorkbenchAdapter {

		@Override
		public Object[] getChildren(Object o) {
			return null;
		}

		@Override
		public ImageDescriptor getImageDescriptor(Object object) {
			return null;
		}

		@Override
		public String getLabel(Object o) {
			return ((CodeEditorInput) o).getName();
		}

		@Override
		public Object getParent(Object o) {
			return null;
		}

	}

	SolutionConfig solutionConfig;
	private WorkbenchAdapter workbenchAdapter = new WorkbenchAdapter();

	public CodeEditorInput(SolutionConfig config) {
		this.solutionConfig = config;
	}

	@Override
	public void saveState(IMemento memento) {
		CodeEditorInputFactory.saveState(memento, solutionConfig);
	}

	@Override
	public String getFactoryId() {
		return CodeEditorInputFactory.ID;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return adapter.cast(workbenchAdapter);
		}
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public boolean exists() {
		return solutionConfig.directory.absPath
			.append(solutionConfig.path).toFile().exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry()
			.getImageDescriptor("icons/codeforces-favicon-16x16.png");
	}

	@Override
	public String getName() {
		return solutionConfig.directory.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return solutionConfig.path;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof CodeEditorInput) {
			return solutionConfig.directory.absPath.append(solutionConfig.path)
				.equals(((CodeEditorInput) obj).solutionConfig.directory.absPath
					.append(((CodeEditorInput) obj).solutionConfig.path));
		}
		return false;
	}

	public File getFile() {
		return solutionConfig.directory.absPath.append(solutionConfig.path).toFile();
	}

}
