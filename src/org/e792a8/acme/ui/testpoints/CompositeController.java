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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class CompositeController {
	TestPointComposite composite;
	ARunner runner = null;

	CompositeController(TestPointComposite comp) {
		composite = comp;
	}

	class ClearTestPointAction extends Action implements Listener {

		@Override
		public void run() {
			// FIXME need better methods
			ITestPoint config = composite.getTestPoint();
			File inFile = config.getInput().getFile();
			File ansFile = config.getAnswer().getFile();
			FileSystem.write(inFile, "");
			FileSystem.write(ansFile, "");
			composite.setInput("");
			composite.setOutput("");
			composite.setAnswer("");
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}

	}

	class DeleteTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			ITestPoint tp = composite.getTestPoint();
			TestPointsView parentView = composite.parentView;
			parentView.composites.remove(composite);
			composite.dispose();
			dispose();
			tp.delete();
			parentView.updateIndexes();
			parentView.refresh();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class RunTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			AcmeUI.fireBeforeRun(composite.getTestPoint());
			composite.setResultText("--");
			composite.saveTestPoint();
			composite.clearOutput();
			runner = RunnerFactory.createRunner(composite.getTestPoint().getProblem().getSolution(),
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
		composite.setResultText("--");
		final TestPointRequest req = new TestPointRequest(composite.getTestPoint(), new IRunnerCallback() {

			@Override
			public void start() {
				composite.setResultText("**");
			}

			@Override
			public void finish(TestResult result) {
				if (result != null) {
					composite.setResultText(result.resultCode);
					if (result.outputFile != null) {
						composite.setOutput(FileSystem.read(result.outputFile, 4096));
					}
					AcmeUI.fireAfterRun(composite.getTestPoint());
				}
			}
		});
		return req;
	}

	protected void dispose() {
		if (runner != null) {
			runner.terminate();
		}
	}

	protected void deleteTestPoint() {
		composite.getTestPoint().delete();
	}
}
