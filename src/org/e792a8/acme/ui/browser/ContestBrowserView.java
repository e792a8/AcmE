package org.e792a8.acme.ui.browser;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ContestBrowserView extends ViewPart {
	public static final String ID = "org.e792a8.acme.ContestBrowserView";
	private Composite parent;
	private StackLayout stackLayout = new StackLayout();
	private List<ContestBrowserPart> browserParts = new LinkedList<>();
	private Composite currentPart;
	private EmptyBrowserPart emptyBrowserPart;
	private IDirectoryActionObserver directoryActionObserver = new IDirectoryActionObserver() {

		@Override
		public void open(IDirectory directory) {
			if (isValidUrl(directory.getUrl())) {
				for (ContestBrowserPart p : browserParts) {
					if (directory.equals(p.getDirectory())) {
						refreshView(p);
						return;
					}
				}
				ContestBrowserPart p = new ContestBrowserPart(parent, directory);
				browserParts.add(p);
				refreshView(p);
			}
		}

		@Override
		public void close(IDirectory directory) {
			for (ContestBrowserPart p : browserParts) {
				if (directory.equals(p.getDirectory())) {
					if (p.equals(currentPart)) {
						refreshView(emptyBrowserPart);
					}
					p.dispose();
					browserParts.remove(p);
					break;
				}
			}
		}
	};

	private boolean isValidUrl(String url) {
		if (url.length() > 0)
			return true;
		return false;
	}

	private void refreshView(Composite comp) {
		currentPart = comp;
		stackLayout.topControl = comp;
		parent.layout();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(stackLayout);
		emptyBrowserPart = new EmptyBrowserPart(parent);
		refreshView(emptyBrowserPart);
		AcmeUI.addDirectoryActionObserver(directoryActionObserver);
	}

	@Override
	public void dispose() {
		super.dispose();
		AcmeUI.deleteDirectoryActionObserver(directoryActionObserver);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
