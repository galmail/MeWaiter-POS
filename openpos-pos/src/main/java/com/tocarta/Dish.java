/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataResultSet;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.Session;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dish {
    
    private int id;
    private String sid;
    private String name;
    private int position;
    private String price;
    @JsonProperty("short_title")
    private String shortTitle;
    private String description;
    @JsonProperty("tax_included")
    private boolean taxIncluded;
    private double standardTaxRate;

    public Dish() {
        taxIncluded = true;
        standardTaxRate = 0.10;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        try {
            if(this.taxIncluded){
                double dPrice = Double.parseDouble(price);
                String priceWithoutTax = new Double(dPrice / (1+this.standardTaxRate)).toString();
                this.price = priceWithoutTax;
            }
            else {
                this.price = price;
            }
        } catch (Exception ex){
            this.price = "0";
        }
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }

    void updateAttributes(String attrSetId) throws BasicException {
        // UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAXCAT = ?, ATTRIBUTESET_ID = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ? WHERE ID = ?
        Object params = new Object[]{attrSetId,this.getSid()};
        Session m_s = App.appView.getSession();
        String preparedSQL = "UPDATE PRODUCTS SET ATTRIBUTESET_ID = ? WHERE ID = ?";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING}, new int[]{0,1});
        new PreparedSentence(m_s, preparedSQL, serWriter).exec(params);
    }
    
}
