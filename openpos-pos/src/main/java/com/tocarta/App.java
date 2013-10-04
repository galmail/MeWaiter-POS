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
        try {
            Session m_s = appView.getSession();
            new PreparedSentence(m_s, "DELETE FROM PRODUCTS_CAT", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM PRODUCTS", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM CATEGORIES", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTEVALUE", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTEUSE", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTEINSTANCE", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTE", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTESETINSTANCE", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            new PreparedSentence(m_s, "DELETE FROM ATTRIBUTESET", new SerializerWriteBasicExt(null, new int[]{})).exec(null);
            
        } catch (BasicException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
