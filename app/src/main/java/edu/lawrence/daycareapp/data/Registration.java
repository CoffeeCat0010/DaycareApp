package edu.lawrence.daycareapp.data;

import java.util.Date;

public class Registration {
    private int id;
    private int child;
    private int provider;
    private Date start;
    private Date end;
    private int status;

    public Registration(){}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getChild(){
        return child;
    }
    public void setChild(int child) {
        this.child = child;
    }
    public int getProvider(){
        return provider;
    }
    public void setProvider(int provider) {
        this.provider = provider;
    }
    public Date getStart(){
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }
    public Date getEnd(){
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
    public int getStatus(){
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
