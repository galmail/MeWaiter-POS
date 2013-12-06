/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.actions;

/**
 *
 * @author gal
 */

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.tocarta.App;
import com.tocarta.Ticket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
 
public class SecondCoursesServlet extends HttpServlet
{   
    public SecondCoursesServlet(){
    
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sresource = "Printer.TicketSimpleAction";
        boolean resp = false;
        try {
            String tSid = request.getParameter("table_sid");
            String tName = request.getParameter("table_name");
            
            AppView m_App = App.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            TicketInfo ticket = dlReceipts.getSharedTicket(tSid);
            
            if(ticket!=null){
                // get main course and set the printer
                DataLogicSales dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
                List<CategoryInfo> cats = dlSales.getMainCourseCategories();
                int printerId = 1; // default printer
                if(!cats.isEmpty()){
                    printerId = cats.get(0).getPrinterId();
                }
                ticket.setPrinterId(printerId);
                resp = App.printTicket(sresource, ticket, tName);
            }
        } catch (BasicException ex) {
            Logger.getLogger(SecondCoursesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 3. Send result OK
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ resp +" }");
    }
    
}