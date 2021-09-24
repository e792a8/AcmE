package org.e792a8.acme.ui.testpoints;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class EmptyViewPart extends Composite {

	public EmptyViewPart(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		CLabel lblNoTestPoints = new CLabel(this, SWT.NONE);
		lblNoTestPoints.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.ITALIC));
		lblNoTestPoints.setAlignment(SWT.CENTER);
		lblNoTestPoints.setText("No test points to show.");
	}

}
