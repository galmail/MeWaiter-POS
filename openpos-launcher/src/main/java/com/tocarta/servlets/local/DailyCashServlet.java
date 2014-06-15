/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.openbravo.pos.panels.PaymentsModel;
import com.tocarta.App;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class DailyCashServlet extends HttpServlet
{   
    public DailyCashServlet(){
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ ");
        try {
            PaymentsModel m_PaymentsToClose = PaymentsModel.loadInstance(App.appView);
            String totalMoney = m_PaymentsToClose.printPaymentsTotal();
            String totalPayments = m_PaymentsToClose.printPayments();
            response.getWriter().print("\"total\": \""+totalMoney+"\", ");
            response.getWriter().print("\"payments\": \""+totalPayments+"\", ");
            resp = true;
        } catch (Exception ex) {
            Logger.getLogger(DailyCashServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("\"success\": "+ resp +" }");
    }
    
}