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
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.tocarta.App;
import com.tocarta.Ticket;
import com.tocarta.TicketLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
 
public class OrderServlet extends HttpServlet
{
    
    public OrderServlet(){
    
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Use method POST to send an order</h1>");
        //response.getWriter().println("session=" + request.getSession(true).getId());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        while(true){
            String line = br.readLine();
            if(line==null) break;
            else { jsonStr += line; }
        }
        // 2. Process JSON to Ticket Object
        ObjectMapper mapper = new ObjectMapper();
        Ticket ticket = mapper.readValue(jsonStr,Ticket.class);
        this.processOrder(ticket);
        // 3. Send result OK
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": true }");
    }
    
    private void processOrder(Ticket newticket){
        try {
            System.out.println(" [x] Received '" + newticket.toString() + "'");

            // update ticket and insert new ticket lines
            AppView m_App = App.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            String ticketId = new Integer(newticket.getTableNumber()).toString();
            TicketInfo ticket = dlReceipts.getSharedTicket(ticketId);
            
            // fixing the standard tax for now
            Date d = null;
            try { d = new SimpleDateFormat("yyyy-MM-dd").parse("2012-01-01"); } catch (Exception ex) {}
            TaxInfo tax = new TaxInfo("001", "Tax Standard", "001", d, null, null, 0.1, false, null);
            
            for (TicketLine newticketLine : newticket.getTicketLines()){
                String productId = newticketLine.getProductSid();
                String categoryId = newticketLine.getCategorySid();
                String productName = newticketLine.getProductName();
                double dMultiply = newticketLine.getMultiply();
                double dPrice = newticketLine.getPrice();
                Properties props = new Properties();
                props.setProperty("product.taxcategoryid", "001");
                props.setProperty("product.com", "false");
                props.setProperty("product.categoryid", categoryId);
                props.setProperty("product.name", productName);
                if(newticketLine.getModifierListSet()!=null && !newticketLine.getModifierListSet().getModifierLists().isEmpty()){
                    props.setProperty("product.attsetid", newticketLine.getModifierListSet().getSid()); // fixing this for now
                    props.setProperty("product.attsetdesc", newticketLine.printAllModifiers());
                }
                TicketLineInfo ticketline = new TicketLineInfo(productId, dMultiply, dPrice, tax, props);
                ticket.insertLine(ticket.getLinesCount(), ticketline);
            }
            
            // save ticket in database
            dlReceipts.updateSharedTicket(ticketId, ticket);

            // print current ticket
            System.out.println("Table "+ ticketId +" has " + ticket.getLinesCount() + " lines");
            String sresource = "Printer.TicketPreview";
            this.printTicket(m_App, sresource, ticket, "Table "+ticketId);
        } catch (BasicException ex) {
            Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(OrderServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void printTicket(AppView m_App, String sresourcename, TicketInfo ticket, Object ticketext) throws BasicException {
        DataLogicSystem dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        DataLogicSales dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
        SentenceList senttax = dlSales.getTaxList();
        java.util.List<TaxInfo> taxlist = senttax.list();
        ListKeyed taxcollection = new ListKeyed<TaxInfo>(taxlist);
        TaxesLogic taxeslogic = new TaxesLogic(taxlist);
        TicketParser m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);

        String sresource = dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            System.out.println("ERROR: sresource = null");
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxcollection);
                script.put("taxeslogic", taxeslogic);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                System.out.println("ERROR: ScriptException = " + e.toString());
            } catch (TicketPrinterException e) {
                System.out.println("ERROR: TicketPrinterException = " + e.toString());
            }
        }
    }
    
    
    
    
    
    
}

// Order JSON should look like this


/*

{
    "table_number": 1,
    "ticket_lines": [
        {
            "product_sid": "5d004328-aa99-4666-8241-73e9a4f2e8ea",
            "category_sid": "22ce7475-6cf1-4afa-b8a9-fef05c60f958",
            "product_name": "Lady Burguer P",
            "multiply": 1,
            "price": 9.9,
            "modifier_list_set": {
                "name": "Carnes",
                "sid": "3de3c011-d33b-4425-89a4-5ca4f86c64d5",
                "modifier_lists": [
                    {
                        "sid": "d1679d5c-f3b9-45c5-8dbe-466a08758d8c",
                        "name": "Término de la Carne",
                        "selected_modifiers": [
                            {
                                "sid": "5858344a-1795-4dc0-b461-bf58d6f230c8",
                                "name": "Poco Hecha"
                            }
                        ]
                    }
                ]
            },
            "note": "cambiar patatas por ensalada"
        }
    ]
}

*/

