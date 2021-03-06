/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import com.openbravo.basic.BasicException;
import java.util.UUID;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discount {
    
    private String sid;
    private int position;
    private String name;
    private String note;
    private String dtype;
    private double amount;
    private String categorySid;
    private static final String discountsCategoryName = "Descuentos";

    public Discount() {
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCategorySid() {
        return categorySid;
    }

    public void setCategorySid(String categorySid) {
        this.categorySid = categorySid;
    }
    
    public double calculateFixDiscount(double total){
        if(dtype.equals("fixed")){
            return amount*(-1);
        }
        else if (dtype.equals("percentage")){
            return total*(amount/100)*(-1);
        }
        else {
            return 0;
        }
    }
    
    public static String insertDiscountCategory() throws BasicException {
        String discountsCategoryId = UUID.randomUUID().toString();
        Object params = new Object[]{discountsCategoryId,discountsCategoryName,null,null,1};
        Menu.insertStatement(params);
        return discountsCategoryId;
    }

    public void insertOnDB(String categoryId) throws BasicException {
        Dish d = new Dish();
        d.setSid(sid);
        d.setPosition(position);
        d.setName(name);
        d.setDescription(note);
        d.setPrice(new Double(amount).toString());
        Section.insertShortStatement(d,categoryId);
        Section.setPositionStatement(new Object[]{sid,position});
    }
    
}
