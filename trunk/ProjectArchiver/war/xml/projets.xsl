<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	

	<xsl:template match="/">
		<html>
			<head>
				<title>Listing de tous les projets</title>
				<link type="text/css" rel="stylesheet" href="projets.css" />
			</head>
			<body>
				<xsl:apply-templates select="projets"/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="projets">
	
		<a name="home">
			<h1>Liste des projets réalisés à la HEIG</h1>
		</a>
		
		<table border ="2" align="center">
			<tr>
				<td colspan="2" class="colonneGauche" valign="top">
					<xsl:for-each select="//projet">
						<p><a href="#{generate-id()}">
						<xsl:value-of select="titre"/>
					</a></p>
					</xsl:for-each>
				</td>
				
				<td>
					<xsl:apply-templates select="projet"/>
				</td>
			</tr>
		</table>
		
	</xsl:template>
	
	<xsl:template match="projet">
		<tr>
			<td colspan="2">
				<a name="{generate-id()}">
					<h2><xsl:value-of select="@id"/> - <xsl:value-of select="titre"/></h2>
				</a>	
			</td>
		</tr>	
		
		
		<tr>
			<td class="Etiquette"><xsl:text>Ajouté le : </xsl:text></td>
			<td class="Contenu"><xsl:apply-templates select="dateAjout"/></td>
		</tr>
		
		<tr>
			<td class="Etiquette"><xsl:text>Auteur : </xsl:text></td>
			<td class="Contenu"><xsl:apply-templates select="auteurs"/></td>
		</tr>
		
		<tr>
			<td class="Etiquette"><xsl:text>Branche : </xsl:text></td>
			<td class="Contenu">
				<xsl:variable name="idCoursCourant" select="idBranche"/>
				<xsl:value-of select="document('listeDesCours.xml')//cours[@id=$idCoursCourant]/nom"/>			
			</td>
		</tr>
		
		<tr>
			<td class="Etiquette"><xsl:text>Responsables: </xsl:text></td>
			<td class="Contenu"><xsl:apply-templates select="responsables"/></td>
		</tr>
		<tr>
			<td class="Etiquette"><xsl:text>Mots cle: </xsl:text></td>
			<td class="Contenu"><xsl:apply-templates select="motsCle"/></td>
			
		</tr>
		
		<tr>
			<td class="Etiquette" colspan="2">
				<br/>
					<xsl:text>Synopsis</xsl:text>
				<br/>
			</td>
		
		</tr>
		<tr>
			<td class="Resume" colspan="2"><xsl:value-of  select="synopsis"/></td>
		</tr>
		<tr>
			
			<td class="Interligne">
				<xsl:variable name="archive" select="nomArchive"/>
				<a href="{$archive}">Télécharger l'archive</a>
			</td>	
			
			<td	 class="Interligne">
				<a href="#home">home</a>
			</td>			
			
		</tr>	
		
	</xsl:template>
	
	
	<xsl:template match="dateAjout">
		<xsl:value-of select="."/>
	</xsl:template>
	
	<xsl:template match="auteur|responsables">
		
		<xsl:value-of select="."/>	
		<br/>
			
	</xsl:template>
	
	<xsl:template match="@Age">
		<xsl:text>Age : </xsl:text>
		<xsl:value-of select="."/>
		<xsl:text> ans</xsl:text>
	</xsl:template>
	
	<xsl:template match="Adresse">
	
		<xsl:value-of select="Rue"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="NoRue"/>
		<BR/>
		<xsl:value-of select="NPA"/>
		<xsl:text> </xsl:text>
		<xsl:value-of select="Localite"/>
				
	</xsl:template>
	
	<xsl:template match="motsCle">
		<xsl:apply-templates select="motCle"/>	
	</xsl:template>
	
	<xsl:template match="motCle">
		<li>
		<xsl:value-of select="."/>	
		</li>
	</xsl:template>

</xsl:stylesheet>