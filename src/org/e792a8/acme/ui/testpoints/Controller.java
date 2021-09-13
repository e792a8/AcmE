package org.e792a8.acme.ui.testpoints;

import org.e792a8.acme.core.runner.RunnerFactory;
import org.e792a8.acme.core.runner.pipeline.ARunner;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class Controller {
	TestPointsView testsView;

	Controller(TestPointsView view) {
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
			// TODO run all tests
			ARunner runner = RunnerFactory.createRunner(testsView.getDirectory().solution,
				testsView.getDirectory().testPoints);
		}

		@Override
		public void handleEvent(Event event) {
			run();
		}
	}
}
