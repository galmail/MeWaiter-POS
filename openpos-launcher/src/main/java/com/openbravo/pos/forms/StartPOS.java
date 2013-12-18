//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.forms;

import com.openbravo.pos.instance.InstanceQuery;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author adrianromero
 */
public class StartPOS {

    private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");
    private static int port = 8080;

    /**
     * Creates a new instance of StartPOS
     */
    public StartPOS() {
    }

    public static boolean registerApp() {
        // vemos si existe alguna instancia        
        InstanceQuery i = null;
        try {
            i = new InstanceQuery();
            i.getAppMessage().restoreWindow();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void main(final String args[]) throws InterruptedException {
        //OpenPos.initApplicationContext("org.openpos","com.openbravo");
        java.awt.EventQueue.invokeLater(new OpenPOS(args));

        try {
            // start listening to RabbitMQ messages
            //new Thread(new ApiReceiver(args)).start();
            // start web server
            startWebServer();
        } catch (Exception ex) {
            Logger.getLogger(OpenPOS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static void startWebServer(){
        try {
            // listen to Web Services on port 8080
            Server server = new Server(port);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context = setupV1API(context);
            context = setupV2API(context);
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(StartPOS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static ServletContextHandler setupV1API(ServletContextHandler context){
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.HelloServlet()), "/");
        System.out.println("Try http://localhost:8080 to make sure server is up");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.TableServlet()), "/table");
        System.out.println("Try to GET/POST a Table on http://localhost:8080/table");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.OrderServlet()), "/order");
        System.out.println("Try to POST an Order on http://localhost:8080/order");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.TicketServlet()), "/ticket");
        System.out.println("Try to GET/POST a Ticket on http://localhost:8080/ticket");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.SecondCoursesServlet()), "/actions/bring_second_courses");
        System.out.println("Try to POST some action on http://localhost:8080/actions/<name>");
        return context;
    }
    
    private static ServletContextHandler setupV2API(ServletContextHandler context){
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.HelloServlet()), "/v2");
        System.out.println("Try http://localhost:8080/v2 to make sure server is up");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.TableServlet()), "/v2/table");
        System.out.println("Try to GET/POST a Table on http://localhost:8080/v2/table");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.OrderServlet()), "/v2/order");
        System.out.println("Try to POST an Order on http://localhost:8080/v2/order");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.TicketServlet()), "/v2/ticket");
        System.out.println("Try to GET/POST a Ticket on http://localhost:8080/v2/ticket");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.SecondCoursesServlet()), "/v2/actions/bring_second_courses");
        System.out.println("Try to POST some action on http://localhost:8080/v2/actions/<name>");
        return context;
    }
    
}
