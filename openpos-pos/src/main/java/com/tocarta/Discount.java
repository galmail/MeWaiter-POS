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
    private static String discountsCategoryId;
    private static final String discountsCategoryName = "Descuentos";

    public Discount() {
    }
    
    public static String getCategorySid(){
        return discountsCategoryId;
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
    
    public static void insertDiscountCategory() throws BasicException {
        discountsCategoryId = UUID.randomUUID().toString();
        Object params = new Object[]{discountsCategoryId,discountsCategoryName,null,null};
        Menu.insertStatement(params);
    }

    public void insertOnDB() throws BasicException {
        Dish d = new Dish();
        d.setSid(sid);
        d.setPosition(position);
        d.setName(name);
        d.setDescription(note);
        d.setPrice(new Double(amount).toString());
        Section.insertShortStatement(d,discountsCategoryId);
        Section.setPositionStatement(new Object[]{sid,position});
    }
    
}
