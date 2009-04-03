<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:ts="http://org.eclipse.tigerstripe/xml/tigerstripeExport/v2-0">

<xsl:template match="/">
  <html>
  <body>
    <p>Project: <xsl:value-of select="ts:tigerstripeProject/@name"/>
 Version: <xsl:value-of select="ts:tigerstripeProject/@version"/></p>

<hr/>

    <font color="#FF6600">
     <h2>Artifact: <xsl:value-of select="ts:tigerstripeProject/ts:artifact/@name"/></h2>
    </font>
    <p>Artifact Type: <xsl:value-of select="ts:tigerstripeProject/ts:artifact/@artifactType"/></p>
    <p>Extended Artifact: <xsl:value-of select="ts:tigerstripeProject/ts:artifact/@extendedArtifact"/></p>
    <xsl:if test="count(ts:tigerstripeProject/ts:artifact/ts:implements/ts:implementedInterface) &gt; 0">
    <p>Implemented Interfaces:</p>
    <xsl:for-each select="ts:tigerstripeProject/ts:artifact/ts:implements/ts:implementedInterface">
    <p>    <xsl:value-of select="."/></p>
    </xsl:for-each>
    </xsl:if>
    <p>isAbstract: <xsl:value-of select="ts:tigerstripeProject/ts:artifact/@isAbstract"/></p>
    <p>Description: <xsl:value-of select="ts:tigerstripeProject/ts:artifact/ts:comment"/></p>
    
    
    <font color="#000099">
     <h4>Annotations</h4>
    </font>
    <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
    <xsl:if test="count(ts:stereotypes/ts:stereotype) &gt; 0">
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
            
            
            </xsl:for-each>                              
            </table>
            </xsl:if>
            <xsl:if test="count(ts:stereotypes/ts:stereotype) = 0">
            <p>No annotations</p>
            </xsl:if>
            </xsl:for-each>

      
        <!--enumerationSpecifics   -->
    
     <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
     <xsl:if test="@artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact'">
      <font color="#000099">
       <h4>enumerationSpecifics</h4>
      </font>
      <p>baseType: <xsl:value-of select="ts:enumerationSpecifics/@baseType"/></p>
      </xsl:if>
      </xsl:for-each>
      
      
       <!-- associationSpecifics and associationClassSpecifics-->
    
    <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
     <xsl:if test="@artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact' or
                   @artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact'">
      <font color="#000099">
       <h4>Association Specifics</h4>
      </font>
      
      <table border="1">
            <tr>
            <th>Name</th>
            <th>End</th>
            <th>Type</th>
            <th>Multiplicity</th>
            <th>Aggregation</th>
            <th>Changeable</th>
            <th>Navigable</th>
            <th>Unique</th>
            <th>Ordered</th>
            <th>Visibility</th>
            </tr>
         <xsl:for-each select="ts:associationSpecifics/ts:associationEnd">
         <tr>
          <td><xsl:value-of select="@name"/></td>
          <td><xsl:value-of select="@end"/></td>
          <td><xsl:value-of select="@type"/></td>
          <td><xsl:value-of select="@multiplicity"/></td>
          <td><xsl:value-of select="@aggregation"/></td>
          <td><xsl:value-of select="@changeable"/></td>
          <td><xsl:value-of select="@navigable"/></td>
          <xsl:if test="@multiplicity='0..*' or @multiplicity='1..*' or @multiplicity='*'">
            <td><xsl:value-of select="@unique"/></td>
            <td><xsl:value-of select="@ordered"/></td>
          </xsl:if>
          <xsl:if test="@multiplicity='0..1' or @multiplicity='0'">
            <td>N/A</td>
            <td>N/A</td>
          </xsl:if>
            <td><xsl:value-of select="@visibility"/></td>
         </tr>
         </xsl:for-each>
         <xsl:for-each select="ts:associationClassSpecifics/ts:associationEnd">
         <tr>
          <td><xsl:value-of select="@name"/></td>
          <td><xsl:value-of select="@end"/></td>
          <td><xsl:value-of select="@type"/></td>
          <td><xsl:value-of select="@multiplicity"/></td>
          <td><xsl:value-of select="@aggregation"/></td>
          <td><xsl:value-of select="@changeable"/></td>
          <td><xsl:value-of select="@navigable"/></td>
          <xsl:if test="@multiplicity='0..*' or @multiplicity='1..*' or @multiplicity='*'">
            <td><xsl:value-of select="@unique"/></td>
            <td><xsl:value-of select="@ordered"/></td>
          </xsl:if>
          <xsl:if test="@multiplicity='0..1' or @multiplicity='0'">
            <td>N/A</td>
            <td>N/A</td>
          </xsl:if>
          <td><xsl:value-of select="@visibility"/></td>
         </tr>
         </xsl:for-each>
       </table>
      
      
          
    <xsl:choose>
    <xsl:when test="count(ts:associationClassSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype) &gt; 0 or
    count(ts:associationSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype) &gt; 0">
    <font color="#000099">
     <h4>Association End Annotations</h4>
    </font>
    <xsl:choose>
    <xsl:when test="count(ts:associationClassSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype) &gt; 0">
    
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Constant</th>
            <th>Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:associationClassSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
        </xsl:when>
      
      </xsl:choose>
      <xsl:choose>
    <xsl:when test="count(ts:associationSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype) &gt; 0">
    
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Constant</th>
            <th>Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:associationSpecifics/ts:associationEnd/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
        </xsl:when>
      
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <p>No annotations</p>
      </xsl:otherwise>
    </xsl:choose>
    </xsl:if>
    </xsl:for-each>
    
    
      
      <!-- dependencySpecifics   -->
    <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
     <xsl:if test="@artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact'">
      <font color="#000099">
       <h4>dependencySpecifics</h4>
      </font>
      <p>aEndTypeName: <xsl:value-of select="ts:dependencySpecifics/@aEndTypeName"/></p>
      <p>zEndTypeName: <xsl:value-of select="ts:dependencySpecifics/@zEndTypeName"/></p>
      </xsl:if>
      </xsl:for-each>
      
      <!-- querySpecifics -->
    <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
     <xsl:if test="@artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact'">
      <font color="#000099">
       <h4>querySpecifics</h4>
      </font>
      <p>returnedTypeName: <xsl:value-of select="ts:querySpecifics/@returnedTypeName"/></p>
      <p>returnedTypeMultiplicity: <xsl:value-of select="ts:querySpecifics/@returnedTypeMultiplicity"/></p>
      </xsl:if>
     </xsl:for-each>
      
          


      <!-- Selecting only artifacts that support either Labels or Fields -->

      <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
      <xsl:if test="@artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact' and
                    @artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact' and
                    @artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact'">


              <!-- Constants  Literals -->
     
        <xsl:if test="@artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact' and
                      @artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact'">
           
        <font color="#FF6600">
         <h3>Constants (Literals)</h3>
        </font>
        <xsl:if test="count(ts:literals/ts:literal) &gt; 0">
            <table border="1">
            <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Value</th>
            <th>Visibility</th>
            <th>Description</th>
            </tr>
            <xsl:for-each select="ts:literals/ts:literal">
            <tr>
            <td><xsl:value-of select="@name"/></td>
            <td><xsl:value-of select="@type"/></td>
            <td><xsl:value-of select="@value"/></td>
             <td><xsl:value-of select="@visibility"/></td>
            <td><xsl:value-of select="ts:comment"/></td>
            </tr>
            </xsl:for-each>
            </table>
            

    <font color="#000099">
     <h4>Constant Annotations</h4>
    </font>
    <xsl:choose>
    <xsl:when test="count(ts:literals/ts:literal/ts:stereotypes/ts:stereotype) &gt; 0">
    
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Constant</th>
            <th>Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:literals/ts:literal/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
        </xsl:when>
      <xsl:otherwise>
      <p>No annotations</p>
      </xsl:otherwise>
      </xsl:choose>

       </xsl:if>
        
       <xsl:if test="count(ts:literals/ts:literal) = 0">
       <p>No constants</p>
       </xsl:if>
            
       </xsl:if>

              <!-- Attributes  Fields -->

        <xsl:if test="@artifactType != 'org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact'">

        <font color="#FF6600">
         <h3>Attributes (Fields)</h3>
        </font>
        <xsl:if test="count(ts:fields/ts:field) &gt; 0">
            <table border="1">
            <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Multiplicity</th>
            <th>Visibility</th>
            <th>Readonly</th>
            <th>Unique</th>
            <th>Ordered</th>
            <th>Default Value (optional)</th>
            <th>Description</th>
            </tr>
            <xsl:for-each select="ts:fields/ts:field">
            <tr>
            <td><xsl:value-of select="@name"/></td>
            <td><xsl:value-of select="@type"/></td>
            <td><xsl:value-of select="@typeMultiplicity"/></td>
             <td><xsl:value-of select="@visibility"/></td>
            <td><xsl:value-of select="@readonly"/></td>
            <xsl:if test="@typeMultiplicity='0..*' or @typeMultiplicity='1..*' or @typeMultiplicity='*'">
            <td><xsl:value-of select="@unique"/></td>
            <td><xsl:value-of select="@ordered"/></td>
            </xsl:if>
            <xsl:if test="@typeMultiplicity='0..1' or @typeMultiplicity='0'">
            <td>N/A</td>
            <td>N/A</td>
            </xsl:if>
            <td><xsl:value-of select="@defaultValue"/></td>
            <td><xsl:value-of select="ts:comment"/></td>
            </tr>
            </xsl:for-each>
            </table>

<font color="#000099">
 <h4>Attribute Annotations</h4>
</font>
 <xsl:choose>
  <xsl:when test="count(ts:fields/ts:field/ts:stereotypes/ts:stereotype) &gt; 0">
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Attribute</th>
            <th>Annotation Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:fields/ts:field/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
   </xsl:when>
   <xsl:otherwise>
    <p>No annotations</p>
   </xsl:otherwise>
  </xsl:choose>
     
     </xsl:if>
     </xsl:if>
     <xsl:if test="count(ts:fields/ts:field) = 0">
     <p>No attributes</p>
     </xsl:if>
     </xsl:if>
     </xsl:for-each>
     
     

              <!--       Methods        -->
              
        <xsl:for-each select="ts:tigerstripeProject/ts:artifact">
        <xsl:if test="@artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact' or 
                      @artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact' or
                      @artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact' or
                      @artifactType = 'org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact'">
        
        <font color="#FF6600">
         <h3>Methods</h3>
        </font>
           <xsl:if test="count(ts:methods/ts:method) &gt; 0">
            <table border="1">
            <tr>
            <th>Name</th>
            <th>isAbstract</th>
            <th>Unique</th>
            <th>Ordered</th>
            <th>isVoid</th>
            <th>Type</th>            
            <th>Multiplicity</th>            
            <th>Visibility</th>
            <th>Return Name</th>
            <th>Default Value</th>
            <th>Description</th>
            <th>Arguments <small>(click on argument name to jump to argument details)</small></th>
            <th>Exceptions</th>
            </tr>
            <xsl:for-each select="ts:methods/ts:method">
            <tr>
            <td><xsl:value-of select="@name"/></td>
            <td><xsl:value-of select="@isAbstract"/></td>
            <xsl:if test="@returnTypeMultiplicity='0..*' or @returnTypeMultiplicity='1..*' or @returnTypeMultiplicity='*'">
            <td><xsl:value-of select="@unique"/></td>
            <td><xsl:value-of select="@ordered"/></td>
            </xsl:if>
            <xsl:if test="@returnTypeMultiplicity='0..1' or @returnTypeMultiplicity='0'">
            <td>N/A</td>
            <td>N/A</td>
            </xsl:if>
            
            
            <td><xsl:value-of select="@isVoid"/></td>
            <td><xsl:value-of select="@returnType"/></td>
            
            <td><xsl:value-of select="@returnTypeMultiplicity"/></td>
            
             <td><xsl:value-of select="@visibility"/></td>
            <td><xsl:value-of select="@methodReturnName"/></td>
            <td><xsl:value-of select="@defaultReturnValue"/></td>
            <td><xsl:value-of select="ts:comment"/></td>
            <xsl:variable name="arg-name"><xsl:value-of select="ts:arguments/ts:argument/@name"/></xsl:variable>            
            <xsl:variable name="method-name"><xsl:value-of select="@name"/></xsl:variable>
            
              <a href="#{$arg-name}{$method-name}">            
                <td>
                  <xsl:for-each select="ts:arguments/ts:argument">
                  
                    <font color="#FF6600"><p><xsl:value-of select="@name"/></p></font>
                    
                   </xsl:for-each>
                </td>
              </a>
            
            <td><xsl:for-each select="ts:exceptions/ts:exception"><p><xsl:value-of select="@name"/></p></xsl:for-each></td>
            </tr>
            </xsl:for-each>
            </table>

<font color="#000099">
 <h4>Method Annotations</h4>
</font>
 <xsl:choose>
  <xsl:when test="count(ts:methods/ts:method/ts:stereotypes/ts:stereotype) &gt; 0">
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Method</th>
            <th>Annotation Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:methods/ts:method/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
   </xsl:when>
   <xsl:otherwise>
    <p>No annotations</p>
   </xsl:otherwise>
  </xsl:choose>
  
<font color="#000099">
 <h4>Method Return Annotations</h4>
</font>
 <xsl:choose>
  <xsl:when test="count(ts:methods/ts:method/ts:returnStereotypes/ts:stereotype) &gt; 0">
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Method</th>
            <th>Method Return Name</th>
            <th>Annotation Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:methods/ts:method/ts:returnStereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="../../@methodReturnName"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
   </xsl:when>
   <xsl:otherwise>
    <p>No annotations</p>
   </xsl:otherwise>
  </xsl:choose>


              <!--       Arguments        -->


        <font color="#000099">
         <h4>   Arguments</h4>
        </font>
            <table border="1">
            <tr>
            <th>Name</th>
            <th>Method</th>
            <th>Type</th>
            <th>Multiplicity</th>
            <th>Unique</th>
            <th>Ordered</th>
            <th>Default Value (optional)</th>
            <th>Description</th>
            
            </tr>
            <xsl:for-each select="ts:methods/ts:method/ts:arguments/ts:argument">
            <tr>
            <xsl:variable name="arg-name"><xsl:value-of select="@name"/></xsl:variable>
            <xsl:variable name="method-name"><xsl:value-of select="../../@name"/></xsl:variable>
            <td><a name="{$arg-name}{$method-name}"><xsl:value-of select="@name"/></a></td>
            <td><xsl:value-of select="../../@name"/></td>
            <td><xsl:value-of select="@type"/></td>
            <td><xsl:value-of select="@typeMultiplicity"/></td>
            <xsl:if test="@typeMultiplicity='0..*' or @typeMultiplicity='1..*' or @typeMultiplicity='*'">
            <td><xsl:value-of select="@unique"/></td>
            <td><xsl:value-of select="@ordered"/></td>
            </xsl:if>
            <xsl:if test="@typeMultiplicity='0..1' or @typeMultiplicity='0'">
            <td>N/A</td>
            <td>N/A</td>
            </xsl:if>
            <td><xsl:value-of select="@defaultValue"/></td>
            <td><xsl:value-of select="ts:comment"/></td>
            
            </tr>
            </xsl:for-each>
            </table>

<font color="#000099">
 <h4>Argument Annotations</h4>
</font>
 <xsl:choose>
  <xsl:when test="count(ts:methods/ts:method/ts:arguments/ts:argument/ts:stereotypes/ts:stereotype) &gt; 0">
    <table border="1">
            <tr>
            <th>Annotation Name</th>
            <th>Argument</th>
            <th>Annotation Attribute Name</th>
            <th>isArray</th>
            <th>Value</th>
            </tr>
            <xsl:for-each select="ts:methods/ts:method/ts:arguments/ts:argument/ts:stereotypes/ts:stereotype">
            
            <xsl:choose>
             <xsl:when test="count(ts:stereotypeAttribute) &gt; 0">
             <xsl:variable name="row-span"><xsl:value-of select="count(ts:stereotypeAttribute)"/></xsl:variable>
              <tr>
               <td rowspan="{$row-span}"><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute[1]/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute[1]/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              <xsl:for-each select="ts:stereotypeAttribute[position()>1]">
              <tr>
               <td><xsl:value-of select="../../../@name"/></td>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="@array"/></td>
               <td>
                <xsl:for-each select="ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
              </xsl:for-each>
             </xsl:when>
            <xsl:otherwise>
             <tr>
               <td><xsl:value-of select="@name"/></td>
               <td><xsl:value-of select="../../@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@name"/></td>
               <td><xsl:value-of select="ts:stereotypeAttribute/@array"/></td>
               <td>
                <xsl:for-each select="ts:stereotypeAttribute/ts:value">
                 <p><xsl:value-of select="."/></p>
                </xsl:for-each>
               </td>
              </tr>
            </xsl:otherwise>
           </xsl:choose>
          </xsl:for-each>
         </table>  
   </xsl:when>
   <xsl:otherwise>
    <p>No annotations</p>
   </xsl:otherwise>
  </xsl:choose>
            
            </xsl:if>
            
            <xsl:if test="count(ts:methods/ts:method) = 0">
            <p>No methods</p>
            </xsl:if>
            
     </xsl:if>
     </xsl:for-each>

  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
