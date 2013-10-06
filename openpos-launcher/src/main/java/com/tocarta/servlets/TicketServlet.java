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
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.ticket.TicketInfo;
import com.tocarta.App;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
 
public class TicketServlet extends HttpServlet
{
    public TicketServlet(){}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            // get table number
            String ticketId = request.getParameter("table");
            DataLogicReceipts dlReceipts = new DataLogicReceipts();
            dlReceipts.init(App.appView.getSession());
            TicketInfo ticket = dlReceipts.getSharedTicket(ticketId);
            if(ticket!=null){
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                // convert TicketInfo to ticket and return ticket
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(ticket);
                response.getWriter().print(json);
            }
        } catch (BasicException ex) {
            Logger.getLogger(TicketServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
