package com.google.android.myapplication.DataBase.Model;

/**
 * Created by Oana on 26-Feb-17.
 */

public class User {

    public static final String TABLE = "Users";

    public static final String label_email="Email";
    public static final String label_username="Username";
    public static final String label_password="Password";
    public static final String label_idUser="IdUser";


    private String username;
    private String password;
    private int idUser;
    private String email;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }



    public User(String username, String password, int idUser, String email) {
        this.username = username;
        this.password = password;
        this.idUser = idUser;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", idUser=" + idUser +
                ", email='" + email + '\'' +
                '}';
    }
}
