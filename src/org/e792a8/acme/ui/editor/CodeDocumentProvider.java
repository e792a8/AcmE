package org.e792a8.acme.ui.editor;

import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class CodeDocumentProvider extends TextFileDocumentProvider {
	/* DocumentProviders seem tobe useless for highlighting */
	/*
	 * private static class ParentProvider extends FileDocumentProvider {
	 * 
	 * @Override protected IDocument createDocument(Object element) throws
	 * CoreException { IDocument document = super.createDocument(element); if
	 * (document != null) { IDocumentPartitioner partitioner = new
	 * FastPartitioner(new CodePartitionScanner(), CodePartitionScanner.RULES);
	 * partitioner.connect(document); document.setDocumentPartitioner(partitioner);
	 * } return document; } }
	 * 
	 * public CodeDocumentProvider() { super(new ParentProvider()); }
	 */
}
