/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {
    
    @JsonProperty("table_sid")
    private String tableSid;
    @JsonProperty("table_name")
    private String tableName;
    @JsonProperty("payment_lines")
    private List<PaymentLine> paymentLines;
    private List<Discount> discounts;

    public Payment() {
    }

    public String getTableSid() {
        return tableSid;
    }

    public void setTableSid(String tableSid) {
        this.tableSid = tableSid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<PaymentLine> getPaymentLines() {
        return paymentLines;
    }

    public void setPaymentLines(List<PaymentLine> paymentLines) {
        this.paymentLines = paymentLines;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }
    
}
