package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 06-Mar-17.
 */

public class Product {

    public static final String TABLE = "Product";

    public static final String label_idProduct="IdProduct";
    public static final String label_idCategory="IdCategory";
    public static final String label_description="Description";
    public static final String label_function="Function";
    public static final String label_brand="Brand";

    private int idProduct;
    private int idCategory;
    private String description;
    private String brand;
    private String function;

    public Product() {
    }

    public Product(String description,String brand,int idCategory, String function) {
        this.description = description;
        this.brand=brand;
        this.idCategory=idCategory;
        this.function=function;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", idCategory=" + idCategory +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", function='" + function + '\'' +
                '}';
    }
}
