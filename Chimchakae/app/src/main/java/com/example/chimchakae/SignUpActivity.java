package com.example.chimchakae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.Iterator;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseAuth firebaseAuth;

    // Email & Password
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCarNum;
    private Button signUpButton;

    private String email = "";
    private String password = "";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    // Firebase Device Token Info
    private String deviceToken;

    private ValueEventListener checkRegister = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
            while(child.hasNext()) {
                if(editTextEmail.getText().toString().equals(child.next().getKey())) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                    databaseReference.removeEventListener(this);
                    return;
                }
            }
            makeNewId();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        deviceToken = FirebaseInstanceId.getInstance().getToken();

        editTextCarNum = findViewById(R.id.et_carNum);
        editTextEmail = findViewById(R.id.et_eamil);
        editTextPassword = findViewById(R.id.et_password);
        signUpButton = findViewById(R.id.btn_signUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp(v);
            }
        });


    }

    public void singUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        System.out.println("진입");
        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
            // 회원가입 성공 이후 firebase rdb에 정보 upload
        }
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);

        }
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

    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            // 성공 이후 rdb에 회원정보 upload
                            databaseReference.addListenerForSingleValueEvent(checkRegister);
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // 로그인
    private void loginUser(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(SignUpActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void makeNewId() {

        String userId = editTextEmail.getText().toString();
        String carNum = editTextCarNum.getText().toString();
        User user = new User(userId, deviceToken, carNum);
        databaseReference.child("users").push().setValue(user);
        //Toast.makeText(getApplicationContext(), "Databsase 등록 성공", Toast.LENGTH_SHORT).show();
    }


}