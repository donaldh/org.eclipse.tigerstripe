package org.eclipse.tigerstripe.refactor.project;

import java.util.Collection;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ProjectHelper extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void loadProjectFromCVS(IUIContext ui) throws Exception {
		
		ui.contextClick(new SWTWidgetLocator(Tree.class, new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")), "&Import...");
		ui.wait(new ShellShowingCondition("Import"));
		ui.click(new FilteredTreeItemLocator("CVS/Projects from CVS"));
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("dev.eclipse.org");		
		ui.keyClick(WT.TAB);
		ui.enterText("/cvsroot/technology");
		ui.keyClick(WT.TAB);
		ui.enterText("anonymous");
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("org.eclipse.tigerstripe/samples/test-models/model-refactoring");
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Checkout from CVS"));
		
		Thread.sleep(25000); // Let the workspace build
		
		// Disconnect from CVS so we don't have to deal with all of the decorations!
		
		ui
		.contextClick(
				new TreeItemLocator(
						"model-refactoring   [dev.eclipse.org]",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Team/Disconnect...");
		ui.wait(new ShellShowingCondition("Confirm Disconnect from CVS"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Confirm Disconnect from CVS"));
		ui.wait(new ShellDisposedCondition("Progress Information"));
	

		// Just make sure we got what we expected!
			IAbstractTigerstripeProject aProject = TigerstripeCore.findProject("model-refactoring");
			ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
			IArtifactManagerSession mgrSession = modelProject
				.getArtifactManagerSession();
			
			TestInitialPackageContents.testArtifacts(ui);
			
			//How Many Artifacts should we have?
			IArtifactQuery query = mgrSession.makeQuery(IQueryAllArtifacts.class.getName());
			Collection<IAbstractArtifact> allArtifacts = mgrSession.queryArtifact(query);
			assertEquals("Incorrect number of artifacts", 21, allArtifacts.size());
		
		
	}

	public void reloadProjectFromCVS(IUIContext ui, String projectName) throws Exception {
		ui
		.contextClick(
				new SWTWidgetLocator(
						Tree.class,
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"&Import...");
		ui.wait(new ShellShowingCondition("Import"));
		ui.click(new FilteredTreeItemLocator("CVS/Projects from CVS"));
		ui.click(new ButtonLocator("&Next >"));
		ui.click(new TableItemLocator(
		":pserver:anonymous@dev.eclipse.org:/cvsroot/technology"));
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("org.eclipse.tigerstripe/samples/test-models/" + projectName);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Checkout from CVS"));
		
		Thread.sleep(5000); // Let the workspace build
		
		// Disconnect from CVS so we don't have to deal with all of the decorations!
		
		ui
		.contextClick(
				new TreeItemLocator(
						projectName+"   [dev.eclipse.org]",
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Team/Disconnect...");
		ui.wait(new ShellShowingCondition("Confirm Disconnect from CVS"));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Confirm Disconnect from CVS"));
		ui.wait(new ShellDisposedCondition("Progress Information"));

		//TODO - This is a massive cop out!
		ui
		.click(new TreeItemLocator(
				projectName,
				new ViewLocator(
				"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")));
		ui
		.contextClick(
				new TreeItemLocator(
						projectName,
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Clean Audit Now");
		ui.wait(new ShellDisposedCondition("Clean Audit Now"));



		// Just make sure we got what we expected!
			IAbstractTigerstripeProject aProject = TigerstripeCore.findProject(projectName);
			ITigerstripeModelProject modelProject = (ITigerstripeModelProject) aProject;
			IArtifactManagerSession mgrSession = modelProject
				.getArtifactManagerSession();
            
			if(projectName.equals("model-refactoring"))
			TestInitialPackageContents.testArtifacts(ui);
			
			//How Many Artifacts should we have?
			IArtifactQuery query = mgrSession.makeQuery(IQueryAllArtifacts.class.getName());
			Collection<IAbstractArtifact> allArtifacts = mgrSession.queryArtifact(query);
			for (IAbstractArtifact art: allArtifacts){
				System.out.println("On reload : "+art.getFullyQualifiedName());
			}
			if(projectName.equals("model-refactoring"))
			    assertEquals("Incorrect number of artifacts", 21, allArtifacts.size());
			else if(projectName.equals("model-refactoring-reference"))
				assertEquals("Incorrect number of artifacts", 26, allArtifacts.size());
				
			
	}


	public void deleteProject(IUIContext ui,String projectName)throws Exception {
		ui
		.contextClick(
				new TreeItemLocator(
						projectName,
						new ViewLocator(
						"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew")),
		"Delete");
		ui.wait(new ShellDisposedCondition("Progress Information"));
		ui.wait(new ShellShowingCondition("Delete Resources"));
		ui.click(new ButtonLocator(
		"&Delete project contents on disk (cannot be undone)"));
		ui.click(new ButtonLocator("OK"));
		ui.wait(new ShellDisposedCondition("Delete Resources"));
	}


	public void deleteDiagrams(IUIContext ui) throws Exception {
		ViewLocator view = new ViewLocator(
		"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew");
		String project="model-refactoring";
		ui.click(new TreeItemLocator(
				project+"/src/simple/moved/inside-moved",
				view));
		ui.click(
				1,
				new TreeItemLocator(
						project+"/src/simple/default",
						view),
						WT.CTRL);
		ui.click(
				1,
				new TreeItemLocator(
						project+"/outside-class-diagram",
						view),
						WT.CTRL);
		ui.click(
				1,
				new TreeItemLocator(
						project+"/outside-instance-diagram",
						view),
						WT.CTRL);
		ui.keyClick(WT.DEL);
		ui.wait(new ShellShowingCondition("Delete..."));
		ui.click(new ButtonLocator("&Yes"));
		ui.wait(new ShellDisposedCondition("Delete..."));
	}

}