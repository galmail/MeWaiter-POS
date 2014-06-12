/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class ShutdownServlet extends HttpServlet
{   
    public ShutdownServlet(){
    
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            resp = true;
        } catch (Exception ex) {
            Logger.getLogger(ShutdownServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ resp +" }");
        response.flushBuffer();
        System.exit(0);
    }
}