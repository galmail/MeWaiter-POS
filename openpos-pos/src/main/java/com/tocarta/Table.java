/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tocarta;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author gal
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Table {
    
    private int id;
    private String sid;
    private int positionX;
    private int positionY;
    private int number;
    private String name;

    public Table() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    public int getPositionX() {
        if(this.positionX>0) return positionX;
        else {
            return (((this.number-1)%4)+1)*133;
        }
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        if(this.positionY>0) return positionY;
        else {
            return (((this.number-1)%4)+1)*113;
        }
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getName(){
        if(this.name!=null && this.name.length()>0) return this.name;
        else return "Mesa " + this.getNumber();
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
