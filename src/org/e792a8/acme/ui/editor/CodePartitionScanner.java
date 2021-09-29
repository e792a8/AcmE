package org.e792a8.acme.ui.editor;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class CodePartitionScanner extends RuleBasedPartitionScanner {
	public static final String TYPENAME = "__typename";
	public static final String VARNAME = "__varname";
	public static final String[] RULES = {
		TYPENAME, VARNAME
	};

	public CodePartitionScanner() {
		IToken typenameToken = new Token(TYPENAME);
		IToken varnameToken = new Token(VARNAME);
		IPredicateRule[] rules = new IPredicateRule[2];
		rules[0] = new MultiLineRule("aa", "zz", typenameToken);
		rules[1] = new MultiLineRule("bc", "de", varnameToken);

		setPredicateRules(rules);
	}

}
