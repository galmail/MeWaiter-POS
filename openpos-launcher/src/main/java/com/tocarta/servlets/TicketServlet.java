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
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.payment.JPaymentInterface;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoCash;
import com.openbravo.pos.payment.PaymentInfoFree;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.TaxesException;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.tocarta.App;
import com.tocarta.Payment;
import com.tocarta.PaymentLine;
import com.tocarta.Ticket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class TicketServlet extends HttpServlet {

    public TicketServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // get table number
            String ticketId = request.getParameter("table");
            DataLogicReceipts dlReceipts = new DataLogicReceipts();
            dlReceipts.init(App.appView.getSession());
            TicketInfo ticket = dlReceipts.getSharedTicket(ticketId);
            if (ticket != null) {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean resp = false;
        boolean closeTable = Boolean.parseBoolean(request.getParameter("close_table"));
        // 1. Get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String jsonStr = "";
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            } else {
                jsonStr += line;
            }
        }
        // 2. Process JSON to Ticket Object
        ObjectMapper mapper = new ObjectMapper();
        Payment payment = mapper.readValue(jsonStr, Payment.class);
        resp = this.printBill(payment,closeTable);
        // 3. Send result OK
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": " + resp + " }");
    }

    private List<PaymentInfo> setupPayments(double total, List<PaymentLine> paymentLines) {
        LinkedList<PaymentInfo> pList = new LinkedList<PaymentInfo>();
        for(PaymentLine paymentLine : paymentLines){
            if(paymentLine.getName().matches("cash")){
                pList.add(new PaymentInfoCash(total,paymentLine.getAmount()));
            }
            else if(paymentLine.getName().matches("free")){
                pList.add(new PaymentInfoFree(total));
            }
        }
        return pList;
    }

    private boolean printBill(Payment payment, boolean closeTable) {
        try {
            // 1. Get Ticket
            AppView m_App = App.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            TicketInfo ticket = dlReceipts.getSharedTicket(payment.getTableSid());

            DataLogicSales dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSales");
            SentenceList senttax = dlSales.getTaxList();
            java.util.List<TaxInfo> taxlist = senttax.list();
            ListKeyed taxcollection = new ListKeyed<TaxInfo>(taxlist);
            TaxesLogic taxeslogic = new TaxesLogic(taxlist);

            // reset the payment info
            taxeslogic.calculateTaxes(ticket);
            if (ticket.getTotal() >= 0.0) {
                ticket.resetPayments(); //Only reset if is sale
            }

            // assign the payments selected and calculate taxes.         
            ticket.setPayments(setupPayments(ticket.getTotal(),payment.getPaymentLines()));

            // Asigno los valores definitivos del ticket...
            ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket.setActiveCash(m_App.getActiveCashIndex());
            ticket.setDate(new Date()); // Le pongo la fecha de cobro

            // Save the receipt and assign a receipt number
            if(closeTable){
                dlSales.saveTicket(ticket, m_App.getInventoryLocation());
            }

            // Print receipt.
            App.printTicket("Printer.Ticket", ticket, payment.getTableName());
            
            if(closeTable){
                // reset the payment info and close table
                ticket.resetTaxes();
                ticket.resetPayments();
                dlReceipts.deleteSharedTicket(payment.getTableSid());
            }
            
        } catch (TaxesException ex) {
            Logger.getLogger(TicketServlet.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (BasicException ex) {
            Logger.getLogger(TicketServlet.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    /*
     
     {
        "table_sid": "629a322a-6416-4a50-827c-83ab911d94b5",
        "table_name": "Terraza Mesa 1",
        "payment_lines": [
           {
              "name": "cash",
              "sid": "whatever...",
              "amount": 50.0
           }
        ]
     }
     
     */
}
