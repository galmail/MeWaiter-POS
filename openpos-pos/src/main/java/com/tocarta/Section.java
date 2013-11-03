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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    
    private int id;
    private String sid;
    private String name;
    private List<Subsection> subsections;
    private List<Dish> dishes;

    public Section() {
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

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Subsection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<Subsection> subsections) {
        this.subsections = subsections;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void insertDishesToDB() {
        try {
            if(this.getSubsections().isEmpty()){
                for(Dish dish : this.getDishes()){
                    Section.insertShortStatement(dish,this.getSid());
                    Section.setPositionStatement(new Object[]{dish.getSid(),dish.getPosition()});
                }
            }
            else {
                for(Subsection subsection : this.getSubsections()){
                    for(Dish dish : subsection.getDishes()){
                        Section.insertShortStatement(dish,subsection.getSid());
                        Section.setPositionStatement(new Object[]{dish.getSid(),dish.getPosition()});
                    }
                }
            }
        } catch (BasicException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void insertShortStatement(Dish dish, String parentSid) throws BasicException{
        String attributesSetId = null;
        double dishPrice = new Double(dish.getPrice()).doubleValue();
        Object params = new Object[]{dish.getSid(),dish.getSid(),dish.getSid(),dish.getName(),0.0,dishPrice,parentSid,"001",0,0};
        
        // insert the menu itself as a section and then insert the rest of its sections
        Session m_s = App.appView.getSession();
        String preparedSQL = "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ISCOM, ISSCALE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.STRING,Datas.INT,Datas.INT}, new int[]{0,1,2,3,4,5,6,7,8,9});
        PreparedSentence ps = new PreparedSentence(m_s, preparedSQL, serWriter, null);
        DataResultSet SRS = ps.openExec(params);
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.noupdatecount"));
        }
        else {
            int iResult = SRS.updateCount();
            SRS.close();
        }
    }
    
    public static void setPositionStatement(Object params) throws BasicException{
        Session m_s = App.appView.getSession();
        String preparedSQL = "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.INT}, new int[]{0,1});
        PreparedSentence ps = new PreparedSentence(m_s, preparedSQL, serWriter, null);
        DataResultSet SRS = ps.openExec(params);
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.noupdatecount"));
        }
        else {
            int iResult = SRS.updateCount();
            SRS.close();
        }
    }
    
    
    
    
    
}
