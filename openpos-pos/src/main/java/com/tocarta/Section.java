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
                    Object params = new Object[]{dish.getSid(),dish.getSid(),dish.getSid(),dish.getName(),false,false,0.0,new Double(dish.getPrice()),this.getSid(),"001",null,null,null,null,true,null,null};
                    this.insertStatement(params);
                }
            }
            else {
                for(Subsection subsection : this.getSubsections()){
                    for(Dish dish : subsection.getDishes()){
                        Object params = new Object[]{dish.getSid(),dish.getSid(),dish.getSid(),dish.getName(),false,false,0.0,new Double(dish.getPrice()),subsection.getSid(),"001",null,null,null,null,true,null,null};
                        this.insertStatement(params);
                    }
                }
            }
        } catch (BasicException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertStatement(Object params) throws BasicException{
        // insert the menu itself as a section and then insert the rest of its sections
        Session m_s = App.appView.getSession();
        String preparedSQL = "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, CODETYPE, NAME, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, ATTRIBUTESET_ID, STOCKCOST, STOCKVOLUME, IMAGE, ISCOM, ISSCALE, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.IMAGE,Datas.INT,Datas.INT,Datas.NULL}, new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
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
