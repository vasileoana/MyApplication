package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 06-Mar-17.
 */

public class Category {

       public static final String TABLE = "Categories";

    public static final String label_categoryName="CategoryName";
    public static final String label_idCategory="IdCategory";
    public static final String label_idParent="idSuccessor";

    private int idCategory;
    private int idParent;
    private String categoryName ;

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", idParent=" + idParent +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
