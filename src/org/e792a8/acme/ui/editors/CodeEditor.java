package org.e792a8.acme.ui.editors;

import org.e792a8.acme.control.ContestManager;
import org.e792a8.acme.workspace.DirectoryHandle;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class CodeEditor {
	public static void openEditor() {
		DirectoryHandle handle = ContestManager.getCurrentDirectory();
		if ("problem".equals(handle.type)) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
//				IEditorPart editor = EgitUiEditorUtils.findEditor(file, page);
//				IEditorPart active = page.getActiveEditor();
//				if (editor != null && editor != active) {
//					window.getWorkbench().getDisplay()
//						.asyncExec(() -> page.bringToTop(editor));
//				}
			}
		}
	}

	public static void closeEditor() {

	}
}
