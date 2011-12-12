<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                xmlns:trickl="http://trickl.com">
   <xsl:output indent="yes"/>   
   <xsl:template match="/">
      <trickl:notifications>
         <xsl:apply-templates select="//table/tbody"/>
      </trickl:notifications>
   </xsl:template>
   <xsl:template match="tbody">
      <xsl:for-each select="tr[position()>1]">            
         <trickl:notification>
            <trickl:timestamp-format>dd/mm/yyyy HH:mm:ss</trickl:timestamp-format>
            <trickl:timestamp><xsl:value-of select="normalize-space(td[1]) "/><xsl:text> </xsl:text><xsl:value-of select="normalize-space(td[2])"/></trickl:timestamp>
            <trickl:message>$<xsl:value-of select="normalize-space(td[3])"/>:<xsl:value-of select="normalize-space(td[5])"/> (<xsl:value-of select="normalize-space(td[4])"/>) halted at <xsl:value-of select="normalize-space(td[2])"/>,<xsl:value-of select="normalize-space(td[1])"/>. Reason: <xsl:choose>
                  <xsl:when test="contains(td[6], 'T2')">News Released</xsl:when>
                  <xsl:when test="contains(td[6], 'T3')">Resumption Times</xsl:when>
                  <xsl:when test="contains(td[6], 'T5')">Stock Trading Pause In Effect</xsl:when>
                  <xsl:when test="contains(td[6], 'T6')">Extraordinary Market Activity</xsl:when>
                  <xsl:when test="contains(td[6], 'T7')">Stock Trading Pause/Quotation Only Period</xsl:when>
                  <xsl:when test="contains(td[6], 'T8')">Exchange-Traded-Fund (ETF)</xsl:when>
                  <xsl:when test="contains(td[6], 'T12')">Additional Information Requested By NASDAQ</xsl:when>
                  <xsl:when test="contains(td[6], 'H4')">Non-compliance</xsl:when>
                  <xsl:when test="contains(td[6], 'H9')">Not current</xsl:when>                     
                  <xsl:when test="contains(td[6], 'H10')">SEC Trading Suspension</xsl:when>                     
                  <xsl:when test="contains(td[6], 'H11')">Regulatory Concern</xsl:when>                     
               </xsl:choose>
            </trickl:message>
         </trickl:notification>        
      </xsl:for-each>
   </xsl:template>
</xsl:stylesheet>