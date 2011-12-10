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
      <xsl:apply-templates select="//div[@id='res']"/>
   </xsl:template>

   <xsl:template match="div[@id='res']">
      <trickl:google-directory-pages>
         <xsl:apply-templates select="descendant-or-self::div[@class='g']"/>
      </trickl:google-directory-pages>
   </xsl:template>

   <xsl:template match="div[@class='g']">
      <trickl:google-directory-page>
         <trickl:web-page-url><xsl:value-of select="descendant-or-self::a[@class='l']/attribute::href"/></trickl:web-page-url>
         <trickl:uri><xsl:value-of select="table/descendant::a/attribute::href"/></trickl:uri>
      </trickl:google-directory-page>
   </xsl:template>

</xsl:stylesheet>
