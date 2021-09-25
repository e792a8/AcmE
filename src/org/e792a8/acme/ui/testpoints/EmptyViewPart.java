package org.e792a8.acme.ui.testpoints;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EmptyViewPart extends Composite {

	public EmptyViewPart(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Label lblNoTestPoints = new Label(this, SWT.NONE);
		lblNoTestPoints.setText("No test points to show.");
	}

}
