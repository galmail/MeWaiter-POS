/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.openbravo.pos.forms.AppConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.codehaus.jackson.map.ObjectMapper;
 
public class LoginServlet extends HttpServlet
{   
    private static AppConfig config = null;
    
    public LoginServlet(AppConfig conf){
        config = conf;
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            String baseUrl = config.getProperty("mw.url");
            String email = request.getParameter("email");
            String passwd = request.getParameter("password");
            
            // setup connection to server
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource service = client.resource(UriBuilder.fromUri(baseUrl).build());
            
            // testing connection
            String res1 = service.path("/cli/c").path("/hello").accept(MediaType.APPLICATION_JSON).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(res1, HashMap.class);
            
            // login and get auth token
            MultivaluedMap formData = new MultivaluedMapImpl();
            formData.add("email", email);
            formData.add("password", passwd);
            String res2 = service.path("/users").path("/sign_in").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData).getEntity(String.class);
            map = mapper.readValue(res2, HashMap.class);
            if ((Boolean) map.get("success")) {
                resp = true;
                HttpSession session = request.getSession(true);
                session.setAttribute("mwuser", email);
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        response.sendRedirect("/");
    }
}