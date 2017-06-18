package com.firebaseapp.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginCheckActivity extends AppCompatActivity {
    private static final String TAG = "LoginCheckActivity";
    private static final int RC_SIGNIN = 100;

    private Button signInButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);
        signInButton = (Button)findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                Toast.makeText(this, "signed in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            } else {
                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startSignIn();
                        ((Button)v).setText("SIGNING IN...");
                        v.setEnabled(false);
                    }
                });
                signInButton.setEnabled(true);
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "sign in cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "sign in failed: no network", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "sign in failed: unknown error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "unknown sign in response", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "user null", Toast.LENGTH_SHORT).show();
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSignIn();
                    ((Button)v).setText("SIGNING IN...");
                    v.setEnabled(false);
                }
            });
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void startSignIn() {
        Intent authIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .build();
        startActivityForResult(authIntent, RC_SIGNIN);
    }
}
