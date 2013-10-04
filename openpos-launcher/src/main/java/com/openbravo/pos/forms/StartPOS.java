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
import org.eclipse.jetty.server.RetryRequest;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author adrianromero
 */
public class StartPOS {

    private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");

    /**
     * Creates a new instance of StartPOS
     */
    public StartPOS() {
    }

    // Display a message, preceded by
    // the name of the current thread
//    static void threadMessage(String message) {
//        String threadName =
//                Thread.currentThread().getName();
//        System.out.format("%s: %s%n",
//                threadName,
//                message);
//    }
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
//        java.awt.EventQueue.invokeLater(new Runnable() {});

//        threadMessage("Starting OpenPOSThread...");
//        Thread t = new Thread(new OpenPOS(args));
//        t.start();

        java.awt.EventQueue.invokeLater(new OpenPOS(args));

        try {
            // start listening to RabbitMQ messages
            new Thread(new ApiReceiver(args)).start();
            
            // listen to Web Services on port 8080
            Server server = new Server(8080);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addServlet(new ServletHolder(new HelloServlet()), "/*");
            System.out.println("Try access: http://localhost:8080/");
            context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")), "/it/*");
            System.out.println("Try access: http://localhost:8080/it/");
            context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")), "/fr/*");
            System.out.println("Try access: http://localhost:8080/fr/");

            server.start();
            server.join();

        } catch (Exception ex) {
            Logger.getLogger(OpenPOS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
