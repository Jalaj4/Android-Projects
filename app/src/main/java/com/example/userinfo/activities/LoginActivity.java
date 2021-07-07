package com.example.userinfo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userinfo.R;
import com.example.userinfo.UserDao;
import com.example.userinfo.UserDashboard;
import com.example.userinfo.UserDatabase;
import com.example.userinfo.UserEntity;

public class LoginActivity extends AppCompatActivity {

    EditText userName,password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        userLogin();
    }

    private void userLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameText = userName.getText().toString();
                String passwordText = password.getText().toString();
                if(userNameText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
                }else{
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UserEntity userEntity = userDao.login(userNameText,passwordText);
                            if(userEntity == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Invalid Credintials",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                                startActivity(intent);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void initUi() {
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login1);
    }
}