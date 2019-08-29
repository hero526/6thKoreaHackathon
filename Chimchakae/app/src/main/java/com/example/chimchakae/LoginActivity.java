package com.example.chimchakae;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private String email;
    private String password;

    private Button signUpButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxAutoLogin;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    // pwd regular expression
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    // Firebase obj for Authentication
    private FirebaseAuth firebaseAuth;

    // auto login obj
    private SharedPreferences auto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        signUpButton = findViewById(R.id.signUpBtn);
        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        checkBoxAutoLogin = findViewById(R.id.cb_autoLogin);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        autoLogin();

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editTextEmail.getText().toString().equals("") && editTextPassword.getText().toString().equals("")) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }
        };
        editTextEmail.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
 /*       passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(v);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    private void autoLogin() {
            String loginId = auto.getString("inputId", null);
            String loginPwd = auto.getString("inputPwd",null);

            if(loginId != null && loginPwd != null) {
                    Toast.makeText(LoginActivity.this, loginId + "계정으로 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
            }
    }

    // 로그인
    private void loginUser(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            if(checkBoxAutoLogin.isChecked()) {
                                System.out.println("체크박스");
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", email);
                                autoLogin.putString("inputPwd", password);
                                autoLogin.commit();
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }


}
