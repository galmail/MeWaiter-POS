/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.openbravo.pos.config.JFrmConfig;
import com.openbravo.pos.config.JPanelConfiguration;
import com.openbravo.pos.panels.JPanelPrinter;
import com.tocarta.App;
import com.tocarta.services.Setup;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
 
public class ShowPrinterServlet extends HttpServlet
{   
    
    public ShowPrinterServlet(){
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            JFrame printerWindow = new JFrame();
            JPanelPrinter panelPrinter = new JPanelPrinter(App.appView);
            printerWindow.getContentPane().add(panelPrinter, BorderLayout.CENTER);
            printerWindow.setBounds(300, 100, 500, 500);
            panelPrinter.activate();
            printerWindow.setVisible(true);
            printerWindow.setAlwaysOnTop(true);
            resp = true;
        } catch (Exception ex) {
            Logger.getLogger(ShowPrinterServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ resp +" }");
    }
}