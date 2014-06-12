/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tocarta.services;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.IOException;
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
public class Login {
    
    public static boolean login(String email, String passwd){
        boolean resp = false;
        try {
            // login and get auth token
            MultivaluedMap formData = new MultivaluedMapImpl();
            formData.add("email", email);
            formData.add("password", passwd);
            String res = Setup.service.path("/users").path("/sign_in").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData).getEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(res, HashMap.class);
            resp = (Boolean) map.get("success");
            if(resp){
                String token = (String) map.get("auth_token");
                Setup.config.setProperty("mw.auth_token", token);
                Setup.config.save();
            }
        }
        catch(IOException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        return resp;
    }
    
}
