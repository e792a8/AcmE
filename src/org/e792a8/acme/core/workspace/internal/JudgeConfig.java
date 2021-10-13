package org.e792a8.acme.core.workspace.internal;

import org.e792a8.acme.core.workspace.IDirectory;
import org.e792a8.acme.core.workspace.IJudgeConfig;
import org.e792a8.acme.core.workspace.IProblem;
import org.e792a8.acme.core.workspace.IWorkspaceElement;

public class JudgeConfig implements IJudgeConfig {
	private IProblem problem;
	private String type;

	public JudgeConfig(IProblem problem) {
		this.problem = problem;
		type = "strict";
	}

	@Override
	public IProblem getProblem() {
		return problem;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IDirectory getDirectory() {
		return getProblem();
	}

	@Override
	public String getJudgeType() {
		return type;
	}

	@Override
	public int compareTo(IWorkspaceElement arg0) {
		int res = getJudgeType().compareTo(((IJudgeConfig) arg0).getJudgeType());
		// TODO
//		if (res == 0) {
//			return getJudgeArgs().compareTo(((IJudgeConfig) arg0).getJudgeArgs());
//		}
		return res;
	}

}
