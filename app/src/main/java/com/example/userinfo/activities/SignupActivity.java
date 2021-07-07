package com.example.userinfo.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userinfo.R;
import com.example.userinfo.UserDao;
import com.example.userinfo.UserDatabase;
import com.example.userinfo.UserEntity;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    private ImageView gallery;
    private EditText dob,name,lastName,address,etPassword,userName,answer;
    private Button btnRegister;
    private Spinner dropDown;
    private boolean isAtLeast8 = false, hasUpperCase = false, isButtonClickable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setUi();
        setProfile();
        setDate();
        securityQuestions();
        buttonRegistration();
    }

    private void buttonRegistration() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserEntity userEntity = new UserEntity();
                userEntity.setName(name.getText().toString());
                userEntity.setlName(lastName.getText().toString());
                userEntity.setDateOfBirth(dob.getText().toString());
                userEntity.setUserName(userName.getText().toString());
                userEntity.setPwd(etPassword.getText().toString());
                userEntity.setAnswers(answer.getText().toString());
                userEntity.setAddress(address.getText().toString());

                if(validateInput(userEntity) && passwordCheck()){
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userDao.rgister(userEntity);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(getApplicationContext(),"Fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean passwordCheck(){
        String password = etPassword.getText().toString();

        if(password.length() >=8 && password.matches("(.*[A-Z].*)")){
            isAtLeast8 = true;
        }else {
            isAtLeast8 = false;
            Toast.makeText(getApplicationContext(),"Passwrd should have 8 characters and 1 uppercase letter",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateInput(UserEntity userEntity){
        if(userEntity.getName().isEmpty() || userEntity.getlName().isEmpty() || userEntity.getDateOfBirth().isEmpty() || userEntity.getPwd().isEmpty()
         || userEntity.getAnswers().isEmpty() || userEntity.getAddress().isEmpty()){
            return false;
        }
        return true;
    }



    private void securityQuestions() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SignupActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.questions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(myAdapter);
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }


    private void setProfile() {
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         switch (requestCode){
             case 2:
                 if(resultCode == RESULT_OK){
                     Bundle bundle = data.getExtras();
                     Bitmap bitmapImage = (Bitmap) bundle.get("data");
                     gallery.setImageBitmap(bitmapImage);
                 }
                 break;
         }
    }

    private void setUi() {
        gallery = findViewById(R.id.galleryimage);
        dob = findViewById(R.id.dob);
        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.addr);
        etPassword = findViewById(R.id.psw);
        btnRegister = findViewById(R.id.register);
        userName = findViewById(R.id.usern);
        dropDown = findViewById(R.id.spinner);
        answer = findViewById(R.id.ans);
    }
}