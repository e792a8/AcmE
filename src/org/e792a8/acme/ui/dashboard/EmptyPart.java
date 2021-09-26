package org.e792a8.acme.ui.dashboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EmptyPart extends Composite {

	public EmptyPart(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		new Label(parent, SWT.NONE).setText("No problem or contest to display.");
	}

}
