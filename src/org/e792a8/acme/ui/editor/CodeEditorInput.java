package org.e792a8.acme.ui.editor;

import org.e792a8.acme.core.workspace.SolutionConfig;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class CodeEditorInput implements IEditorInput, IPersistableElement {

	SolutionConfig solutionConfig;
	private WorkbenchAdapter workbenchAdapter = new WorkbenchAdapter();

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

	public CodeEditorInput(SolutionConfig config) {
		solutionConfig = config;
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
		return solutionConfig.directory.absPath.append(solutionConfig.path).toFile().isFile();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(getName());
	}

	@Override
	public String getName() {
		return solutionConfig.directory.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO make it persistable
//		return this;
		return null;
	}

	@Override
	public String getToolTipText() {
		return solutionConfig.path;
	}

	@Override
	public void saveState(IMemento memento) {
		// TODO make it persistable
//		FileStoreEditorInputFactory.saveState(memento, this);
	}

	@Override
	public String getFactoryId() {
		// TODO make it persistable
//		return FileStoreEditorInputFactory.ID;
		return null;
	}

}
