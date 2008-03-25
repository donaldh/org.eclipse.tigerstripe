/* ====================================================================
 *   Copyright 2007 Tigerstripe, Inc.
 *
 *   Licensed under the TigerStripe(tm) License (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.tigerstripesoftware.com/licenses/TIGERSTRIPE_LICENSE.txt
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * 
 * ====================================================================
 */
/**
 * 
 */
package org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project;

import org.eclipse.tigerstripe.workbench.eclipse.wizards.project.NewProjectWizard;

public class TestNewProjectWizard extends NewProjectWizard {

	public void setPageOne(TestNewProjectWizardPage page) {
		pageOne = page;
	}
}