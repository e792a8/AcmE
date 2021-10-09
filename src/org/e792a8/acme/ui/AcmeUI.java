package org.e792a8.acme.ui;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.ISolution;
import org.e792a8.acme.core.workspace.IWorkspaceElement;
import org.e792a8.acme.ui.editor.CodeEditor;
import org.e792a8.acme.ui.editor.CodeEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

public class AcmeUI {
	static List<IDirectoryActionObserver> directoryActionObservers = new LinkedList<>();
	static List<IRunTestObserver> runTestObservers = new LinkedList<>();

	private static IDirectoryActionObserver[] globalOpenDirectoryObservers = {

		new IDirectoryActionObserver() {
			@Override
			public void open(IDirectory config) {
				if (config == null || !config.isProblem()) {
					return;
				}
				IProblem p = config.toProblem();
				ISolution sol = p.getSolution();
				// FIXME we should have only 1 editor for a problem
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().openEditor(new CodeEditorInput(sol), CodeEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void close(IDirectory config) {
				if (config.isProblem()) {
					// TODO close related editors when closing a problem
				}
			}

			@Override
			public void handleException(Exception e) {
				// TODO Auto-generated method stub

			}
		}
	};

	private void addGlobalOpenDirectoryObservers() {
		for (IDirectoryActionObserver o : globalOpenDirectoryObservers) {
			addDirectoryActionObserver(o);
		}
	}

	private void deleteGlobalOpenDirectoryObservers() {
		for (IDirectoryActionObserver o : globalOpenDirectoryObservers) {
			deleteDirectoryActionObserver(o);
		}
	}

	public void start(BundleContext context) throws Exception {
		addGlobalOpenDirectoryObservers();
	}

	public void stop(BundleContext context) throws Exception {
		deleteGlobalOpenDirectoryObservers();
	}

	public static void addDirectoryActionObserver(IDirectoryActionObserver observer) {
		directoryActionObservers.add(observer);
	}

	public static void deleteDirectoryActionObserver(IDirectoryActionObserver observer) {
		directoryActionObservers.remove(observer);
	}

	public static void fireOpenDirectory(IDirectory directory) {
		for (IDirectoryActionObserver o : directoryActionObservers) {
			try {
				try {
					o.open(directory);
				} catch (Exception e) {
					o.handleException(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void fireCloseDirectory(IDirectory directory) {
		for (IDirectoryActionObserver o : directoryActionObservers) {
			try {
				try {
					o.close(directory);
				} catch (Exception e) {
					o.handleException(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addRunTestObserver(IRunTestObserver observer) {
		runTestObservers.add(observer);
	}

	public static void deleteRunTestObserver(IRunTestObserver observer) {
		runTestObservers.remove(observer);
	}

	public static void fireBeforeRun(IWorkspaceElement config) {
		for (IRunTestObserver o : runTestObservers) {
			try {
				try {
					o.before(config);
				} catch (Exception e) {
					o.handleException(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void fireAfterRun(IWorkspaceElement config) {
		for (IRunTestObserver o : runTestObservers) {
			try {
				try {
					o.after(config);
				} catch (Exception e) {
					o.handleException(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
