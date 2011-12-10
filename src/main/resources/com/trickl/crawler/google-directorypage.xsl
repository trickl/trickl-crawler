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
      <trickl:google-page-ranks>
         <xsl:apply-templates select="descendant-or-self::table"/>
      </trickl:google-page-ranks>
   </xsl:template>

   <xsl:template match="table">
      <xsl:apply-templates select="descendant-or-self::tr"/>
   </xsl:template>

   <xsl:template match="tr">
      <xsl:if test="contains(descendant::a/img/attribute::src, '/images/pos.gif')">
         <trickl:google-page-rank>
            <trickl:web-page-url><xsl:value-of select="descendant::a[2]/attribute::href"/></trickl:web-page-url>
            <trickl:score><xsl:value-of select="number(substring-before(descendant::a/img/attribute::width, ',')) div 4" /></trickl:score>
         </trickl:google-page-rank>
      </xsl:if>      
   </xsl:template>

</xsl:stylesheet>
