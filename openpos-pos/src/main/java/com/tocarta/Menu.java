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
public class Menu {
    
    private int id;
    private String sid;
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
    
    public void insertSectionsToDB(){
        try {
            Object params = new Object[]{this.getSid(),this.getName(),null,null};
            Menu.insertStatement(params);
            for(Section section : this.getSections()){
                params = new Object[]{section.getSid(),section.getName(),this.getSid(),null};
                Menu.insertStatement(params);
                if(!section.getSubsections().isEmpty()){
                    for(Subsection subsection : section.getSubsections()){
                        params = new Object[]{subsection.getSid(),subsection.getName(),section.getSid(),null};
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
        String preparedSQL = "insert into CATEGORIES (ID, NAME, PARENTID, IMAGE) values (?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.IMAGE}, new int[]{0,1,2,3});
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
