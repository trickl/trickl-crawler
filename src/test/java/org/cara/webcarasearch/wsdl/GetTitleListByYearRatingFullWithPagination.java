//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.27 at 07:48:18 PM GMT 
//


package org.cara.webcarasearch.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rating" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startRow" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="endRow" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "year",
    "rating",
    "startRow",
    "endRow"
})
@XmlRootElement(name = "GetTitleListByYearRatingFullWithPagination")
public class GetTitleListByYearRatingFullWithPagination {

    protected String year;
    protected String rating;
    protected int startRow;
    protected int endRow;

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYear(String value) {
        this.year = value;
    }

    /**
     * Gets the value of the rating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRating(String value) {
        this.rating = value;
    }

    /**
     * Gets the value of the startRow property.
     * 
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Sets the value of the startRow property.
     * 
     */
    public void setStartRow(int value) {
        this.startRow = value;
    }

    /**
     * Gets the value of the endRow property.
     * 
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * Sets the value of the endRow property.
     * 
     */
    public void setEndRow(int value) {
        this.endRow = value;
    }

}
