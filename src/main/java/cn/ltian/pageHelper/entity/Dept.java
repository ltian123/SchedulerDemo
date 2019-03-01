package cn.ltian.pageHelper.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class Dept {

    @Id
    @Column(name="ID")
    private String id;
    @Column(name="NAME")
    private String name;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
