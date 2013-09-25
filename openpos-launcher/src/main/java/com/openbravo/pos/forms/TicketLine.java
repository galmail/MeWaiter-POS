/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author gal
 */
public class TicketLine implements Serializable {
    
    private String productId;
    private List<Modifier> modifiers;
    private String note;
    private String categoryid;
    private String productName;
    private double multiply;
    private double price;
    
    public TicketLine() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double multiply) {
        this.multiply = multiply;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String printAllModifiers(){
        String result = "";
        for(Modifier modifier : this.modifiers){
            if(modifier.getValue()!=null)
                result += modifier.getValue() + ",";
        }
        if(this.note!=null)
            result += this.note;
        if(result.endsWith(",")){
            result = result.substring(0,result.length()-1);
        }
        return result;
    }
    
    public boolean hasModifiers(){
        return (!this.modifiers.isEmpty() || this.note!=null);
    }
    
}
