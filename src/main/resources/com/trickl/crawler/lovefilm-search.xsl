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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:trickl="http://trickl.com">
   <xsl:output indent="yes"/>

   <xsl:template match="/">
      <xsl:apply-templates select="//xhtml:div[@id='film_listings']"/>
   </xsl:template>

   <xsl:template match="xhtml:div[@id='film_listings']">
      <trickl:lovefilm-products>
         <xsl:apply-templates select="descendant::xhtml:div[@class='fl_detail_info']"/>
      </trickl:lovefilm-products>
   </xsl:template>

   <xsl:template match="xhtml:div[@class='fl_detail_info']">
      <trickl:lovefilm-product>
         <trickl:title><xsl:value-of select="descendant-or-self::xhtml:a"/></trickl:title>
         <trickl:uri><xsl:value-of select="descendant-or-self::xhtml:a/attribute::href"/></trickl:uri>
         <xsl:for-each select="xhtml:div">
            <xsl:if test="contains(text(), 'Format:')">
               <trickl:format><xsl:value-of select="normalize-space(substring-after(., 'Format:'))"/></trickl:format>
            </xsl:if>
         </xsl:for-each>
      </trickl:lovefilm-product>
   </xsl:template>

</xsl:stylesheet>
