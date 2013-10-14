/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.DataResultSet;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gal
 */
public class App {
    
    public static AppView appView = null;
    public static int attrCounter = 0;
    public static JPaymentSelect jpaymentView = null;

    public App() {
    }
    
    public static void cleanDB() {
        deleteTable("PRODUCTS_CAT");
        deleteTable("PRODUCTS");
        deleteTable("CATEGORIES");
        
        deleteTable("ATTRIBUTEVALUE");
        deleteTable("ATTRIBUTEUSE");
        deleteTable("ATTRIBUTEINSTANCE");
        deleteTable("ATTRIBUTE");
        deleteTable("ATTRIBUTESETINSTANCE");
        deleteTable("ATTRIBUTESET");
        
        deleteTable("PLACES");
        deleteTable("FLOORS");    
    }
    
    private static void deleteTable(String table){
        try {
            Session m_s = appView.getSession();
            new PreparedSentence(m_s, "DELETE FROM "+table, new SerializerWriteBasicExt(null, new int[]{})).exec(null);            
        }
        catch (BasicException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void printTicket(String sresourcename, TicketInfo ticket, Object ticketext) throws BasicException {
        AppView m_App = appView;
        DataLogicSystem dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        DataLogicSales dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        SentenceList senttax = dlSales.getTaxList();
        java.util.List<TaxInfo> taxlist = senttax.list();
        ListKeyed taxcollection = new ListKeyed<TaxInfo>(taxlist);
        TaxesLogic taxeslogic = new TaxesLogic(taxlist);
        TicketParser m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        String sresource = dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            System.out.println("ERROR: sresource = null");
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                System.out.println("ERROR: ScriptException = " + e.toString());
            } catch (TicketPrinterException e) {
                System.out.println("ERROR: TicketPrinterException = " + e.toString());
            }
        }
    }
    
    
}
