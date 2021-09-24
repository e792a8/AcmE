package org.e792a8.acme.ui.testpoints;

import java.util.Set;
import java.util.TreeSet;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TestPointsView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.testpoints.TestPointsView";
	private Composite currentViewPart;
	private Set<TestsViewPart> viewParts = new TreeSet<>();

	private IDirectoryActionObserver directoryActionObserver = new IDirectoryActionObserver() {
		@Override
		public void open(IDirectory config) {
			// TODO create a view part for this problem
		}

		@Override
		public void close(IDirectory config) {
			// TODO delete the view part for this problem
		}
	};

	@Override
	public void dispose() {
		AcmeUI.deleteOpenDirectoryObserver(directoryActionObserver);
		super.dispose();
	}

	@Override
	public void setFocus() {
		// TODO Set the focus to control
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		currentViewPart = new EmptyViewPart(parent);
		AcmeUI.addOpenDirectoryObserver(directoryActionObserver);
	}
}
