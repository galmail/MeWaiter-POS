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

package com.openbravo.pos.config;

import com.openbravo.data.user.DirtyManager;
import java.awt.Component;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.util.AltEncrypter;
import com.openbravo.pos.util.DirectoryEvent;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.tocarta.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author adrianromero
 */
public class JPanelConfigServer extends javax.swing.JPanel implements PanelConfig {
    
    private DirtyManager dirty = new DirtyManager();
    
    /** Creates new form JPanelConfigServer */
    public JPanelConfigServer() {
        initComponents();
        jtxtMwURL.getDocument().addDocumentListener(dirty);
        jtxtMwEmail.getDocument().addDocumentListener(dirty);
        jtxtMwPassword.getDocument().addDocumentListener(dirty);
    }
    
    public boolean hasChanged() {
        return dirty.isDirty();
    }
    
    public Component getConfigComponent() {
        return this;
    }
   
    public void loadProperties(AppConfig config) {
        if(config.getProperty("mw.localIP")==null || config.getProperty("mw.localIP")==""){
            try {
                jtxtMwLocalIP.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
                Logger.getLogger(JPanelConfigServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            jtxtMwLocalIP.setText(config.getProperty("mw.localIP"));
        }
        jtxtMwURL.setText(config.getProperty("mw.url"));
        String sMWemail = config.getProperty("mw.email");
        String sMWpassword = config.getProperty("mw.password");
        if (sMWemail != null && sMWpassword != null && sMWpassword.startsWith("crypt:")) {
            // La clave esta encriptada.
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sMWemail);
            sMWpassword = cypher.decrypt(sMWpassword.substring(6));
        }        
        jtxtMwEmail.setText(sMWemail);
        jtxtMwPassword.setText(sMWpassword);
        dirty.setDirty(false);
    }
   
    public void saveProperties(AppConfig config) {
        config.setProperty("mw.localIP", jtxtMwLocalIP.getText());
        config.setProperty("mw.url", jtxtMwURL.getText());
        config.setProperty("mw.email", jtxtMwEmail.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtMwEmail.getText());       
        config.setProperty("mw.password", "crypt:" + cypher.encrypt(new String(jtxtMwPassword.getPassword())));
        dirty.setDirty(false);
    }
    
    private void updateStatus(String msg){
        this.jLabelStatus.setText(msg);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtxtMwURL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtMwEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtMwPassword = new javax.swing.JPasswordField();
        jLabelStatus = new javax.swing.JLabel();
        jButtonImportMenu = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jtxtMwLocalIP = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("Label.mwServer"))); // NOI18N

        jLabel2.setText(AppLocal.getIntString("label.mwURL")); // NOI18N

        jLabel3.setText(AppLocal.getIntString("label.mwEmail")); // NOI18N

        jLabel4.setText(AppLocal.getIntString("label.mwPassword")); // NOI18N

        jLabelStatus.setText("Press \"Import Menu\" button to load menu from server");

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jButtonImportMenu.setLabel(bundle.getString("button.mwImportMenu")); // NOI18N
        jButtonImportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportMenuActionPerformed(evt);
            }
        });

        jLabel5.setText(AppLocal.getIntString("label.mwLocalIP")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jButtonImportMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtMwLocalIP, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtMwURL, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtMwEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtMwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtMwURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtMwLocalIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtMwEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtMwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonImportMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("MeWaiterAccount");
        jPanel1.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonImportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportMenuActionPerformed
        System.out.println("Import Menu Now");
        // connect to server
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(UriBuilder.fromUri(jtxtMwURL.getText()).build());
        try {
            updateStatus("Connecting to server...");
            String response = service.path("/cli/c").path("/hello").accept(MediaType.APPLICATION_JSON).get(String.class);
            ObjectMapper mapper = new ObjectMapper();
            //JsonNode actualObj = mapper.readTree(response);
            HashMap<String,Object> map = mapper.readValue(response, HashMap.class);
            if((Boolean)map.get("result")){
                updateStatus("Server is up!");
                // login and get auth token
                MultivaluedMap formData = new MultivaluedMapImpl();
                formData.add("email", jtxtMwEmail.getText());
                formData.add("password", new String(jtxtMwPassword.getPassword()));
                String result = service.path("/users").path("/sign_in").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, formData).getEntity(String.class);
                updateStatus(result);
                map = mapper.readValue(result, HashMap.class);
                if((Boolean)map.get("success")){
                   String token = (String)map.get("auth_token");
                   updateStatus("Login successfull... loading all menus from server...");
                   // get restaurant menu
                   MultivaluedMap inputData = new MultivaluedMapImpl();
                   inputData.add("auth_token", token);
                   String restMenu = service.path("/cli/c").path("/get_restaurant_info").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).method("GET", String.class, inputData);  //.get(String.class); //.json?auth_token=
                   map = mapper.readValue(restMenu, HashMap.class);
                   // create all sections
                   
                   List<Menu> menus = mapper.convertValue(map.get("menus"), new TypeReference<List<Menu>>() { });
                   
                   //List<Menu> menus = (List<Menu>)map.get("menus");
                   if(menus.isEmpty()==false){
                       updateStatus("Menus loaded!! Inserting menus in the database...");
                       for(Menu menu : menus){
                           menu.insertSectionsToDB();
                           for(Section section : menu.getSections()){
                               section.insertDishesToDB();
                           }
                       }
                       updateStatus("Menu Imported Succesfully!");
                   }
                   else {
                       updateStatus("Menus were not loaded!");
                   }
                }
                else {
                    updateStatus("Authentication Failed! Please verify email/password and try again.");
                }
            }
            else {
                updateStatus("Server is down!");
            }
        } catch (IOException ex) {
            Logger.getLogger(JPanelConfigServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(ClientHandlerException e){
            this.jLabelStatus.setText("Error connecting to server: "+e.getMessage());
        }
        catch(UniformInterfaceException e){
            this.jLabelStatus.setText("Error connecting to server: "+e.getMessage());
        }        
    }//GEN-LAST:event_jButtonImportMenuActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonImportMenu;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jtxtMwEmail;
    private javax.swing.JTextField jtxtMwLocalIP;
    private javax.swing.JPasswordField jtxtMwPassword;
    private javax.swing.JTextField jtxtMwURL;
    // End of variables declaration//GEN-END:variables
    
}
