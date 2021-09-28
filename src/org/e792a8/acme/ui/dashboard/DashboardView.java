package org.e792a8.acme.ui.dashboard;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DashboardView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.dashboard.DashboardView";

	private Composite currentPart;
	private Composite parent;
	private StackLayout stackLayout = new StackLayout();

	private IDirectoryActionObserver directoryActionObserver = new IDirectoryActionObserver() {
		@Override
		public void open(IDirectory dir) {
			currentPart.dispose();
			refreshView(new DashboardPart(parent, dir));
		}

		@Override
		public void close(IDirectory dir) {
			if (currentPart instanceof DashboardPart) {
				if (((DashboardPart) currentPart).getDirectory().equals(dir)) {
					currentPart.dispose();
					refreshView(new EmptyPart(parent));
				}
			}
		}

		@Override
		public void handleException(Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	};

	private void refreshView(Composite comp) {
		currentPart = comp;
		stackLayout.topControl = currentPart;
		parent.layout();
	}

	@Override
	public void dispose() {
		AcmeUI.deleteDirectoryActionObserver(directoryActionObserver);
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(stackLayout);

		refreshView(new EmptyPart(parent));
		AcmeUI.addDirectoryActionObserver(directoryActionObserver);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
