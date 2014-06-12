/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tocarta.services;

import com.openbravo.pos.forms.AppConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author gal
 */
public class Setup {
    
    public static AppConfig config = null;
    public static WebResource service = null;
    
    public static void init(AppConfig conf){
        config = conf;
        clientConnection();
    }
    
    // setup connection to server
    private static void clientConnection(){
        String baseUrl = config.getProperty("mw.url");
        ClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create(clientConfig);
        service = client.resource(UriBuilder.fromUri(baseUrl).build());
    }
    
}
