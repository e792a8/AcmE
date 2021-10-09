package org.e792a8.acme.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class CodeConfiguration extends SourceViewerConfiguration {
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		return new CodeReconciler();
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		String[] arr = new String[CodePartitionScanner.RULES.length + 1];
		arr[0] = IDocument.DEFAULT_CONTENT_TYPE;
		for (int i = 0; i < CodePartitionScanner.RULES.length; ++i) {
			arr[i + 1] = CodePartitionScanner.RULES[i];
		}
		return arr;
	}
}
