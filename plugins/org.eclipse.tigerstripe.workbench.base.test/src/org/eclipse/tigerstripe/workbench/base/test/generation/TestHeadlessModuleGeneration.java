package org.eclipse.tigerstripe.workbench.base.test.generation;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;

public class TestHeadlessModuleGeneration extends TestCase {
	protected static final String WS = " ";
	protected static final String WIN_ECLIPSE_EXE = "eclipse.exe";
	protected static final String MAC_ECLIPSE_EXE = "eclipse";
	protected static final String LNX_ECLIPSE_EXE = MAC_ECLIPSE_EXE;
	protected static final String HEADLESS_PLUGIN = "org.eclipse.tigerstripe.workbench.headless.moduleGeneration";

	private final static String NAME = "OrderManagement";
	private final static String ID = NAME;

	public void testHeadlessModuleGeneration() throws Exception {
		ModelProjectHelper.createEmptyModelProject(NAME, ID);
		StringBuilder command = new StringBuilder();
		String loc = getEclipseLocation();
		command.append(loc).append(WS);
		command.append("-nosplash").append(WS);
		command.append("-data").append(WS);
		command.append(getEclipseWorkspace())
				.append(System.currentTimeMillis()).append(WS);
		command.append("-application").append(WS);
		command.append(HEADLESS_PLUGIN).append(WS);
		command.append("PROJECT_EXPORT=" + NAME).append(WS);
		String tmpJarPath = getTmpJarPath();
		command.append("OUTPUT=" + tmpJarPath);
		System.out.println(command.toString());
		Process exec = Runtime.getRuntime().exec(command.toString());
		int result = exec.waitFor();
		assertEquals(IApplication.EXIT_OK.intValue(), result);
		File tmp = new File(tmpJarPath);
		assertTrue(tmp.exists());
	}

	protected String getOSSpecificTmpLocation() {
		String result = System.getProperty("java.io.tmpdir");
		return result;
	}

	protected String getTmpJarPath() {
		File tmp = new File(getOSSpecificTmpLocation() + File.separator
				+ "tsExport" + System.currentTimeMillis() + ".jar");

		while (tmp.exists()) {
			tmp = new File(getOSSpecificTmpLocation() + File.separator
					+ "tsExport" + System.currentTimeMillis() + ".jar");
		}
		return tmp.getPath();
	}

	private String getEclipseWorkspace() {
		IPath path = Platform.getLocation();
		return path.toOSString();
	}

	private String getEclipseLocation() {
		Location loc = Platform.getInstallLocation();
		URL url = loc.getURL();
		String eclipseExe = null;
		if (Platform.OS_WIN32.equals(Platform.getOS())) {
			eclipseExe = WIN_ECLIPSE_EXE;
		} else if (Platform.OS_LINUX.equals(Platform.getOS())) {
			eclipseExe = LNX_ECLIPSE_EXE;
		} else if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			eclipseExe = MAC_ECLIPSE_EXE;
		} else {
			throw new IllegalArgumentException("Unknown OS: "
					+ Platform.getOS());
		}
		return new Path(url.getPath()).append(eclipseExe).toOSString();
	}
}