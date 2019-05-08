package com.svr.techno;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";
    private EditText email, password;
    private Button signIn;

    @Override
    public void onClick(View v) {

        int content = R.id.content_login_main;

        switch (v.getId()) {
            case R.id.sign_in_button:
                signInProcess();

                break;
            case R.id.sign_in_create_account:
                switchFragment(content, new SignUpFragment());
                break;
            case R.id.sign_in_forget_password:
                //switchFragment(content, new );
                break;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);

        TextView signUp = view.findViewById(R.id.sign_in_create_account);
        TextView forgetPassword = view.findViewById(R.id.sign_in_forget_password);

        signIn = view.findViewById(R.id.sign_in_button);

        signUp.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        signIn.setOnClickListener(this);

        return view;
    }

    private void switchFragment(int id, Fragment fragment) {

        getFragmentManager()
                .beginTransaction()
                .replace(id, fragment)
                .commit();

    }

    private void signInProcess() {

        final String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

        if ((inputEmail.isEmpty()) || (inputPassword.isEmpty())) {
            if (inputEmail.isEmpty()) {
                email.setError("ID cannot blank");
                email.requestFocus();
            }

            if (inputPassword.isEmpty()) {
                password.setError("Password cannot blank");
                password.requestFocus();
            }
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    if (documentSnapshot.exists()) {
                                                        Log.d(TAG, "signInWithEmail:success");
                                                        Toast.makeText(getActivity(), "Authentication success", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                                                        Toast.makeText(getActivity(), "T", Toast.LENGTH_SHORT).show();
                                                        if (documentSnapshot.getString("firstlogin").equals("1")) {
//                                                            Toast.makeText(getActivity(), "T1", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getActivity(), FirstSignInActivity.class));
                                                        } else {
//                                                            Toast.makeText(getActivity(), "T2", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getActivity(), HomeActivity.class));
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "signInWithEmail:error", e);
                                                Toast.makeText(getActivity(), "Authentication error", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }
}
