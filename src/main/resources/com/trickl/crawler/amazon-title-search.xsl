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
      <xsl:apply-templates select="//div[@id='Results']"/>

   </xsl:template>

   <xsl:template match="div[@id='Results']">
      <trickl:amazon-products>
         <xsl:apply-templates select="descendant-or-self::tr/a/span[@class='srTitle']"/>
      </trickl:amazon-products>
   </xsl:template>

   <xsl:template match="tr/a/span[@class='srTitle']">
      <trickl:amazon-product>
         <trickl:title><xsl:value-of select="text()"/></trickl:title>
         <trickl:uri><xsl:value-of select="../attribute::href"/></trickl:uri>
         <trickl:format><xsl:value-of select="../../descendant-or-self::span[@class='binding']"/></trickl:format>
      </trickl:amazon-product>
   </xsl:template>

</xsl:stylesheet>
