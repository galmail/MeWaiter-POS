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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {
    
    private String name;
    private String key;
    private String value;
    @JsonProperty("is_image")
    private boolean isImage;

    public Resource() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isIsImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }
    
    public void updateOnDB() {
        try {
            this.updateStatement();
        } catch (BasicException ex) {
            Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateStatement() throws BasicException {
        Session m_s = App.appView.getSession();
        String preparedSQL = "UPDATE RESOURCES SET NAME = ?, RESTYPE = ?, CONTENT = ? WHERE NAME = ?";
        int isImage = (this.isImage) ? 1 : 0;
        Object params = new Object[]{this.getKey(),isImage,this.getValue().getBytes(),this.getKey()};
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.INT,Datas.OBJECT,Datas.STRING}, new int[] {0,1,2,3});
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
