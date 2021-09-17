package org.e792a8.acme.ui.testpoints;

import org.e792a8.acme.core.workspace.TestPointConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class TestPointComposite extends Composite {

	TestPointsView parentView;
	int index;
	CLabel lblTest;
	private DataBlock inputBlock;
	private DataBlock outputBlock;
	private DataBlock answerBlock;
	TestPointConfig testPointConfig;
	CompositeController controller;
	CLabel lblResult;

	void delete() {
		parentView.deleteTestPoint(index - 1);
	}

	void clear() {
		inputBlock.setText("");
		outputBlock.setText("");
		answerBlock.setText("");
	}

	public void setResultText(String txt) {
		lblResult.getDisplay().asyncExec(() -> {
			try {
				lblResult.setText(txt);
				int color = SWT.COLOR_DARK_GRAY;
				if ("AC".equals(txt)) {
					color = SWT.COLOR_DARK_GREEN;
				} else if ("--".equals(txt)) {
					color = SWT.COLOR_DARK_GRAY;
				} else if ("**".equals(txt)) {
					color = SWT.COLOR_BLACK;
				} else {
					color = SWT.COLOR_RED;
				}
				lblResult.setForeground(SWTResourceManager.getColor(color));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void setInput(String txt) {
		inputBlock.setText(txt);
	}

	public void setOutput(String txt) {
		outputBlock.getDisplay().asyncExec(() -> {
			outputBlock.setText(txt);
		});
	}

	public void setAnswer(String txt) {
		answerBlock.setText(txt);
	}

	public String getInput() {
		return inputBlock.getText();
	}

	public String getOutput() {
		return outputBlock.getText();
	}

	public String getAnswer() {
		return answerBlock.getText();
	}

	public void setIndex(int i) {
		index = i;
		lblTest.setText("Test " + index);
	}

	public TestPointConfig getConfig() {
		return testPointConfig;
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TestPointComposite(Composite parent, int style, TestPointsView view, TestPointConfig tpConf, int index) {
		super(parent, style);
		parentView = view;
		testPointConfig = tpConf;
		controller = new CompositeController(this);

		setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite testPointFrame = new Composite(this, SWT.BORDER);
		testPointFrame.setLayout(new FormLayout());

		Composite header = new Composite(testPointFrame, SWT.NONE);
		header.setLayout(new FormLayout());
		FormData fd_header = new FormData();
		fd_header.top = new FormAttachment(0);
		fd_header.right = new FormAttachment(100);
		fd_header.left = new FormAttachment(0);
		header.setLayoutData(fd_header);

		Composite controls = new Composite(header, SWT.NONE);
		FormData fd_controls = new FormData();
		fd_controls.right = new FormAttachment(100);
		fd_controls.top = new FormAttachment(0);
		controls.setLayoutData(fd_controls);
		RowLayout rl_controls = new RowLayout(SWT.HORIZONTAL);
		rl_controls.pack = false;
		rl_controls.center = true;
		controls.setLayout(rl_controls);

		Composite labels = new Composite(header, SWT.NONE);
		labels.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_labels = new FormData();
		fd_labels.right = new FormAttachment(controls);
		fd_labels.bottom = new FormAttachment(100);
		fd_labels.top = new FormAttachment(0);
		fd_labels.left = new FormAttachment(0);
		labels.setLayoutData(fd_labels);

		lblTest = new CLabel(labels, SWT.NONE);

		lblResult = new CLabel(labels, SWT.NONE);
		lblResult.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblResult.setText("--");

		Button btnRun = new Button(controls, SWT.NONE);
		btnRun.setText("Run");
		btnRun.addListener(SWT.MouseDown, controller.new RunTestPointAction());

		Button btnClear = new Button(controls, SWT.NONE);
		btnClear.setText("Clear");
		btnClear.addListener(SWT.MouseDown, controller.new ClearTestPointAction());

		Button btnDelete = new Button(controls, SWT.NONE);
		btnDelete.setText("Delete");
		btnDelete.addListener(SWT.MouseDown, controller.new DeleteTestPointAction());

		Composite body = new Composite(testPointFrame, SWT.NONE);
		body.setLayout(new FillLayout(SWT.HORIZONTAL));
		FormData fd_body = new FormData();
		fd_body.height = 160;
		fd_body.top = new FormAttachment(header);
		fd_body.right = new FormAttachment(100);
		fd_body.bottom = new FormAttachment(100);
		fd_body.left = new FormAttachment(0);
		body.setLayoutData(fd_body);

		inputBlock = new DataBlock(body, SWT.NONE, "Input");

		outputBlock = new DataBlock(body, SWT.NONE, "Output");

		answerBlock = new DataBlock(body, SWT.NONE, "Answer");

		setIndex(index);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
