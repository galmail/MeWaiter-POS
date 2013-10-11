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
import com.openbravo.pos.forms.AppView;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gal
 */
public class App {
    
    public static AppView appView = null;
    public static int attrCounter = 0;

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
    
    
}
