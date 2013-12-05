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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Floor {
    
    private int id;
    @JsonProperty("printer_id")
    private int printerId;
    private String sid;
    private String name;
    private List<Table> tables;

    public Floor() {
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

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public int getPrinterId() {
        return printerId;
    }

    public void setPrinterId(int printerId) {
        this.printerId = printerId;
    }
    
    public void insertTablestoDB() {
        try {
            BufferedImage in = ImageIO.read(new URL("http://media-cache-ak0.pinimg.com/originals/99/28/c2/9928c234242bd399de157b4fc3d4761d.jpg"));
            Object params = new Object[]{this.getSid(),this.getName(),in, this.getPrinterId()};
            this.insertFloorStatement(params);
            int counter = 1;
            for(Table table : this.getTables()){
                params = new Object[]{table.getSid(),table.getName(this.getName()),table.getPositionX(counter),table.getPositionY(counter),this.getSid()};
                this.insertTableStatement(params);
                counter++;
            }
        } catch (BasicException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Floor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void insertFloorStatement(Object params) throws BasicException{
        // insert the menu itself as a section and then insert the rest of its sections
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into FLOORS (ID, NAME, IMAGE, PRINTERID) values (?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.IMAGE,Datas.INT}, new int[]{0,1,2,3});
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
    
    private void insertTableStatement(Object params) throws BasicException{
        // insert the menu itself as a section and then insert the rest of its sections
        Session m_s = App.appView.getSession();
        String preparedSQL = "insert into PLACES (ID, NAME, X, Y, FLOOR) values (?, ?, ?, ?, ?)";
        SerializerWriteBasicExt serWriter = new SerializerWriteBasicExt(new Datas[]{Datas.STRING,Datas.STRING,Datas.INT,Datas.INT,Datas.STRING}, new int[]{0,1,2,3,4});
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
