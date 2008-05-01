package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

public class CompareModelAndExtract {

	private MessageList messages;
	private Map<String, IAbstractArtifact> extractedArtifacts;
	
	/* Constructor */
	public void CompareModelAndExtract() {
		
	}

	public ArrayList<Difference> compareExtractAndProject(Map<String, IAbstractArtifact> extractedArtifacts,
			ITigerstripeModelProject tsProject,
		    PrintWriter out, MessageList messages) {
		this.messages = messages;
		this.extractedArtifacts = extractedArtifacts;

		
		ArrayList<Difference> allXMLDiffs = new ArrayList<Difference>();
		try {
			IArtifactManagerSession mgrSession = tsProject.getArtifactManagerSession();
			Comparer comparer = new Comparer();
			/*
			 * For every artifact in the UML, find the project version (if it
			 * exists) and compare it
			 */

			for (IAbstractArtifact extractedArtifact : extractedArtifacts
					.values()) {
				String artifactName = extractedArtifact.getName();
				out.print("Comparing " + artifactName);
				IAbstractArtifact projectArtifact = mgrSession
				.getArtifactByFullyQualifiedName(extractedArtifact
						.getFullyQualifiedName());
				if (projectArtifact == null) {
					out.println(extractedArtifact.getFullyQualifiedName()
							+ " not found in project");
					// Make a Difference!
					Difference artifactDiff = new Difference("presence",
							extractedArtifact.getFullyQualifiedName(), "",
							"Artifact", "", "present", "absent");
					allXMLDiffs.add(artifactDiff);

				} else {
					ArrayList<Difference> artifactDiffs = comparer
					.compareArtifacts(extractedArtifact,
							projectArtifact, true, false);
					allXMLDiffs.addAll(artifactDiffs);
					out.println(" " + artifactDiffs.size() + " Diffs");

				}

			}

			IArtifactQuery myQuery = mgrSession
			.makeQuery(IQueryAllArtifacts.class.getName());
			myQuery.setIncludeDependencies(false);
			Collection<IAbstractArtifact> projectArtifacts = mgrSession
			.queryArtifact(myQuery);
			for (IAbstractArtifact pArtifact : projectArtifacts) {
				if (!extractedArtifacts.containsKey(pArtifact
						.getFullyQualifiedName())) {
					Difference artifactDiff = new Difference("presence", "",
							pArtifact.getFullyQualifiedName(), "Artifact", "",
							"absent", "present");
					allXMLDiffs.add(artifactDiff);
				}
			}

			out.println("All diffs : total " + allXMLDiffs.size());
			Message msg = new Message();
			msg.setMessage("Total Diffs found : " + allXMLDiffs.size());
			msg.setSeverity(1);
			messages.addMessage(msg);

			for (Difference diff : allXMLDiffs) {
				out.println(diff.toString());
				msg.setMessage(diff.getShortString());
				msg.setMessage("Total Diffs found : " + allXMLDiffs.size());
				msg.setSeverity(1);
				messages.addMessage(msg);
			}

			/*
			 * if (! reviewOnly){
			 * TigerstripeProjectAuditor.setTurnedOffForImport(true); DiffFixer
			 * fixer = new DiffFixer(); fixer.fixAll(allXMLDiffs, mgrSession,
			 * extractedArtifacts, out, messages);
			 * TigerstripeProjectAuditor.setTurnedOffForImport(false); }
			 */


			return allXMLDiffs;

		} catch (Exception e) {
			// Could be IOException, or someting unexpected.. Shouldn't get IO
			// as we've checked that already !
			String msgText = "Unknown Problem handling differences  '"
				+ e.getMessage() + "'";
			Message msg = new Message();
			msg.setMessage(msgText);
			msg.setSeverity(1);
			messages.addMessage(msg);
			out.println("Error : " + msgText);
			e.printStackTrace(out);
			return allXMLDiffs;

		}

	}

}



