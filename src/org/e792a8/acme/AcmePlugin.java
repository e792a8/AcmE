package org.e792a8.acme;

import org.e792a8.acme.ui.AcmeUI;
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

	private static AcmeUI acmeUI;

	/**
	 * The constructor
	 */
	public AcmePlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		acmeUI = new AcmeUI();
		acmeUI.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		acmeUI.stop(context);
		acmeUI = null;

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
