package org.e792a8.acme.core.runner.judge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.e792a8.acme.core.runner.TestResult;

public class StrictJudge extends AJudge {
	@Override
	public TestResult judge(File inputFile, File outputFile, File answerFile) {
		FileReader outReader = null, ansReader = null;
		TestResult res = null;
		try {
			outReader = new FileReader(outputFile);
			ansReader = new FileReader(answerFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			res = new TestResult();
			res.resultCode = "SYSTEM ERROR";
			res.message = e.getMessage();
		}
		while (res == null) {
			int o, a;
			try {
				o = outReader.read();
				a = ansReader.read();
			} catch (IOException e) {
				e.printStackTrace();
				res = new TestResult();
				res.resultCode = "SYSTEM ERROR";
				res.message = e.getMessage();
				break;
			}
			if (o == -1 && a == -1) {
				res = new TestResult();
				res.resultCode = "AC";
				res.message = "Passed";
				break;
			} else if (o != a) {
				res = new TestResult();
				res.resultCode = "WA";
				res.message = "Wrong Answer";
				break;
			}
		}
		try {
			outReader.close();
			ansReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			res.message = e.getMessage();
			res.resultCode = "SYSTEM ERROR";
		}
		return res;
	}
}
