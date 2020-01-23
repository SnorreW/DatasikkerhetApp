package com.androidigniter.loginandregistration;

import java.util.Date;

/**
 * Created by AndroidIgniter on 23 Mar 2019 020.
 */

public class User {
    String email;
    String fullName;
    Date sessionExpiryDate;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
