/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.util.AltEncrypter;
import com.tocarta.services.Login;
import com.tocarta.services.Setup;
import com.tocarta.services.TestConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
public class LoginServlet extends HttpServlet
{   
    public LoginServlet(){
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            String baseUrl = Setup.config.getProperty("mw.url");
            String email = request.getParameter("email");
            String passwd = request.getParameter("password");
            
            if(TestConnection.echo()){
                if(Login.login(email, passwd)){
                    resp = true;
                    // SAVE EMAIL/PASSWD IN CONFIG FILE
                    Setup.config.setProperty("mw.email", email);
                    AltEncrypter cypher = new AltEncrypter("cypherkey" + passwd);
                    Setup.config.setProperty("mw.password", "crypt:" + cypher.encrypt(passwd));
                    Setup.config.save();
                }
                else {
                    Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, null, "Failed to authenticate user: "+email);
                    resp = false;
                }
            }
            else {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, null, "Cannot connect to server: "+baseUrl);
                resp = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.sendRedirect("/");
    }
}