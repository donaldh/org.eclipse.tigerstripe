<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:ts="http://org.eclipse.tigerstripe/xml/tigerstripeExport/v2-0">

<xsl:template match="/">
 <html>
    

    <body>
    <h2>Artifacts within Project: <xsl:value-of select="ts:tigerstripeProject/@name"/>
        Version: <xsl:value-of select="ts:tigerstripeProject/@version"/></h2>
    <hr/>
    <p/>
    

    <table border="1">
    <tr bgcolor="#9acd32">
      <th align="left">Artifact Name</th>
      <th align="left">Type</th>
    </tr>
    <xsl:for-each select="ts:tigerstripeProject/ts:artifactFile">
    <xsl:sort select="@name"/>
    <xsl:variable name="file-name"><xsl:value-of select="@fileName"/></xsl:variable>
    <tr>    
      <td><a href="{$file-name}" target="_blank"><xsl:value-of select="@name"/></a></td>   
      <td><xsl:value-of select="@artifactType"/></td>
    </tr>
    </xsl:for-each>
    </table>
    <br/>
    
  </body>

  </html>
  
 
</xsl:template>
</xsl:stylesheet>