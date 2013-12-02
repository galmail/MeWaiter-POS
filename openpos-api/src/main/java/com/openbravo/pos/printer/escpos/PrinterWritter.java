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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PrinterWritter {
    
    private boolean initialized = false;
    protected boolean runAtBackground = true;

    private ExecutorService exec;
    
    public PrinterWritter() {
        exec = Executors.newSingleThreadExecutor();
    }
    
    protected abstract boolean internalWrite(byte[] data);
    protected abstract boolean internalFlush();
    protected abstract boolean internalClose();
    
    public void init(final byte[] data) {
        if (!initialized) {
            write(data);
            initialized = true;
        }
    }
       
    public boolean write(String sValue) {
        return write(sValue.getBytes());
    }

    public boolean write(final byte[] data) {
        if(runAtBackground){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    internalWrite(data);
                }
            });
            return true;
        }
        else {
            return internalWrite(data);
        }
    }
    
    public boolean flush() {
        if(runAtBackground){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    internalFlush();
                }
            });
            return true;
        }
        else {
            return internalFlush();
        }
    }
    
    public boolean close() {
        boolean resp = false;
        if(runAtBackground){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    internalClose();
                }
            });
            exec.shutdown();
            return true;
        }
        else {
            resp = internalClose();
            exec.shutdown();
        }
        return resp;
    }
}
