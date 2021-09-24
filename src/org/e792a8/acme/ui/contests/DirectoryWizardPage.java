package org.e792a8.acme.ui.contests;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IDirectoryBuilder;
import org.e792a8.acme.utils.FileSystem;
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
	private Button btnGroup;
	private Button btnProblem;
	private IDirectory directory;
	private boolean isNewWizard;

	public DirectoryWizardPage(IDirectory directory, boolean isNew) {
		super("wizardPage");
		this.directory = directory;
		isNewWizard = isNew;
	}

	private void selectGroupType() {
		groupTypeSelected = true;
		btnProblem.setSelection(false);
		btnGroup.setSelection(true);
	}

	private void selectProblemType() {
		groupTypeSelected = false;
		btnGroup.setSelection(false);
		btnProblem.setSelection(true);
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

		btnGroup = new Button(typeRadios, SWT.RADIO);
		btnGroup.setText("Group / Contest");
		btnProblem = new Button(typeRadios, SWT.RADIO);
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
		if (isNewWizard) {
			setTitle("New Directory");
			setDescription("Adding under " + directory.getFullPath().toString());
			selectGroupType();
		} else {
			setTitle("Configuring " + directory.getName());
			setDescription("Editing " + directory.getFullPath().toString());
			if (directory.isGroup()) {
				selectGroupType();
			} else if (directory.isProblem()) {
				selectProblemType();
			} else {
				return;
			}
			nameText.setText(directory.getName());
			urlText.setText(directory.getUrl());
			pathText.setEnabled(false);
		}
	}

	private void dialogChanged(ModifyEvent event) {
		if (event != null) {
			if (isNewWizard && event.getSource().equals(nameText)) {
				pathText.setText(FileSystem.safePathName(nameText.getText()));
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public IDirectoryBuilder getDirectoryBuilder() {
		IDirectoryBuilder builder = null;
		if (isNewWizard) {
			builder = directory.toGroup().createSubDirectory().setFileName(pathText.getText());
		} else {
			builder = directory.modify();
		}
		builder.setName(nameText.getText()).setUrl(urlText.getText());
		if (groupTypeSelected) {
			builder.setType("group");
		} else {
			builder.setType("problem");
			// TODO custom judge type
			// TODO custom language
		}
		return builder;
	}
}