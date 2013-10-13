/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.io.Serializable;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
public class Ticket implements Serializable {
    
    @JsonProperty("table_sid")
    private String tableSid;
    @JsonProperty("ticket_lines")
    private List<TicketLine> ticketLines;

    public Ticket() {
    }
    
    public String getTableSid() {
        return tableSid;
    }

    public void setTableSid(String sid) {
        this.tableSid = sid;
    }

    public List<TicketLine> getTicketLines() {
        return ticketLines;
    }

    public void setTicketLines(List<TicketLine> ticketLines) {
        this.ticketLines = ticketLines;
    }
    
}
