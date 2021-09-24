package org.e792a8.acme.ui.testpoints;

import java.io.File;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.runner.pipeline.ARunner;
import org.e792a8.acme.core.workspace.ITestPoint;
import org.e792a8.acme.ui.AcmeUI;
import org.e792a8.acme.utils.FileSystem;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

public class TestPointComposite extends Composite {

	int index;
	CLabel lblTest;
	private DataBlock inputBlock;
	private DataBlock outputBlock;
	private DataBlock answerBlock;
	ITestPoint testPoint;
	CLabel lblResult;
	ARunner runner = null;
	private TestsViewPart viewPart;

	private RunTestPointAction runTestPointAction = new RunTestPointAction();
	private ClearTestPointAction clearTestPointAction = new ClearTestPointAction();
	private DeleteTestPointAction deleteTestPointAction = new DeleteTestPointAction();

	private class ClearTestPointAction extends Action implements Listener {

		@Override
		public void run() {
			ITestPoint config = getTestPoint();
			File inFile = config.getInput().getFile();
			File ansFile = config.getAnswer().getFile();
			FileSystem.write(inFile, "");
			FileSystem.write(ansFile, "");
			setInput("");
			setOutput("");
			setAnswer("");
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

	private class DeleteTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			if (runner != null) {
				runner.terminate();
			}
			ITestPoint tp = getTestPoint();
			dispose();
			tp.delete();
			viewPart.removeComposite(TestPointComposite.this);
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	private class RunTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			AcmeUI.fireBeforeRun(getTestPoint());
			setResultText("--");
			saveTestPoint();
			clearOutput();
			runner = RunnerFactory.createRunner(getTestPoint().getProblem().getSolution(),
				getTestPointRequest(), new IRunnerCallback() {

					@Override
					public void start() {
					}

					@Override
					public void finish(TestResult result) {
					}
				});
			runner.launch();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

	public TestPointRequest getTestPointRequest() {
		setResultText("--");
		final TestPointRequest req = new TestPointRequest(getTestPoint(), new IRunnerCallback() {

			@Override
			public void start() {
				setResultText("**");
			}

			@Override
			public void finish(TestResult result) {
				if (result != null) {
					setResultText(result.resultCode);
					if (result.outputFile != null) {
						setOutput(FileSystem.read(result.outputFile, 4096));
					}
					AcmeUI.fireAfterRun(getTestPoint());
				}
			}
		});
		return req;
	}

	void clear() {
		inputBlock.setText("");
		outputBlock.setText("");
		answerBlock.setText("");
		saveTestPoint();
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

	String getInput() {
		return inputBlock.getText();
	}

	String getOutput() {
		return outputBlock.getText();
	}

	String getAnswer() {
		return answerBlock.getText();
	}

	public void setIndex(int i) {
		index = i;
		lblTest.setText("Test " + index);
	}

	public ITestPoint getTestPoint() {
		return testPoint;
	}

	public void clearOutput() {
		setOutput("");
	}

	public void saveTestPoint() {
		File f = testPoint.getInput().getFile();
		FileSystem.write(f, getInput());
		f = testPoint.getAnswer().getFile();
		FileSystem.write(f, getAnswer());
	}

	/**
	 * Create the
	 * 
	 * @param parent
	 * @param style
	 */
	public TestPointComposite(TestsViewPart parent, ITestPoint tpConf, int index) {
		super(parent, SWT.NONE);
		testPoint = tpConf;
		this.viewPart = parent;

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
		btnRun.addListener(SWT.MouseDown, runTestPointAction);

		Button btnClear = new Button(controls, SWT.NONE);
		btnClear.setText("Clear");
		btnClear.addListener(SWT.MouseDown, clearTestPointAction);

		Button btnDelete = new Button(controls, SWT.NONE);
		btnDelete.setText("Delete");
		btnDelete.addListener(SWT.MouseDown, deleteTestPointAction);

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

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		setLayoutData(gridData);

		setIndex(index);

		setInput(FileSystem.read(testPoint.getInput().getFile(), 4096));
		setAnswer(FileSystem.read(testPoint.getAnswer().getFile(), 4096));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void dispose() {
		saveTestPoint();
		super.dispose();
	}

}
