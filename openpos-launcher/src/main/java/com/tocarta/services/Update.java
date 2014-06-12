/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tocarta.services;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author gal
 */
public class Update {
    
    public static boolean uploadPosIP(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String token = Setup.config.getProperty("mw.auth_token");
            // set POS IP Address
            MultivaluedMap ipData = new MultivaluedMapImpl();
            ipData.add("auth_token", token);
            ipData.add("ip", ip);
            String response = Setup.service.path("/cli/mw").path("/load_pos_ip_address").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, ipData).getEntity(String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            HashMap<String, Object> map = mapper.readValue(response, HashMap.class);
//            return ((Boolean) map.get("result"));
            return true;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public static HashMap<String, Object> loadRestaurant(){
        try {
            String token = Setup.config.getProperty("mw.auth_token");
            String restMenu = Setup.service.path("/cli/c").path("/get_restaurant_info").queryParam("auth_token", token).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(restMenu, HashMap.class);
            return map;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static HashMap<String, Object> loadModifiers(){
        try {
            String token = Setup.config.getProperty("mw.auth_token");
            String restModifiers = Setup.service.path("/cli/mw").path("/modifiers").queryParam("auth_token", token).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(restModifiers, HashMap.class);
            return map;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static HashMap<String, Object> loadTables(){
        try {
            String token = Setup.config.getProperty("mw.auth_token");
            String restTables = Setup.service.path("/cli/mw").path("/tables").queryParam("auth_token", token).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(restTables, HashMap.class);
            return map;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static HashMap<String, Object> loadResources(){
        try {
            String token = Setup.config.getProperty("mw.auth_token");
            String posResources = Setup.service.path("/cli/mw").path("/pos_resources").queryParam("auth_token", token).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(posResources, HashMap.class);
            return map;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public static HashMap<String, Object> loadDiscounts(){
        try {
            String token = Setup.config.getProperty("mw.auth_token");
            String posDiscounts = Setup.service.path("/cli/mw").path("/discounts").queryParam("auth_token", token).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(posDiscounts, HashMap.class);
            return map;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    
}
