package com.example.chimchakae;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chimchakae.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

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
    private ProgressDialog progressDialog;

    // pwd regular expression
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    // Firebase obj for Authentication
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;
    private String deviceToken;

    // auto login obj
    private SharedPreferences auto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        // Firebase RDB instance request
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        deviceToken = FirebaseInstanceId.getInstance().getToken();

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        signUpButton = findViewById(R.id.signUpBtn);
        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        checkBoxAutoLogin = findViewById(R.id.cb_autoLogin);
        loginButton = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);

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
        progressDialog.setMessage("로그인 중 입니다.. 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                            if(checkBoxAutoLogin.isChecked()) {
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", email);
                                autoLogin.putString("inputPwd", password);
                                autoLogin.commit();
                            }
                            checkFCMToken();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
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

    private void checkFCMToken() {

        String key = mDatabase.child("users").push().getKey();
        final String userEmail = editTextEmail.getText().toString();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren()) {
                    // user 정보 가져오기
                    User user = messageData.getValue(User.class);
                    // 매치가 되어서 같으면 Token 정보를 바꾼다.
                    if(userEmail.equals(user.getUserId())) {
                        User updateUser = new User(user.getUserId(), deviceToken, user.getCarNum());
                        mDatabase.child(messageData.getKey()).removeValue();
                        mDatabase.push().setValue(updateUser);
                        mDatabase.removeEventListener(this);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Fail", "Fialed to read value");
            }
        });
    }


}
