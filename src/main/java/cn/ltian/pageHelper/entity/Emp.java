package cn.ltian.pageHelper.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class Emp {
    @Id
    @Column(name="ID")
    private String id;
    @Column(name="NAME")
    private String name;
    @Column(name="DEPTID")
    private String deptid;
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

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", deptid='" + deptid + '\'' +
                '}';
    }
}
