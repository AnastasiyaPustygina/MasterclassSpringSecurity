package com.example.masterclassspringsecurity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.masterclassspringsecurity.R;
import com.example.masterclassspringsecurity.domain.Role;
import com.example.masterclassspringsecurity.domain.User;
import com.example.masterclassspringsecurity.rest.AppApiVolley;

public class RegistrationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        EditText et_email = view.findViewById(R.id.et_registration_email);
        EditText et_fullName = view.findViewById(R.id.et_registration_full_name);
        EditText et_password = view.findViewById(R.id.et_registration_password);
        AppCompatButton bt_signUp = view.findViewById(R.id.bt_registration_sign_up);
        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AppApiVolley(RegistrationFragment.this).insertUser(
                        new User(et_email.getText().toString(), et_fullName.getText().toString(),
                                et_password.getText().toString(), Role.user));
            }
        });
        return view;
    }
    public void signIn(){
        NavHostFragment.findNavController(RegistrationFragment.this)
                .navigate(R.id.action_registrationFragment_to_informationFragment);
    }
    public void makeToastFailedRegistration(){
        Toast.makeText(getContext(), "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
    }
}
