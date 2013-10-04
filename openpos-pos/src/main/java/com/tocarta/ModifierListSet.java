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
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifierListSet {
    
    private int id;
    private String sid;
    private String name;
    @JsonProperty("modifier_lists")
    private List<ModifierList> modifierLists;
    private List<Dish> dishes;

    public ModifierListSet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<ModifierList> getModifierLists() {
        return modifierLists;
    }

    public void setModifierList(List<ModifierList> modifierLists) {
        this.modifierLists = modifierLists;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
    
    public void insertModifiersToDB() {
        try {
            // Insert attributes_sets / modifier_list_sets
            this.insertAttrSet();
            // Insert attributes / modifier_lists
            for(ModifierList modList : this.getModifierLists()){
                modList.insertAttr();
                modList.insertAttrsRelation(this.getSid());
                for(Modifier modifier : modList.getModifiers()){
                    modifier.insertAttrValue(modList.getSid());
                }
            }
            // update all dishes to get modifiers
            for(Dish dish : this.getDishes()){
                dish.updateAttributes(this.getSid());
            }
        } catch (BasicException ex) {
            Logger.getLogger(ModifierListSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertAttrSet() throws BasicException{
        Object params = new Object[]{this.getSid(),this.getName()};
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into ATTRIBUTESET (ID, NAME) values (?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING}, new int[]{0,1});
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
