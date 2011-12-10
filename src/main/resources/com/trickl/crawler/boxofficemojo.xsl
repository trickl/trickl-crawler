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
      <trickl:boxofficemojo-records>
         <xsl:apply-templates select="//table/tbody/tr"/>
      </trickl:boxofficemojo-records>
   </xsl:template>

   <xsl:template match="tr">
      <xsl:choose>
         <xsl:when test="contains(td[1]/font/a/attribute::href, '/movies/?id=')">
            <trickl:boxofficemojo-record>
               <trickl:uri><xsl:value-of select="td[1]/font/a/attribute::href"/></trickl:uri>
               <trickl:film>
                  <trickl:title><xsl:value-of select="translate(normalize-space(td[1]),'&#x0A;','')"/></trickl:title>
                  <trickl:release-date><xsl:value-of select="td[3]"/></trickl:release-date>
                  <trickl:domestic-gross-revenue><xsl:value-of select="td[2]"/></trickl:domestic-gross-revenue>
               </trickl:film>
            </trickl:boxofficemojo-record>
         </xsl:when>
         <xsl:when test="contains(font, 'Domestic Total Gross: ')">
            <trickl:boxofficemojo-record>
               <trickl:film>
                  <trickl:title><xsl:value-of select="ancestor::td/font/n/text()"/></trickl:title>
                  <trickl:release-date><xsl:value-of select="substring-after(../tr[2]/td[2], 'Release Date: ')"/></trickl:release-date>
                  <trickl:domestic-gross-revenue><xsl:value-of select="substring-after(font, 'Domestic Total Gross: ')"/></trickl:domestic-gross-revenue>
               </trickl:film>
            </trickl:boxofficemojo-record>
         </xsl:when>
      </xsl:choose>
   </xsl:template>

</xsl:stylesheet>
