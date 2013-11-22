/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets;

/**
 *
 * @author gal
 */

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.ticket.TicketInfo;
import com.tocarta.App;
import com.tocarta.Ticket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
 
public class TableServlet extends HttpServlet
{   
    public TableServlet(){
    
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        String tSid = request.getParameter("table");
        try {
            // 1. Open or Close the table
            AppView m_App = App.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            TicketInfo ticket = dlReceipts.getSharedTicket(tSid);
            if(ticket!=null) resp = true;
        }
        catch(BasicException ex){
            Logger.getLogger(TableServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 2. Send result
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"opened\": "+ resp +" }");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean resp = false;
        try {
            // 1. Get received params from request
            String tSid = request.getParameter("table");
            String tPax = request.getParameter("pax");
            String tReservation = request.getParameter("reservation");
            String tMethod = request.getParameter("method");
            // 2. Open or Close the table
            AppView m_App = App.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            if(tMethod!=null && tMethod.equals("open")){
                TicketInfo ticket = new TicketInfo();
                if(dlReceipts.getSharedTicket(tSid)==null){
                    dlReceipts.insertSharedTicket(tSid, ticket);
                }
            }
            else if(tMethod!=null && tMethod.equals("close")){
                dlReceipts.deleteSharedTicket(tSid);
            }
            resp = true;
        } catch (BasicException ex) {
            Logger.getLogger(TableServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 3. Send result OK
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ resp +" }");
    }
}