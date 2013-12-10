/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tocarta;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.data.loader.SerializerWriteBuilder;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author gal
 */
public class Sale implements SerializableWrite, SerializableRead, Serializable {
    
    private String ID;
    private String RECEIPTS_ID;
    private String RECEIPTS_MONEY;
    private Date RECEIPTS_DATENEW;
    private Properties RECEIPTS_ATTRIBUTES;
    
    private String TICKETS_ID;
    private int TICKETS_TICKETTYPE;
    private int TICKETS_TICKETID;
    private String TICKETS_PERSON;
    private String TICKETS_CUSTOMER;
    private int TICKETS_STATUS;
    private int TICKETS_DINERS;
    
    private String TICKETLINES_TICKET;
    private int TICKETLINES_LINE;
    private String TICKETLINES_PRODUCT;
    private String TICKETLINES_ATTRIBUTESETINSTANCE_ID;
    private double TICKETLINES_UNITS;
    private double TICKETLINES_PRICE;
    private String TICKETLINES_TAXID;
    private Properties TICKETLINES_ATTRIBUTES;
    
    private String PAYMENTS_ID;
    private String PAYMENTS_RECEIPT;
    private String PAYMENTS_PAYMENT;
    private String PAYMENTS_NOTE;
    private double PAYMENTS_TOTAL;
    private String PAYMENTS_TRANSID;
    private String PAYMENTS_RETURNMSG;
    
    private String TAXLINES_ID;
    private String TAXLINES_RECEIPT;
    private String TAXLINES_TAXID;
    private double TAXLINES_BASE;
    private double TAXLINES_AMOUNT;
    
    public Sale(){
        ID = UUID.randomUUID().toString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRECEIPTS_ID() {
        return RECEIPTS_ID;
    }

    public void setRECEIPTS_ID(String RECEIPTS_ID) {
        this.RECEIPTS_ID = RECEIPTS_ID;
    }

    public String getRECEIPTS_MONEY() {
        return RECEIPTS_MONEY;
    }

    public void setRECEIPTS_MONEY(String RECEIPTS_MONEY) {
        this.RECEIPTS_MONEY = RECEIPTS_MONEY;
    }

    public Date getRECEIPTS_DATENEW() {
        return RECEIPTS_DATENEW;
    }

    public void setRECEIPTS_DATENEW(Date RECEIPTS_DATENEW) {
        this.RECEIPTS_DATENEW = RECEIPTS_DATENEW;
    }

    public Properties getRECEIPTS_ATTRIBUTES() {
        return RECEIPTS_ATTRIBUTES;
    }

    public void setRECEIPTS_ATTRIBUTES(Properties RECEIPTS_ATTRIBUTES) {
        this.RECEIPTS_ATTRIBUTES = RECEIPTS_ATTRIBUTES;
    }

    public String getTICKETS_ID() {
        return TICKETS_ID;
    }

    public void setTICKETS_ID(String TICKETS_ID) {
        this.TICKETS_ID = TICKETS_ID;
    }

    public int getTICKETS_TICKETTYPE() {
        return TICKETS_TICKETTYPE;
    }

    public void setTICKETS_TICKETTYPE(int TICKETS_TICKETTYPE) {
        this.TICKETS_TICKETTYPE = TICKETS_TICKETTYPE;
    }

    public int getTICKETS_TICKETID() {
        return TICKETS_TICKETID;
    }

    public void setTICKETS_TICKETID(int TICKETS_TICKETID) {
        this.TICKETS_TICKETID = TICKETS_TICKETID;
    }

    public String getTICKETS_PERSON() {
        return TICKETS_PERSON;
    }

    public void setTICKETS_PERSON(String TICKETS_PERSON) {
        this.TICKETS_PERSON = TICKETS_PERSON;
    }

    public String getTICKETS_CUSTOMER() {
        return TICKETS_CUSTOMER;
    }

    public void setTICKETS_CUSTOMER(String TICKETS_CUSTOMER) {
        this.TICKETS_CUSTOMER = TICKETS_CUSTOMER;
    }

    public int getTICKETS_STATUS() {
        return TICKETS_STATUS;
    }

    public void setTICKETS_STATUS(int TICKETS_STATUS) {
        this.TICKETS_STATUS = TICKETS_STATUS;
    }

    public int getTICKETS_DINERS() {
        return TICKETS_DINERS;
    }

    public void setTICKETS_DINERS(int TICKETS_DINERS) {
        this.TICKETS_DINERS = TICKETS_DINERS;
    }

    public String getTICKETLINES_TICKET() {
        return TICKETLINES_TICKET;
    }

    public void setTICKETLINES_TICKET(String TICKETLINES_TICKET) {
        this.TICKETLINES_TICKET = TICKETLINES_TICKET;
    }

    public int getTICKETLINES_LINE() {
        return TICKETLINES_LINE;
    }

    public void setTICKETLINES_LINE(int TICKETLINES_LINE) {
        this.TICKETLINES_LINE = TICKETLINES_LINE;
    }

    public String getTICKETLINES_PRODUCT() {
        return TICKETLINES_PRODUCT;
    }

    public void setTICKETLINES_PRODUCT(String TICKETLINES_PRODUCT) {
        this.TICKETLINES_PRODUCT = TICKETLINES_PRODUCT;
    }

    public String getTICKETLINES_ATTRIBUTESETINSTANCE_ID() {
        return TICKETLINES_ATTRIBUTESETINSTANCE_ID;
    }

    public void setTICKETLINES_ATTRIBUTESETINSTANCE_ID(String TICKETLINES_ATTRIBUTESETINSTANCE_ID) {
        this.TICKETLINES_ATTRIBUTESETINSTANCE_ID = TICKETLINES_ATTRIBUTESETINSTANCE_ID;
    }

    public double getTICKETLINES_UNITS() {
        return TICKETLINES_UNITS;
    }

    public void setTICKETLINES_UNITS(double TICKETLINES_UNITS) {
        this.TICKETLINES_UNITS = TICKETLINES_UNITS;
    }

    public double getTICKETLINES_PRICE() {
        return TICKETLINES_PRICE;
    }

    public void setTICKETLINES_PRICE(double TICKETLINES_PRICE) {
        this.TICKETLINES_PRICE = TICKETLINES_PRICE;
    }

    public String getTICKETLINES_TAXID() {
        return TICKETLINES_TAXID;
    }

    public void setTICKETLINES_TAXID(String TICKETLINES_TAXID) {
        this.TICKETLINES_TAXID = TICKETLINES_TAXID;
    }

    public Properties getTICKETLINES_ATTRIBUTES() {
        return TICKETLINES_ATTRIBUTES;
    }

    public void setTICKETLINES_ATTRIBUTES(Properties TICKETLINES_ATTRIBUTES) {
        this.TICKETLINES_ATTRIBUTES = TICKETLINES_ATTRIBUTES;
    }

    public String getPAYMENTS_ID() {
        return PAYMENTS_ID;
    }

    public void setPAYMENTS_ID(String PAYMENTS_ID) {
        this.PAYMENTS_ID = PAYMENTS_ID;
    }

    public String getPAYMENTS_RECEIPT() {
        return PAYMENTS_RECEIPT;
    }

    public void setPAYMENTS_RECEIPT(String PAYMENTS_RECEIPT) {
        this.PAYMENTS_RECEIPT = PAYMENTS_RECEIPT;
    }

    public String getPAYMENTS_PAYMENT() {
        return PAYMENTS_PAYMENT;
    }

    public void setPAYMENTS_PAYMENT(String PAYMENTS_PAYMENT) {
        this.PAYMENTS_PAYMENT = PAYMENTS_PAYMENT;
    }

    public String getPAYMENTS_NOTE() {
        return PAYMENTS_NOTE;
    }

    public void setPAYMENTS_NOTE(String PAYMENTS_NOTE) {
        this.PAYMENTS_NOTE = PAYMENTS_NOTE;
    }

    public double getPAYMENTS_TOTAL() {
        return PAYMENTS_TOTAL;
    }

    public void setPAYMENTS_TOTAL(double PAYMENTS_TOTAL) {
        this.PAYMENTS_TOTAL = PAYMENTS_TOTAL;
    }

    public String getPAYMENTS_TRANSID() {
        return PAYMENTS_TRANSID;
    }

    public void setPAYMENTS_TRANSID(String PAYMENTS_TRANSID) {
        this.PAYMENTS_TRANSID = PAYMENTS_TRANSID;
    }

    public String getPAYMENTS_RETURNMSG() {
        return PAYMENTS_RETURNMSG;
    }

    public void setPAYMENTS_RETURNMSG(String PAYMENTS_RETURNMSG) {
        this.PAYMENTS_RETURNMSG = PAYMENTS_RETURNMSG;
    }

    public String getTAXLINES_ID() {
        return TAXLINES_ID;
    }

    public void setTAXLINES_ID(String TAXLINES_ID) {
        this.TAXLINES_ID = TAXLINES_ID;
    }

    public String getTAXLINES_RECEIPT() {
        return TAXLINES_RECEIPT;
    }

    public void setTAXLINES_RECEIPT(String TAXLINES_RECEIPT) {
        this.TAXLINES_RECEIPT = TAXLINES_RECEIPT;
    }

    public String getTAXLINES_TAXID() {
        return TAXLINES_TAXID;
    }

    public void setTAXLINES_TAXID(String TAXLINES_TAXID) {
        this.TAXLINES_TAXID = TAXLINES_TAXID;
    }

    public double getTAXLINES_BASE() {
        return TAXLINES_BASE;
    }

    public void setTAXLINES_BASE(double TAXLINES_BASE) {
        this.TAXLINES_BASE = TAXLINES_BASE;
    }

    public double getTAXLINES_AMOUNT() {
        return TAXLINES_AMOUNT;
    }

    public void setTAXLINES_AMOUNT(double TAXLINES_AMOUNT) {
        this.TAXLINES_AMOUNT = TAXLINES_AMOUNT;
    }
    
    
    

    @Override
    public void writeValues(DataWrite dw) throws BasicException {
        int counter = 1;
        
        dw.setString(counter++, ID);
        
        // RECEIPTS
        dw.setString(counter++, RECEIPTS_ID);
        dw.setString(counter++, RECEIPTS_MONEY);
        dw.setTimestamp(counter++, RECEIPTS_DATENEW);
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            RECEIPTS_ATTRIBUTES.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dw.setBytes(counter++, o.toByteArray());
        } catch (IOException e) {
            dw.setBytes(counter++, null);
        }
        
        // TICKETS
        dw.setString(counter++, TICKETS_ID);
        dw.setInt(counter++, TICKETS_TICKETTYPE);
        dw.setInt(counter++, TICKETS_TICKETID);
        dw.setString(counter++, TICKETS_PERSON);
        dw.setString(counter++, TICKETS_CUSTOMER);
        dw.setInt(counter++, TICKETS_STATUS);
        dw.setInt(counter++, TICKETS_DINERS);
        
        // TICKETLINES
        dw.setString(counter++, TICKETLINES_TICKET);
        dw.setInt(counter++, TICKETLINES_LINE);
        dw.setString(counter++, TICKETLINES_PRODUCT);
        dw.setString(counter++, TICKETLINES_ATTRIBUTESETINSTANCE_ID);
        dw.setDouble(counter++, TICKETLINES_UNITS);
        dw.setDouble(counter++, TICKETLINES_PRICE);
        dw.setString(counter++, TICKETLINES_TAXID);
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            TICKETLINES_ATTRIBUTES.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dw.setBytes(counter++, o.toByteArray());
        } catch (IOException e) {
            dw.setBytes(counter++, null);
        } catch (NullPointerException e) {
            dw.setBytes(counter++, null);
        }
        
        // PAYMENTS
        dw.setString(counter++, PAYMENTS_ID);
        dw.setString(counter++, PAYMENTS_RECEIPT);
        dw.setString(counter++, PAYMENTS_PAYMENT);
        dw.setString(counter++, PAYMENTS_NOTE);
        dw.setDouble(counter++, PAYMENTS_TOTAL);
        dw.setString(counter++, PAYMENTS_TRANSID);
        dw.setBytes(counter++, (byte[]) Formats.BYTEA.parseValue(PAYMENTS_RETURNMSG));
        
        
        // TAXLINES
        dw.setString(counter++, TAXLINES_ID);
        dw.setString(counter++, TAXLINES_RECEIPT);
        dw.setString(counter++, TAXLINES_TAXID);
        dw.setDouble(counter++, TAXLINES_BASE);
        dw.setDouble(counter++, TAXLINES_AMOUNT);
        
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void recreateID(){
        ID = UUID.randomUUID().toString();
    }

    public void clearTicketLine() {
        setTICKETLINES_TICKET(null);
        setTICKETLINES_LINE(0);
        setTICKETLINES_PRODUCT(null);
        setTICKETLINES_ATTRIBUTESETINSTANCE_ID(null);
        setTICKETLINES_UNITS(0);
        setTICKETLINES_PRICE(0);
        setTICKETLINES_TAXID(null);
        setTICKETLINES_ATTRIBUTES(null);
    }

    public void clearTaxLine() {
        setTAXLINES_ID(null);
        setTAXLINES_RECEIPT(null);
        setTAXLINES_TAXID(null);
        setTAXLINES_BASE(0);
        setTAXLINES_AMOUNT(0);
    }
    
}
