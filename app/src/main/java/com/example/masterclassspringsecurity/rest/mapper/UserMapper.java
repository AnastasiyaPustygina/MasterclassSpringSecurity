package com.example.masterclassspringsecurity.rest.mapper;

import com.example.masterclassspringsecurity.domain.Role;
import com.example.masterclassspringsecurity.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMapper {

    public static User getFromJson(JSONObject jsonObject, String password) throws JSONException {
        return new User(jsonObject.getString("email"),
                jsonObject.getString("fullName"), password,
                Role.valueOf(jsonObject.getString("role")));
    }
}
