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
      <xsl:apply-templates select="//xhtml:ol[@id='blogs-authority']"/>
      <xsl:apply-templates select="//xhtml:table[@class='blog-page']"/>
   </xsl:template>

   <xsl:template match="xhtml:ol[@id='blogs-authority']">
      <trickl:technorati-records>
         <xsl:apply-templates select="xhtml:li"/>
      </trickl:technorati-records>
   </xsl:template>

   <xsl:template match="xhtml:table[@class='blog-page']">
      <trickl:technorati-records>
         <trickl:technorati-record>
            <trickl:web-page-url><xsl:value-of select="descendant-or-self::xhtml:td[@class='site-details']/xhtml:a[@class='offsite']/attribute::href"/></trickl:web-page-url>
            <trickl:rating><xsl:value-of select="normalize-space(substring-after(descendant-or-self::xhtml:td[@class='site-details']/xhtml:strong, 'Technorati Authority:'))"/></trickl:rating>
         </trickl:technorati-record>
      </trickl:technorati-records>
   </xsl:template>
   
   <xsl:template match="xhtml:li">
      <trickl:technorati-record>
         <trickl:uri><xsl:value-of select="descendant-or-self::xhtml:td[@class='site-details']/xhtml:h3/xhtml:a/attribute::href"/></trickl:uri>
         <trickl:web-page-url><xsl:value-of select="descendant-or-self::xhtml:td[@class='site-details']/xhtml:a[@class='offsite']/attribute::href"/></trickl:web-page-url>
         <trickl:rating><xsl:value-of select="normalize-space(substring-after(descendant-or-self::xhtml:strong[@class='authority-count'], 'Auth:'))"/></trickl:rating>
      </trickl:technorati-record>
   </xsl:template>

</xsl:stylesheet>
