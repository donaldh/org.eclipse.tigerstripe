package org.eclipse.tigerstripe.workbench.base.test.references;

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.model.ArtifactTestHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModule;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ModelReference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryArtifactsByType;

/**
 * This test case require "com.cisco.testModule" module with version "2.0.0" and
 * "InstalledEntity" entity to be installed
 */
@SuppressWarnings("deprecation")
public class TestInstalledModuleReferences extends TestCase {

	private final static String ID = "com.cisco.sample.project";
	private ITigerstripeModelProject project = null;

	private static final String CISCO_PACKAGE_NAME = "cisco";
	private static final String COM_PACKAGE_NAME = "com";
	private final static String INSTALLED_MODULE_ID = "com.cisco.testModule";
	private final static String INSTALLED_MODULE_LATEST_VERSION = "2.0.0";
	private final static String INSTALLED_ENTITY_NAME = "InstalledEntity";

	private final static String LOCAL_PROJECT_ID = INSTALLED_MODULE_ID;
	private final static String LOCAL_PROJECT_NAME = LOCAL_PROJECT_ID;
	private final static String LOCAL_ENTITY_NAME = "LocalEntity";
	private final static String NAME = "project";

	public void setUp() throws Exception {
		project = ModelProjectHelper.createEmptyModelProject(NAME, ID);
	}

	public void tearDown() throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResource proj = workspace.getRoot().findMember(NAME);
		if (proj != null)
			proj.delete(true, new NullProgressMonitor());
	}

	// Ensure that "contributed" modules are not used by Projects when
	// not referenced.
	public void test01() throws Exception {
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		IArtifactManagerSession session = wc.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) session
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(IManagedEntityArtifact.class.getName());
		Collection<IAbstractArtifact> artifacts = session.queryArtifact(query);
		assertFalse(contains(INSTALLED_ENTITY_NAME, artifacts));
		assertFalse(contains(LOCAL_ENTITY_NAME, artifacts));
	}

	// Ensure that "contributed" modules are used by Projects when
	// referenced.
	public void test02() throws Exception {
		// Add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(new ModelReference(wc, INSTALLED_MODULE_ID));

		// Check artifacts
		IArtifactManagerSession session = wc.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) session
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(IManagedEntityArtifact.class.getName());
		Collection<IAbstractArtifact> artifacts = session.queryArtifact(query);
		assertTrue(contains(INSTALLED_ENTITY_NAME, artifacts));
		assertFalse(contains(LOCAL_ENTITY_NAME, artifacts));
	}

	// Ensure that newest versions of "contributed" modules are used by
	// Projects when referenced.
	public void test03() {
		InstalledModule module = InstalledModuleManager.getInstance()
				.getModule(INSTALLED_MODULE_ID);
		assertEquals(INSTALLED_MODULE_ID, module.getModuleID());
		assertEquals(INSTALLED_MODULE_LATEST_VERSION, module.getModule()
				.getProjectDetails().getVersion());
	}

	// Ensure that local versions (ie Projects) override "contributed"
	// modules are used by Projects when referenced.
	public void test04() throws Exception {
		// Create project with the same ID in workspace
		ITigerstripeModelProject projectInWorkspace = ModelProjectHelper
				.createEmptyModelProject(LOCAL_PROJECT_NAME, LOCAL_PROJECT_ID);
		assertTrue(projectInWorkspace instanceof ITigerstripeModelProject);
		projectInWorkspace = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		ArtifactTestHelper helper = new ArtifactTestHelper(projectInWorkspace);
		// Create local entry
		helper.createArtifact(IManagedEntityArtifact.class.getName(),
				LOCAL_ENTITY_NAME, COM_PACKAGE_NAME + "." + CISCO_PACKAGE_NAME);

		// Add reference
		ITigerstripeModelProject wc = (ITigerstripeModelProject) project
				.makeWorkingCopy(new NullProgressMonitor());
		wc.addModelReference(new ModelReference(wc, INSTALLED_MODULE_ID));

		// Check artifacts
		IArtifactManagerSession session = wc.getArtifactManagerSession();
		IQueryArtifactsByType query = (IQueryArtifactsByType) session
				.makeQuery(IQueryArtifactsByType.class.getName());
		query.setArtifactType(IManagedEntityArtifact.class.getName());
		Collection<IAbstractArtifact> artifacts = session.queryArtifact(query);
		assertTrue(contains(LOCAL_ENTITY_NAME, artifacts));
		assertFalse(contains(INSTALLED_ENTITY_NAME, artifacts));

		// Delete created project
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResource proj = workspace.getRoot().findMember(INSTALLED_MODULE_ID);
		if (proj != null)
			proj.delete(true, new NullProgressMonitor());
	}

	/**
	 * Returns <tt>true</tt> if the specified artifact collection contains the
	 * artifact with specified name and <tt>false</tt> otherwise
	 * 
	 * @param artifactName
	 *            artifact name
	 * @param artifacts
	 *            artifact collection
	 * @return <tt>true</tt> if the specified collection contains artifact with
	 *         the specified name
	 */
	private boolean contains(String artifactName,
			Collection<IAbstractArtifact> artifacts) {
		for (IAbstractArtifact a : artifacts) {
			if (artifactName.equals(a.getName())) {
				return true;
			}
		}
		return false;
	}
}
