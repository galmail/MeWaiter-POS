/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta.servlets.local;

/**
 *
 * @author gal
 */

import com.tocarta.App;
import com.tocarta.Discount;
import com.tocarta.Floor;
import com.tocarta.Menu;
import com.tocarta.ModifierListSet;
import com.tocarta.Resource;
import com.tocarta.Section;
import com.tocarta.services.TestConnection;
import com.tocarta.services.Update;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
 
public class UpdateServlet extends HttpServlet
{   
    public UpdateServlet(){
    
    }
    
    private void writeJsonResponse(boolean res, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{ \"success\": "+ res +" }");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resp = false;
        try {
            // test connection
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Testing Connection...");
            boolean echo = TestConnection.echo();
            if(!echo){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot connect to server.");
                writeJsonResponse(resp,response);
                return;
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Updating POS IP Address...");
            // updating ip pos address
            boolean posIpUpdated = Update.uploadPosIP();
            if(!posIpUpdated){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot update POS IP Address.");
                writeJsonResponse(resp,response);
                return;
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Loading Restaurant Menus...");
            // load restaurant data
            HashMap<String, Object> restMap = Update.loadRestaurant();
            if(restMap==null){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot load restaurant data.");
                writeJsonResponse(resp,response);
                return;
            }
            // clean database
            App.cleanDB();
            ObjectMapper mapper = new ObjectMapper();
            List<Menu> menus = mapper.convertValue(restMap.get("menus"), new TypeReference<List<Menu>>() { });
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Setting Menus...");
            // setup menus, sections, subsections and dishes
            if (menus.isEmpty() == false){
                Menu.SID = null;
                for (Menu menu : menus) {
                    menu.insertSectionsToDB();
                    for (Section section : menu.getSections()) {
                        section.insertDishesToDB();
                    }
                }
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Setting Modifiers...");
            // setup modifiers
            HashMap<String, Object> modifiersMap = Update.loadModifiers();
            if(modifiersMap==null){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot load modifiers.");
                writeJsonResponse(resp,response);
                return;
            }
            List<ModifierListSet> modifierListSets = mapper.convertValue(modifiersMap.get("modifier_list_sets"), new TypeReference<List<ModifierListSet>>() {});
            if(modifierListSets!=null && modifierListSets.isEmpty()==false){
                for (ModifierListSet modifierListSet : modifierListSets) {
                    modifierListSet.insertModifiersToDB();
                }
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Setting Tables and Floors...");
            // setup tables and floors
            HashMap<String, Object> tablesMap = Update.loadTables();
            if(tablesMap==null){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot load tables and floors.");
                writeJsonResponse(resp,response);
                return;
            }
            List<Floor> floors = mapper.convertValue(tablesMap.get("floors"), new TypeReference<List<Floor>>() {});
            if(floors!=null && floors.isEmpty()==false){
                for (Floor floor : floors) {
                    floor.insertTablestoDB();
                }
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Setting Printer Tickets...");
            // setup resources (tickets)
            HashMap<String, Object> resourcesMap = Update.loadResources();
            if(resourcesMap==null){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot load resources.");
                writeJsonResponse(resp,response);
                return;
            }
            List<Resource> resources = mapper.convertValue(resourcesMap.get("resources"), new TypeReference<List<Resource>>() {});
            if(resources!=null && resources.isEmpty()==false){
                for (Resource resource : resources) {
                    resource.updateOnDB();
                }
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Setting Discounts...");
            // setup discounts
            HashMap<String, Object> discountsMap = Update.loadDiscounts();
            if(discountsMap==null){
                Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Cannot load discounts.");
                writeJsonResponse(resp,response);
                return;
            }
            String discountCategoryId = Discount.insertDiscountCategory();
            List<Discount> discounts = mapper.convertValue(discountsMap.get("discounts"), new TypeReference<List<Discount>>() {});
            if(discounts!=null && discounts.isEmpty()==false){
                for (Discount discount : discounts) {
                    discount.insertOnDB(discountCategoryId);
                }
            }
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.INFO, "Updated Successfull.");
            resp = true;
        } catch (Exception ex) {
            Logger.getLogger(UpdateServlet.class.getName()).log(Level.SEVERE, ex.getMessage());
            resp = false;
        }
        writeJsonResponse(resp,response);
    }
}