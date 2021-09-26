package org.e792a8.acme;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.e792a8.acme.ui.editor.CodeEditor;
import org.e792a8.acme.ui.editor.CodeEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AcmePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.e792a8.acme"; //$NON-NLS-1$

	// The shared instance
	private static AcmePlugin plugin;

	/**
	 * The constructor
	 */
	public AcmePlugin() {
	}

	private static IDirectoryActionObserver[] globalOpenDirectoryObservers = {
		new IDirectoryActionObserver() {
			@Override
			public void open(IDirectory config) {
				if (config == null || !(config instanceof IProblem)) {
					return;
				}
				IProblem p = (IProblem) config;
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().openEditor(new CodeEditorInput(p.getSolution()), CodeEditor.ID);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void close(IDirectory config) {
				// TODO some state persisting workarounds
			}
		}
	};

	private void addGlobalOpenDirectoryObservers() {
		for (IDirectoryActionObserver o : globalOpenDirectoryObservers) {
			AcmeUI.addDirectoryActionObserver(o);
		}
	}

	private void deleteGlobalOpenDirectoryObservers() {
		for (IDirectoryActionObserver o : globalOpenDirectoryObservers) {
			AcmeUI.deleteDirectoryActionObserver(o);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		addGlobalOpenDirectoryObservers();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		deleteGlobalOpenDirectoryObservers();

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AcmePlugin getDefault() {
		return plugin;
	}

}
