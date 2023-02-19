package com.example.masterclassspringsecurity.rest;

import com.example.masterclassspringsecurity.domain.User;

import org.json.JSONException;

public interface AppApi {

    void findUserByEmail(String email, String password) throws JSONException;

    void insertUser(User user);

    void getUserInformation();

    void getAdminInformation();

}
