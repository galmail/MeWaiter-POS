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
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Modifier implements Serializable {
    
    private int id;
    private int position;
    private String sid;
    private String name;

    public Modifier() {
    }
    
    public Modifier(String name) {
        this.sid = "";
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
    
    public void insertAttrValue(String parentSid) throws BasicException {
        // Insert attributes_values / modifiers
        Object params = new Object[]{this.getSid(),parentSid,this.getName()};
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into ATTRIBUTEVALUE (ID, ATTRIBUTE_ID, VALUE) values (?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING}, new int[]{0,1,2});
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
