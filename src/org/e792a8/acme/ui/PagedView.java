package org.e792a8.acme.ui;

import java.util.Map;
import java.util.TreeMap;

import org.e792a8.acme.core.workspace.IDirectory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public abstract class PagedView extends ViewPart implements IDirectoryActionObserver {
	private Map<IDirectory, Composite> pages = new TreeMap<>();
	private Composite parent;
	private StackLayout stackLayout = new StackLayout();
	private EmptyPage emptyPage;
	private Composite currentViewPart;
	protected static String emptyPageText;

	protected abstract Composite createPage(Composite parent, IDirectory directory);

	public PagedView(String emptyPageText) {
		super();
		PagedView.emptyPageText = emptyPageText;
	}

	private class EmptyPage extends Composite {
		private Label label;

		public EmptyPage(Composite parent) {
			super(parent, SWT.NONE);
			setLayout(new FillLayout());
			label = new Label(this, SWT.NONE);
			label.setText(emptyPageText);
		}

	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(stackLayout);
		emptyPage = new EmptyPage(parent);
		setTopPage(emptyPage);
		AcmeUI.addDirectoryActionObserver(this);
	}

	@Override
	public void setFocus() {
		currentViewPart.setFocus();
	}

	private void setTopPage(Composite comp) {
		currentViewPart = comp;
		stackLayout.topControl = comp;
		parent.layout();
	}

	@Override
	public void open(IDirectory directory) {
		Composite page = pages.get(directory);
		if (page == null) {
			page = createPage(parent, directory);
			if (page != null) {
				pages.put(directory, page);
			}
		}
		if (page != null) {
			setTopPage(page);
		}
	}

	@Override
	public void close(IDirectory directory) {
		Composite page = pages.get(directory);
		if (page != null) {
			if (page.equals(currentViewPart)) {
				setTopPage(emptyPage);
			}
			pages.remove(directory);
			page.dispose();
		}
	}

	@Override
	public void handleException(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		AcmeUI.deleteDirectoryActionObserver(this);
		super.dispose();
	}

}
