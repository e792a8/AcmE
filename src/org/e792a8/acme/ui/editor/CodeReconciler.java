package org.e792a8.acme.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;

public class CodeReconciler extends PresentationReconciler {
	public CodeReconciler() {
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new CodeScanner());
		setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}
}
