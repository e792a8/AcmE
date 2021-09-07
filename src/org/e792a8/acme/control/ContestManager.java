package org.e792a8.acme.control;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.e792a8.acme.ui.editors.CodeEditor;
import org.e792a8.acme.ui.testpoints.TestPointsView;
import org.e792a8.acme.workspace.DirectoryHandle;
import org.e792a8.acme.workspace.SolutionHandle;
import org.e792a8.acme.workspace.TestPointHandle;
import org.e792a8.acme.workspace.WorkspaceParser;
import org.eclipse.core.runtime.IPath;

public class ContestManager {

	private static DirectoryHandle currentDirectory;

	public static IPath getRootPath() {
		return WorkspaceParser.getRoot();
	}

	public static DirectoryHandle openDirectory(IPath path) {
		if (currentDirectory != null) {
			closeDirectory();
		}
		DirectoryHandle handle = readDirectory(path);
		if (!"problem".equals(handle.type)) {
			return currentDirectory;
		}
		currentDirectory = handle;
		TestPointsView.loadTestPoints();
		CodeEditor.openEditor();
		return currentDirectory;
	}

	public static void closeDirectory() {
		writeDirectory(currentDirectory);
		TestPointsView.unloadTestPoints();
		CodeEditor.closeEditor();
		currentDirectory = null;
	}

	public static DirectoryHandle getCurrentDirectory() {
		return currentDirectory;
	}

	public static boolean checkDirectoryHandle(DirectoryHandle handle) {
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
			Iterator<SolutionHandle> itrS = handle.solutions.iterator();
			while (itrS.hasNext()) {
				if (!path.append(itrS.next().path).toFile().isFile()) {
					return false;
				}
			}
			Iterator<TestPointHandle> itrT = handle.testPoints.iterator();
			while (itrT.hasNext()) {
				TestPointHandle test = itrT.next();
				if (!path.append(test.in).toFile().isFile() || !path.append(test.ans).toFile().isFile()) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static boolean repairDirectory(DirectoryHandle handle) {
		IPath path = handle.absPath;
		if ("group".equals(handle.type)) {
			Iterator<String> itr = handle.children.iterator();
			while (itr.hasNext()) {
				File f = path.append(itr.next()).toFile();
				if (!f.isDirectory()) {
					f.delete();
					f.mkdirs();
				}
			}
		} else if ("problem".equals(handle.type)) {
			Iterator<SolutionHandle> is = handle.solutions.iterator();
			while (is.hasNext()) {
				SolutionHandle s = is.next();
				File f = path.append(s.path).toFile();
				if (!f.isFile()) {
					f.delete();
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			Iterator<TestPointHandle> it = handle.testPoints.iterator();
			while (it.hasNext()) {
				TestPointHandle t = it.next();
				File f = path.append(t.in).toFile();
				if (!f.isFile()) {
					f.delete();
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
				f = path.append(t.ans).toFile();
				if (!f.isFile()) {
					f.delete();
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static DirectoryHandle readDirectory(IPath path) {
		DirectoryHandle handle = WorkspaceParser.readDirHandle(path);
		if (!checkDirectoryHandle(handle)) {
			if (!repairDirectory(handle)) {
				return null;
			}
		}
		return handle;
	}

	public static boolean writeDirectory(DirectoryHandle handle) {
		boolean res = WorkspaceParser.writeDirHandle(handle);
		if (!res) {
			return false;
		}
		res = checkDirectoryHandle(handle);
		if (!res) {
			res = repairDirectory(handle);
		}
		return res;
	}
}
