/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.v1;

/**
 *
 * @author gal
 */

import com.openbravo.pos.forms.AppLocal;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class HelloServlet extends HttpServlet
{   
    public HelloServlet(){
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>meWaiter Status: "+ AppLocal.APP_STATUS +"</h1>");
        
        // show config window button
        response.getWriter().println("<form method=\"post\" action=\"/local/show_config\"><input type=\"submit\" value=\"Open Config Window\" /></form>");
        
        // show close app button
        response.getWriter().println("<form method=\"post\" action=\"/local/shutdown\"><input type=\"submit\" value=\"Shutdown App\" /></form>");
        
    }
    
}