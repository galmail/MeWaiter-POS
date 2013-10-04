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
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ModifierList {
    
    private int id;
    private int position;
    private String sid;
    private String name;
    private List<Modifier> modifiers;

    public ModifierList() {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }    
    
    public void insertAttr() throws BasicException {
        Object params = new Object[]{this.getSid(),this.getName()};
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into ATTRIBUTE (ID, NAME) values (?, ?)";
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
    
    public void insertAttrsRelation(String parentSid) throws BasicException {
        Object params = new Object[]{UUID.randomUUID().toString(),parentSid,this.getSid(),App.attrCounter++};
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into ATTRIBUTEUSE (ID, ATTRIBUTESET_ID, ATTRIBUTE_ID, LINENO) values (?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.INT}, new int[]{0,1,2,3});
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
