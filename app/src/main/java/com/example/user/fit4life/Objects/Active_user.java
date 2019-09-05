package com.example.user.fit4life.Objects;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class Active_user {
    private int ID;
    private int klas_ID;
    private String username;
    private String email;
    private String rol;

    public Active_user(int ID, int klas_ID, String username, String email, String rol, String encoded_A_U) {
        if(encoded_A_U == null) {
            this.ID = ID;
            this.klas_ID = klas_ID;
            this.username = username;
            this.email = email;
            this.rol = rol;
        }
        else {
            byte[] data = Base64.decode(encoded_A_U, Base64.DEFAULT);
            String Active_user = new String(data, StandardCharsets.UTF_8);
            String[] parts = Active_user.split(",");
            this.ID = Integer.parseInt(parts[0]);
            this.klas_ID = Integer.parseInt(parts[1]);
            this.username = parts[2];
            this.email = parts[3];
            this.rol = parts[4];
            System.out.println(Active_user);
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getKlas_ID() {
        return klas_ID;
    }

    public void setKlas_ID(int klas_ID) {
        this.klas_ID = klas_ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getencoded(){
        String text = ID +","+klas_ID+","+username+","+email+","+rol;
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }
}
