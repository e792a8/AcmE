package org.e792a8.acme.ui.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EmptyBrowserPart extends Composite {

	public EmptyBrowserPart(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		Label lbl = new Label(this, SWT.NONE);
		lbl.setText("No page to show.");
	}

}
