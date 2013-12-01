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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {
    
    public static String SID = null;
    private int id;
    private String sid;
    @JsonProperty("printer_id")
    private int printerId;
    private String name;
    private String price;
    private List<Section> sections;

    public Menu() {
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
        this.price = price;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getPrinterId() {
        return printerId;
    }

    public void setPrinterId(int printerId) {
        this.printerId = printerId;
    }
    
    private void createMenusInDB(){
        try {
            SID = UUID.randomUUID().toString();
            Object params = new Object[]{SID,"Menus",null,null,this.getPrinterId()};
            Menu.insertStatement(params);
        } catch (BasicException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createMenuObjectInDB(){
        try {
            if(this.getPrice()!=null && new Double(this.getPrice()).doubleValue()>0){
                // insert menu in menus
                Dish dish = new Dish();
                //dish.setId(this.getId());
                dish.setName(this.getName());
                //dish.setPosition(1);
                dish.setPrice(this.getPrice());
                dish.setSid(this.getSid());
                Section.insertShortStatement(dish,SID);
                //Section.setPositionStatement(new Object[]{dish.getSid(),dish.getPosition()});
            }
        } catch(Exception ex){
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertSectionsToDB(){
        if(SID==null) createMenusInDB();
        createMenuObjectInDB();
        try {
            Object params = new Object[]{this.getSid(),this.getName(),null,null,this.getPrinterId()};
            Menu.insertStatement(params);
            for(Section section : this.getSections()){
                params = new Object[]{section.getSid(),section.getName(),this.getSid(),null,section.getPrinterId()};
                Menu.insertStatement(params);
                if(!section.getSubsections().isEmpty()){
                    for(Subsection subsection : section.getSubsections()){
                        params = new Object[]{subsection.getSid(),subsection.getName(),section.getSid(),null,subsection.getPrinterId()};
                        Menu.insertStatement(params);
                    }
                }
            }
        } catch (BasicException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void insertStatement(Object params) throws BasicException{
        // insert the menu itself as a section and then insert the rest of its sections
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into CATEGORIES (ID, NAME, PARENTID, IMAGE, PRINTERID) values (?, ?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.INT}, new int[]{0,1,2,3,4});
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
