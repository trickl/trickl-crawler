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
   <xsl:strip-space  elements="*"/>

   <xsl:template match="/">
      <xsl:apply-templates select="descendant-or-self::xhtml:table[@id='siteStats']"/>
   </xsl:template>

   <xsl:template match="xhtml:table[@id='siteStats']">
      <trickl:alexa-site-stats>
         <xsl:apply-templates select="descendant-or-self::xhtml:td"/>
      </trickl:alexa-site-stats>
   </xsl:template>

   <xsl:template match="xhtml:td">
      <xsl:variable name = "label" select = "xhtml:div[2]"/>
      <xsl:choose>
         <xsl:when test="contains($label, 'Alexa Traffic Rank')">
            <trickl:traffic-rank>
               <xsl:value-of select="normalize-space(xhtml:div)"/>
            </trickl:traffic-rank>
         </xsl:when>
         <xsl:when test="contains($label, 'Sites Linking In')">
            <trickl:links-in>
               <xsl:value-of select="normalize-space(xhtml:div)"/>
            </trickl:links-in>
         </xsl:when>
         <xsl:when test="contains($label, 'Online Since')">
            <xsl:if test="not(contains(xhtml:div, 'No data'))">
               <trickl:registration-date>
                  <xsl:value-of select="normalize-space(xhtml:div)"/>
               </trickl:registration-date>
            </xsl:if>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

</xsl:stylesheet>

