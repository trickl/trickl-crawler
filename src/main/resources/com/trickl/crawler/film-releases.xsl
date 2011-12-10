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

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:trickl="http://trickl.com">
   <xsl:output indent="yes"/>
   <xsl:strip-space elements="*"/> 

   <xsl:template match="/">
      <xsl:param name="year" select="descendant-or-self::td[@id='films_head']/font[@id='emph_orange']"/>      
      <xsl:apply-templates select="descendant-or-self::td[@id='films_list_back']">
         <xsl:with-param name="year" select="$year"/>
      </xsl:apply-templates>
   </xsl:template>

   <xsl:template match="td[@id='films_list_back']">
      <xsl:param name="year"/>
      <xsl:if test="count(descendant-or-self::table/tbody/tr/td[@id='year_date']) &gt; 0">
         <trickl:films>
            <xsl:apply-templates select="descendant-or-self::table/tbody/tr/td[@id='year_date']">
               <xsl:with-param name="year" select="$year"/>
            </xsl:apply-templates>
         </trickl:films>
      </xsl:if>
   </xsl:template>

   <xsl:template match="td[@id='year_date']">
      <xsl:param name="year"/>
      <xsl:param name="release-date" select="concat(., ', ', $year)"/>
      <xsl:param name="following-release-date" select="parent::*/following-sibling::tr/td[@id='year_date']"/>
      <xsl:apply-templates select="parent::*/following-sibling::tr/td[@id='years_text']">
          <xsl:with-param name="release-date" select="$release-date"/>
          <xsl:with-param name="following-release-date" select="$following-release-date"/>
      </xsl:apply-templates>
   </xsl:template>

   <xsl:template match="td[@id='years_text']">
      <xsl:param name="release-date"/>
      <xsl:param name="following-release-date"/>      
      <xsl:variable name="test-release-date" select="parent::*/following-sibling::tr/td[@id='year_date']"/>
      <xsl:if test="contains($test-release-date, $following-release-date) and contains($following-release-date, $test-release-date)">
         <trickl:film>
            <trickl:title><xsl:value-of select="translate(normalize-space(li/a),'&#x0A;','')"/></trickl:title>
            <trickl:studio><xsl:value-of select="normalize-space(li/font[@id='years_text_studios'])"/></trickl:studio>
            <trickl:release-date><xsl:value-of select="$release-date"/></trickl:release-date>
         </trickl:film>
      </xsl:if>
   </xsl:template>

   
</xsl:stylesheet>
