//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.27 at 07:48:18 PM GMT 
//


package org.cara.webcarasearch.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetTitleListByYearRatingFullWithPaginationResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getTitleListByYearRatingFullWithPaginationResult"
})
@XmlRootElement(name = "GetTitleListByYearRatingFullWithPaginationResponse")
public class GetTitleListByYearRatingFullWithPaginationResponse {

    @XmlElement(name = "GetTitleListByYearRatingFullWithPaginationResult")
    protected String getTitleListByYearRatingFullWithPaginationResult;

    /**
     * Gets the value of the getTitleListByYearRatingFullWithPaginationResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetTitleListByYearRatingFullWithPaginationResult() {
        return getTitleListByYearRatingFullWithPaginationResult;
    }

    /**
     * Sets the value of the getTitleListByYearRatingFullWithPaginationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetTitleListByYearRatingFullWithPaginationResult(String value) {
        this.getTitleListByYearRatingFullWithPaginationResult = value;
    }

}