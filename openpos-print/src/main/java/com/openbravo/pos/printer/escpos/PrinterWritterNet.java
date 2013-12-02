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
package com.openbravo.pos.printer.escpos;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PrinterWritterNet extends PrinterWritter {

    private int m_port = 9100;
    private String m_sIpAddressPrinter;
    private OutputStream m_out;
    private Socket m_socket;

    public PrinterWritterNet(String sIpAndPort) {
        String[] parts = sIpAndPort.split(":");
        m_sIpAddressPrinter = parts[0];
        try {
            m_port = new Integer(parts[1]).intValue();
        } catch(Exception ex){}
        m_out = null;
        m_socket = null;
        runAtBackground = false;
    }

    @Override
    protected boolean internalWrite(byte[] data) {
        boolean resp = false;
        try {
            if (m_out == null) {
                // Connect to TCP/IP port 9100 of the printer and write data
                if(m_socket==null){
                    m_socket = new Socket(m_sIpAddressPrinter, m_port);
                }
                //out = new PrintWriter(socket.getOutputStream(),true);
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                m_out = m_socket.getOutputStream();
            }
            m_out.write(data);
            //m_socket.close();
            resp = true;
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + m_sIpAddressPrinter);
        } catch (IOException e) {
            System.err.println("No I/O: " + e.getMessage());
        }
        return resp;
    }

    @Override
    protected boolean internalFlush() {
        return this.internalClose();
//        try {  
//            if (m_out != null) {
//                m_out.flush();
//                
//            }
//        } catch (IOException e) {
//            System.err.println(e);
//        }    
    }

    @Override
    protected boolean internalClose() {
        boolean resp = false;
        try {  
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
                if(m_socket!=null && !m_socket.isClosed()){
                    m_socket.close();
                }
                m_socket = null;
            }
            resp = true;
        } catch (IOException e) {
            System.err.println(e);
        }
        return resp;
    }
}
