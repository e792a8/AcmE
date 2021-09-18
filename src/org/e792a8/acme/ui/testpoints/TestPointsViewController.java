package org.e792a8.acme.ui.testpoints;

import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.core.runner.IRunnerCallback;
import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.TestPointRequest;
import org.e792a8.acme.core.runner.TestResult;
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
			// TODO add test point
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

	class RunAllTestsAction extends Action implements Listener {
		@Override
		public void run() {
			List<TestPointRequest> requests = new LinkedList<>();
			for (TestPointComposite c : testsView.composites) {
				requests.add(c.controller.getTestPointRequest());
			}
			testsView.setResultText("--");
			RunnerFactory.createRunner(testsView.getDirectory().solution,
				requests, new IRunnerCallback() {

					@Override
					public void start() {
						testsView.setResultText("**");
					}

					@Override
					public void finish(TestResult result) {
						// TODO Auto-generated method stub
						testsView.setResultText(result.resultCode);
					}
				}).launch();
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}

}
