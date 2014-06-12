/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

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
 
public class MainServlet extends HttpServlet
{   
    public MainServlet(){
    
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean loggedIn = false;
        String email = Setup.config.getProperty("mw.email");
        String passwd = Setup.config.getProperty("mw.password");
        if(email!=null && passwd!=null){
            if(email.length()>0 && passwd.length()>0){
                loggedIn = true;
            }
        }
        
        if(loggedIn){
            response.sendRedirect("/web");
        }
        else {
            response.sendRedirect("/login");
        }
    }
}