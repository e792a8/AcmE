package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.ui.IDirectoryActionObserver;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TestPointsView extends ViewPart {

	public static final String ID = "org.e792a8.acme.ui.testpoints.TestPointsView";
	private Composite currentViewPart;
	private List<TestsViewPart> viewParts = new LinkedList<>();
	private StackLayout stackLayout = new StackLayout();
	private Composite parent;
	private EmptyViewPart emptyViewPart;

	private IDirectoryActionObserver directoryActionObserver = new IDirectoryActionObserver() {
		@Override
		public void open(IDirectory config) {
			if (config.isProblem()) {
				IProblem problem = config.toProblem();
				for (TestsViewPart p : viewParts) {
					if (problem.equals(p.getProblem())) {
						setTopControl(p);
						return;
					}
				}
				TestsViewPart p = new TestsViewPart(parent, problem);
				viewParts.add(p);
				setTopControl(p);
			}
		}

		@Override
		public void close(IDirectory config) {
			if (config.isProblem()) {
				IProblem problem = config.toProblem();
				for (TestsViewPart p : viewParts) {
					if (problem.equals(p.getProblem())) {
						if (currentViewPart instanceof TestsViewPart) {
							TestsViewPart part = (TestsViewPart) currentViewPart;
							if (problem.equals(part.getProblem())) {
								setTopControl(emptyViewPart);
							}
						}
						p.dispose();
						viewParts.remove(p);
						break;
					}
				}
			}
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

	private void setTopControl(Composite comp) {
		currentViewPart = comp;
		stackLayout.topControl = comp;
		parent.layout();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(stackLayout);
		emptyViewPart = new EmptyViewPart(parent);
		setTopControl(emptyViewPart);
		AcmeUI.addOpenDirectoryObserver(directoryActionObserver);
	}
}
