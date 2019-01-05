
package it.polito.dp2.NFFG.sol3.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for nffg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nffg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="node" type="{http://www.example.org/Neo4J}Mynode" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="nffg_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastUpadateTime" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nffg", propOrder = {
    "node"
})
public class Nffg {

    @XmlElement(required = true)
    protected List<Mynode> node;
    @XmlAttribute(name = "nffg_id", required = true)
    protected String nffgId;
    @XmlAttribute(name = "lastUpadateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpadateTime;

    /**
     * Gets the value of the node property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the node property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mynode }
     * 
     * 
     */
    public List<Mynode> getNode() {
        if (node == null) {
            node = new ArrayList<Mynode>();
        }
        return this.node;
    }

    /**
     * Gets the value of the nffgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffgId() {
        return nffgId;
    }

    /**
     * Sets the value of the nffgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffgId(String value) {
        this.nffgId = value;
    }

    /**
     * Gets the value of the lastUpadateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpadateTime() {
        return lastUpadateTime;
    }

    /**
     * Sets the value of the lastUpadateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpadateTime(XMLGregorianCalendar value) {
        this.lastUpadateTime = value;
    }

}
