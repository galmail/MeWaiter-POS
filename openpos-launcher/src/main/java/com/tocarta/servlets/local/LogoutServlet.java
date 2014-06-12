/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.openbravo.pos.util.AltEncrypter;
import com.tocarta.services.Setup;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
public class LogoutServlet extends HttpServlet
{   
    public LogoutServlet(){
    
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // RESET EMAIL/PASSWD IN CONFIG FILE
        Setup.config.setProperty("mw.email", "");
        Setup.config.setProperty("mw.password", "");
        Setup.config.save();
        response.sendRedirect("/");
    }
}