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
import com.openbravo.pos.forms.AppConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class ShowConfigServlet extends HttpServlet
{   
    private AppConfig config = null;
    
    public ShowConfigServlet(AppConfig conf){
        config = conf;
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            JFrmConfig configWindow = new JFrmConfig(config);
            configWindow.setVisible(true);
            configWindow.setAlwaysOnTop(true);
            resp = true;
        } catch (Exception ex) {
            Logger.getLogger(ShowConfigServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ resp +" }");
    }
}