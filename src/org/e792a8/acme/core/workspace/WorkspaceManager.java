package org.e792a8.acme.core.workspace;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.e792a8.acme.utils.FileSystem;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * Handles workspace directories and files.
 * 
 * Responsible for ensuring configs and files are consistent and legal.
 * 
 */
public class WorkspaceManager {
	public static boolean checkDirectory(DirectoryConfig handle) {
		if (handle == null) {
			return false;
		}
		IPath path = handle.absPath;
		if ("group".equals(handle.type)) {
			Iterator<String> itr = handle.children.iterator();
			while (itr.hasNext()) {
				if (!path.append(itr.next()).toFile().isDirectory()) {
					return false;
				}
			}
			return true;
		} else if ("problem".equals(handle.type)) {
			Iterator<SolutionConfig> itrS = handle.solutions.iterator();
			while (itrS.hasNext()) {
				if (!path.append(itrS.next().path).toFile().isFile()) {
					return false;
				}
			}
			Iterator<TestPointConfig> itrT = handle.testPoints.iterator();
			while (itrT.hasNext()) {
				TestPointConfig test = itrT.next();
				if (!path.append(test.in).toFile().isFile() || !path.append(test.ans).toFile().isFile()) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static boolean handleBadConfig(IPath path) {
		// TODO ask to reset this directory
		writeDirectory(createDefaultConfig(path, "group"));
		return false;
	}

	public static boolean handleNotConsistent(DirectoryConfig config) {
		// TODO ask to autofix this directory
		return autofixConsistence(config);
	}

	private static boolean autofixConsistence(DirectoryConfig config) {
		String type = config.type;
		IPath dir = config.absPath;
		if ("group".equals(type)) {
			List<String> configChildren = config.children;
			Iterator<String> it = configChildren.iterator();
			while (it.hasNext()) {
				File f = dir.append(it.next()).toFile();
				if (!f.isDirectory()) {
					// FIXME fix incorrect directory
					FileSystem.rmDir(f);
					f.mkdirs();
				}
			}
		} else if ("problem".equals(type)) {
			JudgeConfig jConf = config.judge;
			if ("strict".equals(jConf.type)) {
			}
			// TODO other types of judge
			List<SolutionConfig> sConfs = config.solutions;
			Iterator<SolutionConfig> it1 = sConfs.iterator();
			while (it1.hasNext()) {
				File f = dir.append(it1.next().path).toFile();
				if (!f.isFile()) {
					FileSystem.rmDir(f);
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			List<TestPointConfig> tpConfs = config.testPoints;
			Iterator<TestPointConfig> it2 = tpConfs.iterator();
			while (it2.hasNext()) {
				TestPointConfig c = it2.next();
				File f = dir.append(c.in).toFile();
				if (!f.isFile()) {
					FileSystem.rmDir(f);
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				f = dir.append(c.ans).toFile();
				if (!f.isFile()) {
					FileSystem.rmDir(f);
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	private static DirectoryConfig makeRootConfig(DirectoryConfig config) {
		config.absPath = getRootPath();
		config.type = "group";
		config.name = "ROOT";
		config.url = "";
		if (config.children == null) {
			config.children = new LinkedList<>();
		}
		return config;
	}

	private static DirectoryConfig createDefaultConfig(IPath absPath, String type) {
		if (!getRootPath().isPrefixOf(absPath.removeLastSegments(1))) {
			return null;
		}
		if (getRootPath().equals(absPath)) {
			return readRoot();
		}
		DirectoryConfig config = new DirectoryConfig();
		config.absPath = absPath;
		config.url = "";
		if ("group".equals(type)) {
			config.type = "group";
			config.name = "Default group";
			config.children = new LinkedList<>();
		} else if ("problem".equals(type)) {
			config.type = "problem";
			config.name = "Default problem";
			config.judge = new JudgeConfig();
			config.judge.dirPath = absPath;
			config.judge.type = "strict";
			config.solutions = new LinkedList<>();
			SolutionConfig sc = new SolutionConfig();
			sc.dirPath = absPath;
			sc.lang = "cpp";
			sc.path = "sol.cpp";
			config.solutions.add(sc);
			config.testPoints = new LinkedList<>();
			TestPointConfig tc = new TestPointConfig();
			tc.dirPath = absPath;
			tc.in = "in1.txt";
			tc.ans = "ans1.txt";
			config.testPoints.add(tc);
		} else {
			return null;
		}
		return config;
	}

	private static IPath getRootPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}

	public static DirectoryConfig readRoot() {
		IPath root = getRootPath();
		DirectoryConfig config = ConfigParser.readDirConfig(root);
		if (config == null) {
			config = new DirectoryConfig();
			makeRootConfig(config);
			writeDirectory(config);
		}
		return config;
	}

	public static DirectoryConfig readDirectory(IPath path) {
		DirectoryConfig handle = ConfigParser.readDirConfig(path);
		if (handle == null) {
			// FIXME already a total mess qaq
			handleBadConfig(path);
			handle = ConfigParser.readDirConfig(path);
		}
		if (handle == null) {
			// TODO throw BadConfigException
		}
		if (!checkDirectory(handle)) {
			handleBadConfig(path);
		}
		if (!checkDirectory(handle)) {
			// TODO throw NotConsistentException
		}
		return handle;
	}

	public static boolean writeDirectory(DirectoryConfig handle) {
		boolean res = ConfigParser.writeDirConfig(handle);
		if (!res) {
			// TODO throw BadConfigException
		}
		res = checkDirectory(handle);
		if (!res) {
			res = handleNotConsistent(handle);
		}
		if (!res) {
			// TODO throw NotConsistentException
		}
		return res;
	}

	public static List<IPath> getChildren(IPath path) {
		DirectoryConfig parent = readDirectory(path);
		if (!"group".equals(parent.type)) {
			// TODO throw
		}
		LinkedList<IPath> children = new LinkedList<>();
		Iterator<String> it = parent.children.iterator();
		while (it.hasNext()) {
			children.add(parent.absPath.append(it.next()));
		}
		return children;
	}

	public static IPath getParent(IPath path) {
		IPath ppath = path.removeLastSegments(1);
		if (!getRootPath().isPrefixOf(ppath)) {
			return null;
		}
		return ppath;
	}

	public static boolean addDirectory(DirectoryConfig config) {
		IPath ppath = config.absPath.removeLastSegments(1);
		if (!getRootPath().isPrefixOf(ppath)) {
			return false;
		}
		DirectoryConfig pconf = readDirectory(ppath);
		String subdir = config.absPath.makeRelativeTo(ppath).toString();
		pconf.children.add(subdir);
		writeDirectory(pconf);
		writeDirectory(config);
		return true;
	}

	public static boolean deleteDirectory(IPath path) {
		IPath ppath = path.removeLastSegments(1);
		if (!getRootPath().isPrefixOf(ppath)) {
			return false;
		}
		DirectoryConfig pconf = readDirectory(ppath);
		String subdir = path.makeRelativeTo(ppath).toString();
		pconf.children.remove(subdir);
		writeDirectory(pconf);
		FileSystem.rmDir(path.toFile());
		return true;
	}
}
