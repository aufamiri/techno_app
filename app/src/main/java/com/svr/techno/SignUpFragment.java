package com.svr.techno;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    private Button costumer, seller;
    private TextView registerEmail, registerPassword, registerConfirmPassword;
    private int type;

    @Override
    public void onClick(View v) {

        int content = R.id.content_login_main;

        switch (v.getId()) {
            case R.id.sign_up_create_account:
                signUpProcess();
                break;
            case R.id.sign_up_go_to_sign_in:
                switchFragment(content, new SignInFragment());
                break;
            case R.id.sign_up_costumer:
                selectType(0);
                break;
            case R.id.sign_up_seller:
                selectType(1);
                break;
        }

    }

    private void selectType(int input) {
        if (input == 0) {

            if (costumer.getTag().equals(R.drawable.shape_passive_1)) {

                type = 0;
                costumer.setBackgroundResource(R.drawable.shape_active_1);
                seller.setBackgroundResource(R.drawable.shape_passive_1);

                costumer.setTag(R.drawable.shape_active_1);
                seller.setTag(R.drawable.shape_passive_1);

            }

        } else {

            if (seller.getTag().equals(R.drawable.shape_passive_1)) {

                type = 1;
                seller.setBackgroundResource(R.drawable.shape_active_1);
                costumer.setBackgroundResource(R.drawable.shape_passive_1);

                seller.setTag(R.drawable.shape_active_1);
                costumer.setTag(R.drawable.shape_passive_1);

            }


        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        registerEmail = view.findViewById(R.id.sign_up_email);
        registerPassword = view.findViewById(R.id.sign_up_password);
        registerConfirmPassword = view.findViewById(R.id.sign_up_confirm_password);
        TextView sigIn = view.findViewById(R.id.sign_up_go_to_sign_in);

        costumer = view.findViewById(R.id.sign_up_costumer);
        seller = view.findViewById(R.id.sign_up_seller);

        costumer.setTag(R.drawable.shape_active_1);
        seller.setTag(R.drawable.shape_passive_1);

        Button signUp = view.findViewById(R.id.sign_up_create_account);

        costumer.setOnClickListener(this);
        seller.setOnClickListener(this);

        sigIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        type = 0;

        return view;
    }

    private void switchFragment(int id, Fragment fragment) {

        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();

    }

    private void signUpProcess() {

        final String inputEmail = registerEmail.getText().toString().trim();
        String inputPassword = registerPassword.getText().toString().trim();
        String inputConfirmPassword = registerConfirmPassword.getText().toString().trim();

        if (type == -1 || inputEmail.isEmpty() || (inputPassword.isEmpty()) || (inputPassword.length() < 8) || (!(inputPassword.equals(inputConfirmPassword)))) {
            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {

                if (inputEmail.isEmpty()) {
                    registerEmail.setError("Email cannot blank");
                    registerEmail.requestFocus();
                }

                if (inputPassword.isEmpty()) {
                    registerPassword.setError("Password cannot blank");
                    registerPassword.requestFocus();
                }
            }

            if (inputPassword.length() < 8) {
                registerPassword.setError("Panjang password minimal 8 karakter");
                registerPassword.requestFocus();
            }

            if (!(inputPassword.equals(inputConfirmPassword))) {
                registerConfirmPassword.setError("Password Doesn't match");
                registerConfirmPassword.requestFocus();
            }

            if (type == -1) {
                Toast.makeText(getActivity(), "Select your type", Toast.LENGTH_LONG)
                        .show();
            }
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", inputEmail);
                                user.put("type", type);
                                user.put("firstlogin", "1");
                                user.put("username", "");
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startActivity(new Intent(getActivity(), FirstSignInActivity.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Error at database", Toast.LENGTH_LONG).show();
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error create account", e);
                        }
                    });
        }
    }

}
