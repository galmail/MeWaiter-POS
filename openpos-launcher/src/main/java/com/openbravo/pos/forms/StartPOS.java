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
import com.tocarta.services.Setup;
import java.awt.Desktop;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author adrianromero
 */
public class StartPOS {

    private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");
    
    private static AppConfig config = null;

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
        // load config file
        config = new AppConfig(args);
        config.load();
        Setup.init(config);
        
        // start openbravo
        java.awt.EventQueue.invokeLater(new OpenPOS(config));
        try {
            // start listening to RabbitMQ messages
            //new Thread(new ApiReceiver(args)).start();
            
            // start web server
            int port = Integer.parseInt(config.getProperty("mw.server_port"));
            openWebpage("http://localhost:"+port);
            startWebServer(port);
        } catch (Exception ex) {
            Logger.getLogger(OpenPOS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean startWebServer(int port){
        try {
            // listen to Web Services on port 8080
            Server server = new Server(port);
            
            String webapp = "src/main/webapp";
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setWelcomeFiles(new String[]{ "index.html" });
            resource_handler.setResourceBase(webapp);
            
            ServletContextHandler contextV1 = setupV1API();
            ServletContextHandler contextV2 = setupV2API();
            ServletContextHandler contextCLI = setupLocalServlets();
            
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] { contextV1, contextV2, contextCLI, resource_handler , new DefaultHandler() });
            server.setHandler(handlers);
            
            server.start();
            server.join();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(StartPOS.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private static ServletContextHandler setupV1API(){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/v1");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.HelloServlet()), "/hello");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.TableServlet()), "/table");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.OrderServlet()), "/order");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.TicketServlet()), "/ticket");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v1.SecondCoursesServlet()), "/actions/bring_second_courses");
        return context;
    }
    
    private static ServletContextHandler setupV2API(){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/v2");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.HelloServlet()), "/hello");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.TableServlet()), "/table");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.OrderServlet()), "/order");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.TicketServlet()), "/ticket");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.v2.SecondCoursesServlet()), "/actions/bring_second_courses");
        return context;
    }
    
    private static ServletContextHandler setupLocalServlets(){
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/cli");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.MainServlet()), "/");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.LogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.UpdateServlet()), "/update");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.ShutdownServlet()), "/shutdown");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.ShowConfigServlet()), "/show_config");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.ShowPrinterServlet()), "/show_printer");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.DailyCashServlet()), "/daily_cash");
        context.addServlet(new ServletHolder(new com.tocarta.servlets.local.CloseCashServlet()), "/close_cash");
        return context;
    }
    
    
}
