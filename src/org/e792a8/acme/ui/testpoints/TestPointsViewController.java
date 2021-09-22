package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
import org.e792a8.acme.ui.AcmeUI;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class TestPointsViewController {
	TestPointsView testsView;

	TestPointsViewController(TestPointsView view) {
		testsView = view;
	}

	class AddTestPointAction extends Action implements Listener {
		@Override
		public void run() {
			testsView.addTestPointToView(testsView.getProblem().addTestPoint());
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class RunAllTestsAction extends Action implements Listener {
		@Override
		public void run() {
			if (testsView.getProblem() == null) {
				return;
			}
			AcmeUI.fireBeforeRun(testsView.getProblem());
			List<TestPointRequest> requests = new LinkedList<>();
			for (TestPointComposite c : testsView.composites) {
				requests.add(c.controller.getTestPointRequest());
			}
			testsView.setResultText("--");
			testsView.saveTestPoints();
			testsView.clearOutputs();
			RunnerFactory.createRunner(testsView.getProblem().getSolution(),
				requests, new IRunnerCallback() {

					@Override
					public void start() {
						testsView.setResultText("**");
					}

					@Override
					public void finish(TestResult result) {
						testsView.setResultText(result.resultCode);
						AcmeUI.fireAfterRun(testsView.getProblem());
					}
				}).launch();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

}
