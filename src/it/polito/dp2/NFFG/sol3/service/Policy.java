
package it.polito.dp2.NFFG.sol3.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for policy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="policy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="verificationResult" type="{http://www.example.org/Neo4J}verificationResult" minOccurs="0"/>
 *         &lt;element name="traversedFunc" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="policy_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffg_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isPositive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="srcNode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dstNode" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policy", propOrder = {
    "verificationResult",
    "traversedFunc"
})
public class Policy {

    protected VerificationResult verificationResult;
    @XmlElement(nillable = true)
    protected List<String> traversedFunc;
    @XmlAttribute(name = "policy_name")
    protected String policyName;
    @XmlAttribute(name = "nffg_id", required = true)
    protected String nffgId;
    @XmlAttribute(name = "isPositive", required = true)
    protected boolean isPositive;
    @XmlAttribute(name = "srcNode", required = true)
    protected String srcNode;
    @XmlAttribute(name = "dstNode", required = true)
    protected String dstNode;

    /**
     * Gets the value of the verificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link VerificationResult }
     *     
     */
    public VerificationResult getVerificationResult() {
        return verificationResult;
    }

    /**
     * Sets the value of the verificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerificationResult }
     *     
     */
    public void setVerificationResult(VerificationResult value) {
        this.verificationResult = value;
    }

    /**
     * Gets the value of the traversedFunc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traversedFunc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraversedFunc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTraversedFunc() {
        if (traversedFunc == null) {
            traversedFunc = new ArrayList<String>();
        }
        return this.traversedFunc;
    }

    /**
     * Gets the value of the policyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * Sets the value of the policyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyName(String value) {
        this.policyName = value;
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
     * Gets the value of the isPositive property.
     * 
     */
    public boolean isIsPositive() {
        return isPositive;
    }

    /**
     * Sets the value of the isPositive property.
     * 
     */
    public void setIsPositive(boolean value) {
        this.isPositive = value;
    }

    /**
     * Gets the value of the srcNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcNode() {
        return srcNode;
    }

    /**
     * Sets the value of the srcNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcNode(String value) {
        this.srcNode = value;
    }

    /**
     * Gets the value of the dstNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDstNode() {
        return dstNode;
    }

    /**
     * Sets the value of the dstNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDstNode(String value) {
        this.dstNode = value;
    }

}
