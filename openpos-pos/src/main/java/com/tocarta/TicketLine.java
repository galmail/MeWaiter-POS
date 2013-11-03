/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
public class TicketLine implements Serializable {

    @JsonProperty("product_sid")
    private String productSid;
    @JsonProperty("category_sid")
    private String categorySid;
    @JsonProperty("product_name")
    private String productName;
    private double multiply;
    private double price;
    @JsonProperty("modifier_list_set")
    private ModifierListSet modifierListSet;
    private Discount discount;
    private String note;

    public TicketLine() {
    }

    public String getProductSid() {
        return productSid;
    }

    public void setProductSid(String productSid) {
        this.productSid = productSid;
    }

    public String getCategorySid() {
        return categorySid;
    }

    public void setCategorySid(String categorySid) {
        this.categorySid = categorySid;
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

    public ModifierListSet getModifierListSet() {
        return modifierListSet;
    }

    public void setModifierListSet(ModifierListSet modifierListSet) {
        this.modifierListSet = modifierListSet;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
    public double calculatePrice(){
        double fixedDiscount = discount.calculateFixDiscount(price);
        return price + fixedDiscount;
    }

    public String printAllModifiers() {
        String result = "";
        String separator = ",";
        if (this.getModifierListSet() != null) {
            for (ModifierList mList : this.getModifierListSet().getModifierLists()) {
                for (Modifier modif : mList.getSelectedModifiers()) {
                    result += modif.getName() + separator;
                }
            }
        }
        if (this.note != null) {
            result += this.note;
        }
        if (result.endsWith(separator)) {
            result = result.substring(0, result.length() - separator.length());
        }
        return result;
    }

    public List<Modifier> getAllModifiers() {
        List<Modifier> modifs = new ArrayList<Modifier>();
        if (this.getModifierListSet() != null) {
            for (ModifierList mList : this.getModifierListSet().getModifierLists()) {
                for (Modifier modif : mList.getSelectedModifiers()) {
                    modifs.add(modif);
                }
            }
        }
        if (this.note != null) {
            modifs.add(new Modifier(this.note));
        }
        return modifs;
    }
}
