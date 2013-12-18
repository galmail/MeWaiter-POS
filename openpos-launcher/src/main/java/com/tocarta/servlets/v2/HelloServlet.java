/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.v2;

/**
 *
 * @author gal
 */

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
        response.getWriter().println("<h1>MeWaiter POS v2 is up and running!</h1>");
        //response.getWriter().println("session=" + request.getSession(true).getId());
    }
}