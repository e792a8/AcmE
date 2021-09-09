package org.e792a8.acme.core.workspace;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * Handles workspace directories and files.
 * 
 * Responsible for ensuring configs and files are consistent and legal.
 * 
 */
public class WorkspaceManager {
	public static boolean checkConfigConsistence(DirectoryConfig handle) {
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
		return false;
	}

	public static boolean handleNotConsistent(DirectoryConfig config) {
		// TODO ask to autofix this directory
		return false;
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

	public static IPath getRootPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}

	public static DirectoryConfig getRootConfig() {
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
			handleBadConfig(path);
			handle = ConfigParser.readDirConfig(path);
		}
		if (handle == null) {
			// TODO throw BadConfigException
		}
		if (!checkConfigConsistence(handle)) {
			handleBadConfig(path);
		}
		if (!checkConfigConsistence(handle)) {
			// TODO throw NotConsistentException
		}
		return handle;
	}

	public static boolean writeDirectory(DirectoryConfig handle) {
		boolean res = ConfigParser.writeDirConfig(handle);
		if (!res) {
			// TODO throw BadConfigException
		}
		res = checkConfigConsistence(handle);
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

	private static void rmDir(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					rmDir(f);
				}
			}
			file.delete();
		}
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
		rmDir(path.toFile());
		return true;
	}
}
