/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import com.google.gson.Gson;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.sales.DataLogicReceipts;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sales.TaxesLogic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author gal
 */
public class ApiReceiver {

    private final static String QUEUE_NAME = "hello";

    public static void listen() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            byte[] data = delivery.getBody();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            String json = (String) is.readObject();
            Gson gson = new Gson();
            Ticket newticket = gson.fromJson(json, Ticket.class);
            
//            Ticket newticket = (Ticket) is.readObject();
            System.out.println(" [x] Received '" + newticket.toString() + "'");

            // update ticket and insert new ticket lines
            AppView m_App = OpenPOS.appView;
            DataLogicReceipts dlReceipts = (DataLogicReceipts) m_App.getBean("com.openbravo.pos.sales.DataLogicReceipts");
            String ticketId = new Integer(newticket.getTableNumber()).toString();
            TicketInfo ticket = dlReceipts.getSharedTicket(ticketId);
            
            // fixing the standard tax for now
            Date d = null;
            try { d = new SimpleDateFormat("yyyy-MM-dd").parse("2012-01-01"); } catch (Exception ex) {}
            TaxInfo tax = new TaxInfo("001", "Tax Standard", "001", d, null, null, 0.1, false, null);
            
            for (TicketLine newticketLine : newticket.getTicketLines()){
                String productId = newticketLine.getProductId();
                String categoryId = newticketLine.getCategoryid();
                String productName = newticketLine.getProductName();
                double dMultiply = newticketLine.getMultiply();
                double dPrice = newticketLine.getPrice();
                Properties props = new Properties();
                props.setProperty("product.taxcategoryid", "001");
                props.setProperty("product.com", "false");
                props.setProperty("product.categoryid", categoryId);
                props.setProperty("product.name", productName);
                if(newticketLine.hasModifiers()){
                    props.setProperty("product.attsetid", "44b8cec9-e646-43b4-8f25-bf667e4bc36e"); // fixing this for now
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
            ApiReceiver.printTicket(m_App, sresource, ticket, "Table "+ticketId);
        }
    }

    public static void printTicket(AppView m_App, String sresourcename, TicketInfo ticket, Object ticketext) throws BasicException {
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
