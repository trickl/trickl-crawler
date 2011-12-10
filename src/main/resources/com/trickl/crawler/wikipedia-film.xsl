<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:trickl="http://trickl.com">
   <xsl:output indent="yes"/>
   <xsl:strip-space elements="*"/>
   <xsl:preserve-space elements="xhtml:p"/>

   <xsl:template match="/">
      <xsl:for-each select="//xhtml:div[@id='content']">
         <xsl:call-template name="content-template"/>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="content-template">
      <xsl:choose>
         <xsl:when test="//xhtml:table[@id='disambigbox']">
            <!-- Disambiguation Page -->
            <trickl:wikipedia-pages>
               <xsl:for-each select="//xhtml:a">
                  <xsl:call-template name="disambigation-template"/>
               </xsl:for-each>
            </trickl:wikipedia-pages>
         </xsl:when>
         <xsl:when test="//xhtml:table/@class[contains(.,'infobox')]">
            <!-- Film Page -->
            <trickl:wikipedia-film>
               <trickl:title><xsl:value-of select="translate(*[@id='firstHeading'],'&#x0A;','')"/></trickl:title>
               <trickl:film>
                  <xsl:choose>
                     <xsl:when test="//xhtml:span[@id='Plot']">
                        <!-- (First choice) First paragraph of the plot -->
                        <trickl:synopsis><xsl:value-of select="//xhtml:span[@id='Plot']/following::xhtml:p[1]"/></trickl:synopsis>
                     </xsl:when>
                     <xsl:when test="//xhtml:div[@id='bodyContent']/xhtml:p">
                        <!-- (Second choice) First paragraph in the body -->
                        <trickl:synopsis><xsl:value-of select="//xhtml:div[@id='bodyContent']/xhtml:p[1]"/></trickl:synopsis>
                     </xsl:when>
                  </xsl:choose>
                  <xsl:for-each select="//xhtml:table/@class[contains(.,'infobox')]">
                     <xsl:call-template name="infobox-template"/>
                  </xsl:for-each>
                  <trickl:tags>
                     <xsl:for-each select="//xhtml:a">
                        <xsl:call-template name="category-template"/>
                     </xsl:for-each>
                  </trickl:tags>
               </trickl:film>
               <trickl:poster-location><xsl:value-of select="//xhtml:table[contains(@class,'infobox')]/descendant::xhtml:a[contains(@class,'image')]/xhtml:img/attribute::src"/></trickl:poster-location>
            </trickl:wikipedia-film>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

   <xsl:template name="infobox-template">
      <trickl:title><xsl:value-of select="translate(//xhtml:tr[1],'&#x0A;','')"/></trickl:title>
      <xsl:for-each select="//xhtml:tr">
         <xsl:call-template name="infobox-row-template"/>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="category-template">
      <xsl:variable name = "title" select = "attribute::title"/>
      <xsl:if test="string-length(.) &gt; 0 and (contains(attribute::href, 'Category:'))">
         <xsl:choose>
            <!-- Blacklist -->
            <xsl:when test="string(number(substring-before(., ' films'))) != 'NaN'"></xsl:when>
            <xsl:when test="contains(., 'stub') and string-length(substring-after(., 'stub')) = 0"></xsl:when>
            <xsl:when test="contains(., 'stubs') and string-length(substring-after(., 'stubs')) = 0"></xsl:when>
            <xsl:when test="contains(., 'directed')"></xsl:when>
            <xsl:when test="contains(., 'produced')"></xsl:when>
            <xsl:when test="contains(., 'unsourced')"></xsl:when>
            <xsl:when test="contains(., 'Wikipedia')"></xsl:when>            
            <xsl:when test="starts-with(., 'Articles')"></xsl:when>
            <xsl:when test="starts-with(., 'All articles')"></xsl:when>
            <xsl:when test="starts-with(., 'This article')"></xsl:when>
            <xsl:when test="starts-with(., 'this article')"></xsl:when>
            <xsl:when test="starts-with(., 'biographical article')"></xsl:when>
            <xsl:when test="starts-with(., 'Use ')"></xsl:when>
            <!-- Default passthrough -->
            <xsl:when test="contains(., ' films') and string-length(substring-after(., 'films')) = 0">
               <trickl:tag><xsl:value-of select="normalize-space(substring-before(., ' films'))"/></trickl:tag>
            </xsl:when>
            <xsl:when test="starts-with(., 'Category:')">
               <trickl:tag><xsl:value-of select="normalize-space(substring-after(., 'Category:'))"/></trickl:tag>
            </xsl:when>
            <xsl:otherwise>
               <trickl:tag><xsl:value-of select="normalize-space(.)"/></trickl:tag>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
   </xsl:template>

   <xsl:template name="infobox-row-template">
      <xsl:variable name = "headertext" select = "xhtml:th/text()"/>
      <xsl:choose>
         <xsl:when test="contains($headertext, 'Direct')">
            <trickl:directors>
               <xsl:call-template name="crew-member-list"/>
            </trickl:directors>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Writ')">
            <trickl:writers>
               <xsl:call-template name="writer-list"/>
            </trickl:writers>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Produce')">
            <trickl:producers>
               <xsl:call-template name="producer-list"/>
            </trickl:producers>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Star')">
            <trickl:cast>
               <xsl:call-template name="cast-member-list"/>
            </trickl:cast>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Music')">
            <trickl:music-by>
               <xsl:call-template name="crew-member-list"/>
            </trickl:music-by>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Cinematography')">
            <trickl:cinematography-by>
               <xsl:call-template name="crew-member-list"/>
            </trickl:cinematography-by>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Edit')">
            <trickl:editors>
               <xsl:call-template name="crew-member-list"/>
            </trickl:editors>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Studio')">
            <trickl:studio>
               <xsl:call-template name="cell-value"/>
            </trickl:studio>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Distribut')">
            <trickl:distributor>
               <xsl:call-template name="cell-value"/>
            </trickl:distributor>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Release')">
            <trickl:release-date>
               <xsl:call-template name="cell-value"/>
            </trickl:release-date>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Running time')">
            <trickl:running-time>
               <xsl:call-template name="cell-value"/>
            </trickl:running-time>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Country')">
            <trickl:country>
               <xsl:call-template name="cell-value"/>
            </trickl:country>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Language')">
            <trickl:language>
               <xsl:call-template name="cell-value"/>
            </trickl:language>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Budget')">
            <trickl:budget>
               <xsl:call-template name="cell-value"/>
            </trickl:budget>
         </xsl:when>
         <xsl:when test="contains($headertext, 'Gross revenue')">
            <trickl:domestic-gross-revenue>
               <xsl:call-template name="cell-value"/>
            </trickl:domestic-gross-revenue>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

   <xsl:template name="cast-member-list">
      <xsl:param name="detail" select="xhtml:td"/>
      <xsl:for-each select="$detail/xhtml:a">
         <trickl:cast-member>
            <xsl:attribute name="billing"><xsl:value-of select="position()"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(text())"/></trickl:full-name>
         </trickl:cast-member>
      </xsl:for-each>
      <xsl:for-each select="$detail/text()">
         <trickl:cast-member>
            <xsl:attribute name="billing"><xsl:value-of select="position() + 100"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(.)"/></trickl:full-name>
         </trickl:cast-member>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="crew-member-list">
      <xsl:param name="detail" select="xhtml:td"/>
      <xsl:for-each select="$detail/xhtml:a">
         <trickl:crew-member>
            <xsl:attribute name="credit"><xsl:value-of select="position()"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(text())"/></trickl:full-name>
         </trickl:crew-member>
      </xsl:for-each>
      <xsl:for-each select="$detail/text()">
         <trickl:crew-member>
            <xsl:attribute name="credit"><xsl:value-of select="position() + 100"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(.)"/></trickl:full-name>
         </trickl:crew-member>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="producer-list">
      <xsl:param name="detail" select="xhtml:td"/>
      <xsl:for-each select="$detail/xhtml:a">
         <trickl:producer>
            <xsl:attribute name="credit"><xsl:value-of select="position()"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(text())"/></trickl:full-name>
         </trickl:producer>
      </xsl:for-each>
      <xsl:for-each select="$detail/text()">
         <trickl:producer>
            <xsl:attribute name="credit"><xsl:value-of select="position() + 100"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(.)"/></trickl:full-name>
         </trickl:producer>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="writer-list">
      <xsl:param name="detail" select="xhtml:td"/>
      <xsl:for-each select="$detail/xhtml:a">
         <trickl:writer>
            <xsl:attribute name="credit"><xsl:value-of select="position()"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(text())"/></trickl:full-name>
         </trickl:writer>
      </xsl:for-each>
      <xsl:for-each select="$detail/text()">
         <trickl:writer>
            <xsl:attribute name="credit"><xsl:value-of select="position() + 100"/></xsl:attribute>
            <trickl:full-name><xsl:value-of select="normalize-space(.)"/></trickl:full-name>
         </trickl:writer>
      </xsl:for-each>
   </xsl:template>

   <xsl:template name="cell-value">
      <xsl:param name="detail" select="xhtml:td"/>
      <xsl:choose>
         <xsl:when test="string-length($detail/xhtml:a/text()) &gt; 0">
            <xsl:value-of select="normalize-space($detail/xhtml:a/text())"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="normalize-space($detail/text())"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template name="disambigation-template">
      <xsl:if test="contains(@href, 'wiki')">
         <trickl:wikipedia-page>
            <trickl:title><xsl:value-of select="."/></trickl:title>
            <trickl:uri><xsl:value-of select="@href"/></trickl:uri>
         </trickl:wikipedia-page>
      </xsl:if>
   </xsl:template>

</xsl:stylesheet>
