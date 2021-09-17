package org.e792a8.acme.ui.editor;

import org.eclipse.ui.editors.text.FileDocumentProvider;

public class CodeDocumentProvider extends FileDocumentProvider {
//	@Override
//	protected IDocument createDocument(Object element) throws CoreException {
//		CodeEditorInput input = (CodeEditorInput) element;
//		IDocument document = createEmptyDocument();
//		File f = input.solutionConfig.directory.absPath.append(input.solutionConfig.path).toFile();
//		InputStream stream;
//		try {
//			stream = new FileInputStream(f);
//			setDocumentContent(document, stream, "UTF-8");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return document;
//	}
}
