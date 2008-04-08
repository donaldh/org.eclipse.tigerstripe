<!-- This bit of XML is included by all Ant scripts needing to use the Releng Ant extensions. -->
<!-- It expects a $lib property to be defined and to point at the directory where all required .jar files -->
<!-- can be found -->
<path id="downloadsite.ant.path">
	<fileset dir="${libs}">
		<include name="**/*.jar" />
	</fileset>
</path>

<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.NewBuild" name="dsite.newBuild" classpathref="downloadsite.ant.path">
</taskdef>
<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.MergeBuild" name="dsite.mergeBuild" classpathref="downloadsite.ant.path">
</taskdef>
<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.AddComponent" name="dsite.addComponent" classpathref="downloadsite.ant.path">
</taskdef>

<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.AddDependency" name="dsite.addDependency" classpathref="downloadsite.ant.path">
</taskdef>

<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.AddDetail" name="dsite.addDetail" classpathref="downloadsite.ant.path">
</taskdef>

<taskdef classname="org.eclipse.tigerstripe.releng.downloadsite.ant.UpdateNotesDetails" name="dsite.addReleaseNotes" classpathref="downloadsite.ant.path">
</taskdef>
