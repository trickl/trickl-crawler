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
      <xsl:apply-templates select="descendant-or-self::div[@id='main']"/>
   </xsl:template>

   <xsl:template match="div[@id='main']">
      <xsl:apply-templates select="descendant-or-self::ol[@id='results-tab']"/>
   </xsl:template>

   <xsl:template match="ol[@id='results-tab']">
      <trickl:yahoo-site-stats>
         <xsl:apply-templates select="descendant-or-self::li"/>
      </trickl:yahoo-site-stats>
   </xsl:template>

   <xsl:template match="li">
      <xsl:choose>
         <xsl:when test="contains(., 'Pages')">
            <trickl:links-pages>
               <xsl:value-of select="substring-before(substring-after(., '('), ')')"/>
            </trickl:links-pages>
         </xsl:when>
         <xsl:when test="contains(., 'Inlinks')">
            <trickl:links-in>
               <xsl:value-of select="substring-before(substring-after(., '('), ')')"/>
            </trickl:links-in>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

</xsl:stylesheet>


