package com.example.brandlancerlead;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText username,password,Confirmpassword;
    Button save;
    ImageView hideShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        Confirmpassword=findViewById(R.id.Confirmpassword);
        save=findViewById(R.id.save);
        hideShow=findViewById(R.id.hideShow);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString())){
                    Confirmpassword.setError("Please Enter Username");

                }else if(TextUtils.isEmpty(password.getText().toString())){
                    Confirmpassword.setError("Please Enter Password");


                }else if(TextUtils.isEmpty(Confirmpassword.getText().toString())){
                    Confirmpassword.setError("Please Enter ConfirmPassword");

                }else {

                    if (password.getText().toString().equals(Confirmpassword.getText().toString())){


                    }else {
                        Confirmpassword.setError("Please Confirm Password");
                    }

                }
            }
        });

        hideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Confirmpassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    Confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Confirmpassword.setSelection(Confirmpassword.getText().length());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground(getResources().getDrawable(R.drawable.ic_action_show));
                    } else {
                        v.setBackgroundResource(R.drawable.ic_action_show);
                    }
                } else {
                    Confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Confirmpassword.setSelection(Confirmpassword.getText().length());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground(getResources().getDrawable(R.drawable.ic_action_hide));
                    } else {
                        v.setBackgroundResource(R.drawable.ic_action_hide);
                    }
                }
            }
        });
    }


}
