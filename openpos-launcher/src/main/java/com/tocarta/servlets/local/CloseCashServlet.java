/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.panels.PaymentsModel;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.util.AltEncrypter;
import com.tocarta.App;
import com.tocarta.services.Login;
import com.tocarta.services.Setup;
import com.tocarta.services.TestConnection;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.openpos.OpenPos;
import org.openpos.reports.IReportsPublishService;

public class CloseCashServlet extends HttpServlet {

    public CloseCashServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean resp = false;
        try {
            resp = closeCash();
        } catch (Exception ex) {
            Logger.getLogger(CloseCashServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": " + resp + " }");
    }

    private boolean closeCash() {
        boolean res = false;
        AppView m_App = App.appView;
        Date dNow = new Date();
        try {
            // Cerramos la caja si esta pendiente de cerrar.
            if (m_App.getActiveCashDateEnd() == null) {
                new StaticSentence(m_App.getSession(), "UPDATE CLOSEDCASH SET DATEEND = ? WHERE HOST = ? AND MONEY = ?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.STRING, Datas.STRING}))
                        .exec(new Object[]{dNow, m_App.getProperties().getHost(), m_App.getActiveCashIndex()});
            }
        } catch (BasicException ex) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), ex);
            Logger.getLogger(CloseCashServlet.class.getName()).log(Level.SEVERE, null, ex);
            res = false;
        }
        try {
            // Creamos una nueva caja          
            m_App.setActiveCash(UUID.randomUUID().toString(), m_App.getActiveCashSequence() + 1, dNow, null);
            DataLogicSystem m_dlSystem = (DataLogicSystem)m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
            // creamos la caja activa      
            m_dlSystem.execInsertCash(new Object[]{m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashSequence(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});
            PaymentsModel m_PaymentsToClose = PaymentsModel.loadInstance(m_App);
            // ponemos la fecha de fin
            m_PaymentsToClose.setDateEnd(dNow);
            // print report
            printPayments("Printer.CloseCash");
            res = true;
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
            //msg.show(this);
            res = false;
        }
        return res;
    }
    
    private void printPayments(String report) throws BasicException {
        AppView m_App = App.appView;
        DataLogicSystem m_dlSystem = (DataLogicSystem)m_App.getBean("com.openbravo.pos.forms.DataLogicSystem");
        PaymentsModel m_PaymentsToClose = PaymentsModel.loadInstance(m_App);
        TicketParser m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        String sresource = m_dlSystem.getResourceAsXML(report);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            //msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
                String evaluatedReport = script.eval(sresource).toString();
                m_TTP.printTicket(evaluatedReport);
                if(OpenPos.getApplicationContext()!=null){
                    IReportsPublishService reportsPublishService = OpenPos.getApplicationContext().getBean(IReportsPublishService.class);
                    reportsPublishService.sendDayEndReport(evaluatedReport);
                }
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                //msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                //msg.show(this);
            }
        }
    }
    

}
