package com.example.masterclassspringsecurity.rest;

import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.masterclassspringsecurity.cache.UserCache;
import com.example.masterclassspringsecurity.domain.User;
import com.example.masterclassspringsecurity.fragment.InformationFragment;
import com.example.masterclassspringsecurity.fragment.LoginFragment;
import com.example.masterclassspringsecurity.fragment.RegistrationFragment;
import com.example.masterclassspringsecurity.rest.mapper.UserMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class AppApiVolley implements AppApi{

    private static final String BASE_URL = "http://192.168.1.43:8081";
    private final Fragment fragment;
    private Response.ErrorListener errorListener;

    public AppApiVolley(Fragment fragment) {
        this.fragment = fragment;
        errorListener = new ErrorListenerImpl();
    }


    @Override
    public void findUserByEmail(String email, String password) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(fragment.requireContext());
        String url = BASE_URL + "/user/" + email;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserCache.setCurrent_user(UserMapper.getFromJson(response, password));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (fragment.getClass().equals(LoginFragment.class))
                            ((LoginFragment) fragment).login();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("s", error.getMessage());
                        if(fragment.getClass().equals(LoginFragment.class))
                            ((LoginFragment) fragment).makeToastBadCredentials();
                        Log.i("API_BAD_CREDENTIALS", email);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                String credentials = email + ":"
                        + password;
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Basic " + Base64.encodeToString(
                        credentials.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    @Override
    public void insertUser(User user) {

        RequestQueue referenceQueue = Volley.newRequestQueue(fragment.requireContext());

        String url = BASE_URL + "/user";
        JSONObject params = new JSONObject();
        try {
            params.put("email", user.getEmail());
            params.put("fullName", user.getFullName());
            params.put("password", user.getPassword());
            params.put("role", user.getRole().name());
        } catch (JSONException e) {
            Log.e("API_TASK_ADD_USER", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserCache.setCurrent_user(user);
                if(fragment.getClass().equals(RegistrationFragment.class))
                    ((RegistrationFragment) fragment).signIn();
                Log.d("API_TEST_ADD_USER", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(fragment.getClass().equals(RegistrationFragment.class))
                    ((RegistrationFragment) fragment).makeToastFailedRegistration();
                Log.i("API_FAILED_REGISTRATION", user.getEmail());
            }
        }
        );
        referenceQueue.add(jsonObjectRequest);

    }

    @Override
    public void getUserInformation() {
        RequestQueue queue = Volley.newRequestQueue(fragment.requireContext());
        String url = BASE_URL + "/information/user";
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        if(fragment.getClass().equals(InformationFragment.class))
                        ((InformationFragment) fragment).setUserInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 403){
                            ((InformationFragment) fragment).setUserInfo("нет доступа");
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersBasic();
            }
        };
        queue.add(jsonObjectRequest);
    }

    @Override
    public void getAdminInformation() {
        RequestQueue queue = Volley.newRequestQueue(fragment.requireContext());
        String url = BASE_URL + "/information/admin";
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        ((InformationFragment) fragment).setAdminInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 403){
                            ((InformationFragment) fragment).setAdminInfo("нет доступа");
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeadersBasic();
            }
        };
        queue.add(jsonObjectRequest);
    }

    private class ErrorListenerImpl implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError e) {
            Log.e("AppApiErrorResponse", e.getMessage());
        }
    }
    private Map<String, String> getHeadersBasic() throws AuthFailureError {
        Map<String, String> headers = new Hashtable<>();
        String credentials = UserCache.getCurrentUser().getEmail() + ":"
                + UserCache.getCurrentUser().getPassword();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + Base64.encodeToString(
                credentials.getBytes(), Base64.NO_WRAP));
        return headers;
    }
}
