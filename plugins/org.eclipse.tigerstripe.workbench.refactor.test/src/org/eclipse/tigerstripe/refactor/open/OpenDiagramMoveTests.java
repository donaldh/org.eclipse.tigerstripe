/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.refactor.open;

import org.eclipse.tigerstripe.refactor.artifact.Ent1_to_Ent10;
import org.eclipse.tigerstripe.refactor.closed.MoveTestsClosed;
import org.eclipse.tigerstripe.refactor.suite.DiagramHelper;

public class OpenDiagramMoveTests extends MoveTestsClosed {

	public void testRenames() throws Exception {
		
		DiagramHelper.openDiagrams(ui);
		
		// This is a single move

		Ent1_to_Ent10.openRelatedEditors(ui);
		Ent1_to_Ent10.doChangeByMove(ui);
		// Note this one needs a dot at the end of the package
		// because by default there is no package
		Ent1_to_Ent10.checkExplorerUpdates(ui,"simple.moved.","Ent1");
		Ent1_to_Ent10.checkAPI("simple.moved","Ent1");
		Ent1_to_Ent10.checkEditorUpdated(ui,"simple.moved","Ent1");
		Ent1_to_Ent10.saveAndCloseRelatedEditors(ui);
		
		
		Ent1_to_Ent10.checkDiagramsAfterMove(ui);
		// Move makes diagrams dirty, and they must be saved before next move
		DiagramHelper.saveDiagrams(ui);
		
		multipleMove();
		
		doChecks();
		DiagramHelper.saveDiagrams(ui);
		doDiagramChecks();
		
		DiagramHelper.closeDiagrams(ui);
		
	}
	
}
