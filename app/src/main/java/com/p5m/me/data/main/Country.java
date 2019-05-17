package com.p5m.me.data.main;


public class Country implements java.io.Serializable {
    /**
     * id : 1
     * name : KUWAIT
     * sortname : KWT
     * phonecode : 78
     * status : true
     */

    private int id;
    private String name;
    private String sortname;
    private String phonecode;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
