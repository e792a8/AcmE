package org.e792a8.acme.ui.contests;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DirectoryWizardPage extends WizardPage {
	private Text nameText;
	private Text urlText;
	private IPath parentPath;
	private boolean groupTypeSelected;
	private Text pathText;

	public DirectoryWizardPage(IPath parent) {
		super("wizardPage");
		setTitle("Group / Contest / Problem");
		setDescription("Add a new problem group / contest / problem");
		this.parentPath = parent;
	}

	private void selectGroupType() {
		groupTypeSelected = true;
	}

	private void selectProblemType() {
		groupTypeSelected = false;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		container.setLayout(layout);

		Label lblType = new Label(container, SWT.NULL);
		lblType.setText("Type");

		Composite typeRadios = new Composite(container, SWT.NULL);
		typeRadios.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		typeRadios.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button btnGroup = new Button(typeRadios, SWT.RADIO);
		btnGroup.setText("Group / Contest");
		Button btnProblem = new Button(typeRadios, SWT.RADIO);
		btnProblem.setText("Problem");
		btnGroup.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectGroupType();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		btnProblem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectProblemType();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		Label lblName = new Label(container, SWT.NULL);
		lblName.setText("Name");

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(e -> dialogChanged(e));

		Label lblPath = new Label(container, SWT.NULL);
		lblPath.setText("Path");

		pathText = new Text(container, SWT.BORDER | SWT.SINGLE);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.addModifyListener(e -> dialogChanged(e));

		Label lblUrl = new Label(container, SWT.NULL);
		lblUrl.setText("URL");

		urlText = new Text(container, SWT.BORDER | SWT.SINGLE);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.addModifyListener(e -> dialogChanged(e));
		initialize();
		dialogChanged(null);
		setControl(container);
	}

	private void initialize() {
	}

	private void dialogChanged(ModifyEvent event) {
		if (event != null) {
			if (event.getSource().equals(nameText)) {
				pathText.setText(nameText.getText().replaceAll("[\\ /\\\\]+", "_"));
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public boolean getSelectGroup() {
		return groupTypeSelected;
	}

	@Override
	public String getName() {
		return nameText.getText();
	}

	public String getPath() {
		return pathText.getText();
	}

	public String getUrl() {
		return urlText.getText();
	}

}