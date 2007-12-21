<?xml version="1.0" encoding="ISO-8859-1" ?>
<!--
  Copyright (c) 2007 Cisco Systems, Inc.
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
    http://www.eclipse.org/legal/epl-v10.html

   Contributors:
      E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:UML="http://www.omg.org/UML" version="1.0"
	exclude-result-prefixes="#default">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match='UML:MultiplicityRange[@lower="*"]'>
		<UML:MultiplicityRange lower="-1" upper="-1"/>
	</xsl:template>
	
	<xsl:template match='UML:MultiplicityRange[@upper="*"]'>
		<UML:MultiplicityRange lower="-1" upper="-1"/>
	</xsl:template>
	
	<xsl:template match='UML:MultiplicityRange[@lower="n"]'>
		<UML:MultiplicityRange lower="-1" upper="-1"/>
	</xsl:template>

	<xsl:template match='UML:MultiplicityRange[@upper="n"]'>
		<UML:MultiplicityRange lower="-1" upper="-1"/>
	</xsl:template>
</xsl:stylesheet>

