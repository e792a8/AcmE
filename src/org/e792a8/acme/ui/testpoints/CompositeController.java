package org.e792a8.acme.ui.testpoints;

import java.io.File;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.core.runner.pipeline.ARunner;
import org.e792a8.acme.core.workspace.TestPointConfig;
import org.e792a8.acme.core.workspace.WorkspaceManager;
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
			TestPointConfig config = composite.getConfig();
			File inFile = config.directory.absPath.append(config.in).toFile();
			File ansFile = config.directory.absPath.append(config.ans).toFile();
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
			WorkspaceManager.deleteItem(composite.getConfig());
			composite.dispose();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class RunTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			// TODO better ways to get solution config
			composite.setResultText("--");
			runner = RunnerFactory.createRunner(composite.getConfig().directory.solution,
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
		final TestPointRequest req = new TestPointRequest(composite.getConfig(), new IRunnerCallback() {

			@Override
			public void start() {
				composite.setResultText("**");
			}

			@Override
			public void finish(TestResult result) {
				// TODO Auto-generated method stub
				composite.setResultText(result.resultCode);
				composite.setOutput(FileSystem.read(result.outputFile, 4096));
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
		TestPointConfig c = composite.getConfig();
		WorkspaceManager.deleteItem(c);
		c.directory.testPoints.remove(c);
		WorkspaceManager.writeDirectory(c.directory);
	}
}
