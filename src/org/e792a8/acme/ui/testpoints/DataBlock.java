package org.e792a8.acme.ui.testpoints;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class DataBlock extends Composite {
	private StyledText text;

	public String getText() {
		return text.getText();
	}

	public void setText(String txt) {
		text.setText(txt);
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DataBlock(Composite parent, int style, String title) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite dataBlock = new Composite(this, SWT.NONE);
		dataBlock.setLayout(new FormLayout());

		Label lblData = new Label(dataBlock, SWT.NONE);
		lblData.setText(title);
		FormData fd_lblData = new FormData();
		fd_lblData.right = new FormAttachment(100);
		fd_lblData.left = new FormAttachment(0);
		lblData.setLayoutData(fd_lblData);

		text = new StyledText(dataBlock, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		text.setAlwaysShowScrollBars(false);
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(100);
		fd_text.top = new FormAttachment(lblData);
		fd_text.right = new FormAttachment(100);
		fd_text.left = new FormAttachment(0);
		text.setLayoutData(fd_text);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
