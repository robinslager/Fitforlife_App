package com.example.user.fit4life.Objects;

public class product {



    private String product_name;
    private String product_merk;
    private int cal_100gram;

    public product(String product_name, String product_merk, int cal_100gram) {
        this.product_name = product_name;
        this.product_merk = product_merk;
        this.cal_100gram = cal_100gram;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_merk(String product_merk) {
        this.product_merk = product_merk;
    }

    public void setCal_100gram(int cal_100gram) {
        this.cal_100gram = cal_100gram;
    }

}
