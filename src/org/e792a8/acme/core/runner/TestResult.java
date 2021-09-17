package org.e792a8.acme.core.runner;

import java.io.File;
import java.util.List;

public class TestResult {
	public String resultCode;
	public String message;
	public List<int[]> diffRanges;
	public long timeMs;
	public long memoryKb;
	public File outputFile;
}
