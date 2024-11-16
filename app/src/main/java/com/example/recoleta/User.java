package com.example.recoleta;

public class User {
    private String _id;
    private String firstName;
    private String lastName;
    private String email;
    private String userType;
    private boolean isAdmin;
    private String createdAt;
    private String updatedAt;

    public User(String _id, String updatedAt, String createdAt, boolean isAdmin, String userType, String email, String lastName, String firstName) {
        this._id = _id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.isAdmin = isAdmin;
        this.userType = userType;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
