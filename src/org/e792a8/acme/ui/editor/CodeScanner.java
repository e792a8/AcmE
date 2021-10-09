package org.e792a8.acme.ui.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.wb.swt.SWTResourceManager;

public class CodeScanner extends RuleBasedScanner {

	public CodeScanner() {
		IToken string = new Token(new TextAttribute(SWTResourceManager.getColor(0xee, 0, 0)));
		IToken preprocess = new Token(new TextAttribute(SWTResourceManager.getColor(0x66, 0xcc, 0xff)));

		IRule[] rules = {
			new SingleLineRule("\"", "\"", string, '\\'),
			new SingleLineRule("'", "'", string, '\\'),
			new SingleLineRule("#", " ", preprocess, '\\'),
		};

		setRules(rules);
	}
}
