package cn.edu.hznu.labaddressclient;

public class Address {
    private int id;
    private String name;
    private String mobile;

    public Address(int id,String name,String mobile){
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }
    public Address(String name,String mobile){
        this.name = name;
        this.mobile = mobile;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
