#
#  If you need to re-generate the .ecore and the EMF classes due to a downloadSiteScheme.xsd
#  change, please follow these instructions.
#
#  erdillon
#

1. Make your changes to downloadSiteSchema.xsd and save.
2. Re-generate the .ecore and .genmodel properly
	2.1. delete existing .ecore and .genmodel
	2.2. Right-click on downloadSiteSchema.xsd->New->Other->EMF->EMF Model
	2.3. Hit next 2x, and select XML Schema, Next and Load, Next
	2.4. Change the proposed name for the .ecore file from "schema.ecore" to "downloadSiteSchema.ecore". 
		(this is for consistency, not required or used anywhere)
	2.5. Click Finish to generate the files.
3. Delete the org.eclipse.tigerstripe.releng.downloadsite.schema package and its subpackages.
4. Edit .genmodel file
	4.1 Open the Properties view on the downloadSiteSchema.genmodel file.
	4.2. on the "Schema" package node, set the following properties:
	Base Package = "org.eclipse.tigerstripe.releng.downloadsite"
	Prefix = "DownloadSite" (this will rename the package in the treeview to DownloadSite)
	Save
5. Generate EMF Model classes
	Select the "DownloadSite" package in the downloadSiteSchema.genmodel file
	Right Click->Generate Model Code
	All errors in the Java code should be gone.
6. Update .classpath and .project file
	(you may need to change the filtering properties on the explorer to let .* resource appear)
	Remove any mention of PDE in both files.
	Save
7. Re-generate the .jar file
	delete lib/releng-ant.jar
	Right-click on releng-ant.jardesc and select Create Jar.
	

You're done. Don't forget to commit your changes and make sure the new releng-ant.jar is uploaded.