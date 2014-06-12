/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tocarta.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author gal
 */
public class TestConnection {
    
    public static boolean echo(){
        boolean resp = false;
        try {
            // testing connection
            String res = Setup.service.path("/cli/c").path("/hello").accept(MediaType.APPLICATION_JSON).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(res, HashMap.class);
            resp = (Boolean) map.get("result");
        }
        catch(IOException ex){
            Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
            resp = false;
        }
        return resp;
    }
    
    
}
