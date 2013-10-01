/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import com.openbravo.format.Formats;
import com.tocarta.App;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;

/**
 *
 * @author gal
 */
public class OpenPOS implements Runnable {
    
        private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.OpenPOS");

        private String[] initArgs = null;

        public OpenPOS(String[] args) {
            initArgs = args;
        }

        public void run() {
//            threadMessage("Running OpenPOSThread...");

//                if (!registerApp()) {
//                    System.exit(1);
//                }

            AppConfig config = new AppConfig(initArgs);
            config.load();

            // set Locale.
            String slang = config.getProperty("user.language");
            String scountry = config.getProperty("user.country");
            String svariant = config.getProperty("user.variant");
            if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
                Locale.setDefault(new Locale(slang, scountry, svariant));
            }

            // Set the format patterns
            Formats.setIntegerPattern(config.getProperty("format.integer"));
            Formats.setDoublePattern(config.getProperty("format.double"));
            Formats.setCurrencyPattern(config.getProperty("format.currency"));
            Formats.setPercentPattern(config.getProperty("format.percent"));
            Formats.setDatePattern(config.getProperty("format.date"));
            Formats.setTimePattern(config.getProperty("format.time"));
            Formats.setDateTimePattern(config.getProperty("format.datetime"));

            // Set the look and feel.
            try {

                Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();

                if (laf instanceof LookAndFeel) {
                    UIManager.setLookAndFeel((LookAndFeel) laf);
                } else if (laf instanceof SubstanceSkin) {
                    SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Cannot set look and feel", e);
            }

            String screenmode = config.getProperty("machine.screenmode");
            if ("fullscreen".equals(screenmode)) {
                JRootKiosk rootkiosk = new JRootKiosk();
                App.appView = rootkiosk.initFrame(config);
                
            } else {
                JRootFrame rootframe = new JRootFrame();
                App.appView = rootframe.initFrame(config);
            }
        }
    }
