/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author gal
 */
public class Ticket implements Serializable {
    
    private int tableNumber;
    private List<TicketLine> ticketLines;

    public Ticket() {
    }
    
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<TicketLine> getTicketLines() {
        return ticketLines;
    }

    public void setTicketLines(List<TicketLine> ticketLines) {
        this.ticketLines = ticketLines;
    }
    
}
