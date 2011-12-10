<?xml version="1.0" encoding="ISO-8859-1"?>
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:trickl="http://trickl.com">
   <xsl:output indent="yes"/>

   <xsl:template match="/">
      <xsl:apply-templates select="//div[@id='main']"/>
      <xsl:apply-templates select="//div[@class='maindetails']"/>
   </xsl:template>

   <xsl:template match="div[@id='main']">
      <trickl:imdb-records>
         <xsl:apply-templates select="descendant::table/tbody/tr/td/a"/>
      </trickl:imdb-records>
   </xsl:template>

   <xsl:template match="tr/td/a">
      <xsl:if test="string-length(.) &gt; 0">
         <trickl:imdb-record>
            <trickl:title><xsl:value-of select="."/></trickl:title>
            <trickl:uri><xsl:value-of select="attribute::href"/></trickl:uri>
            <trickl:release-date><xsl:value-of select="substring(normalize-space(../text()), 2, 4)"/></trickl:release-date>
            <xsl:choose>
               <xsl:when test="contains(../text(), '(VG)')">
                  <trickl:format>Video Game</trickl:format>
               </xsl:when>
               <xsl:when test="contains(../text(), '(V)')">
                  <trickl:format>Video</trickl:format>
               </xsl:when>
               <xsl:when test="contains(../text(), '(TV)')">
                  <trickl:format>TV</trickl:format>
               </xsl:when>
               <xsl:otherwise>
                  <trickl:format>Cinema</trickl:format>
               </xsl:otherwise>
            </xsl:choose>
         </trickl:imdb-record>
      </xsl:if>
   </xsl:template>

   <xsl:template match="div[@class='maindetails']">
      <trickl:imdb-records>
         <trickl:imdb-record>
            <trickl:title><xsl:value-of select="descendant::h1/text()"/></trickl:title>
            <trickl:uri><xsl:value-of select="//html/head/link[@rel='canonical']/attribute::href"/></trickl:uri>
            <trickl:release-date><xsl:value-of select="descendant::h1/span/a/text()"/></trickl:release-date>
            <xsl:choose>
               <xsl:when test="contains(descendant::h1/span, '(VG)')">
                  <trickl:format>Video Game</trickl:format>
               </xsl:when>
               <xsl:when test="contains(descendant::h1/span, '(V)')">
                  <trickl:format>Video</trickl:format>
               </xsl:when>
               <xsl:when test="contains(descendant::h1/span, '(TV)')">
                  <trickl:format>TV</trickl:format>
               </xsl:when>
               <xsl:otherwise>
                  <trickl:format>Cinema</trickl:format>
               </xsl:otherwise>
            </xsl:choose>
         </trickl:imdb-record>
      </trickl:imdb-records>
   </xsl:template>

</xsl:stylesheet>
