package com.example.brandlancerlead;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.LoginRespons;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userId,userPwd;
    private Button userLogin;

    private SharedPreferences executivePreferences;

    private static final String  FileName = "ExecutiveLogin";
    private static final String  UserName = "ExecutiveId";
    private static final String  IsLogin = "Remember";


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId==R.id.userLoginBtn){


                    String userName = userId.getText().toString().trim();
                    String userPass = userPwd.getText().toString().trim();
                    if(TextUtils.isEmpty(userName)){
                        Toast.makeText(LoginActivity.this,"Enter Valid User ID",Toast.LENGTH_SHORT).show();

                    }else if(TextUtils.isEmpty(userPass)){
                        Toast.makeText(LoginActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();

                    }else{
                        Dialog loader = new Dialog(LoginActivity.this);
                        if(loader.getWindow() != null)
                            loader.getWindow() .setBackgroundDrawableResource(android.R.color.transparent);
                        loader.setContentView(new ProgressBar(LoginActivity.this));
                        loader.setCancelable(false);
                        loader.show();

                        callLoginuser(userName,userPass,loader);
                    }


            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userId = findViewById(R.id.userId);
        userPwd = findViewById(R.id.userPassword);

        userLogin = findViewById(R.id.userLoginBtn);

        userLogin.setOnClickListener(clickListener);

        executivePreferences = getSharedPreferences(FileName,MODE_PRIVATE);
        boolean isRemember = executivePreferences.getBoolean(IsLogin,false);
        if(isRemember){
            enterIntoMain();
        }
    }

    private void callLoginuser(String userName, final String password, final Dialog load) {
        WebContentsInterface webLogin = ServerConnection.createRetrofitConnection(WebContentsInterface.class);

        Call<LoginRespons> loginResponsCall = webLogin.userLoginCall(userName);
        loginResponsCall.enqueue(new Callback<LoginRespons>() {
            @Override
            public void onResponse(Call<LoginRespons> call, Response<LoginRespons> response) {
                if(response.code() == 200){
                    LoginRespons login = response.body();
                    if(login != null && login.isResult()){
                        if(login.getResultset().getPassword().equalsIgnoreCase(password)){
                            dismissLoader(load);
                            loginSuccess(login.getResultset().getExecutiveId());
                        }else{
                            Toast.makeText(LoginActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                            dismissLoader(load);
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"Failed to login with user retry",Toast.LENGTH_SHORT).show();

                        dismissLoader(load);

                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Failed to login with user retry",Toast.LENGTH_SHORT).show();

                    dismissLoader(load);
                }
            }

            @Override
            public void onFailure(Call<LoginRespons> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Failed to login with user retry",Toast.LENGTH_SHORT).show();
                dismissLoader(load);
            }
        });
    }

    private void loginSuccess(String executive) {
        SharedPreferences.Editor loginEditor = executivePreferences.edit();
        loginEditor.putString(UserName,executive);
        loginEditor.putBoolean(IsLogin,true);
        loginEditor.apply();
        enterIntoMain();
    }
    private void  enterIntoMain(){
        Intent mainIn = new Intent(LoginActivity.this,DashBoardActivity.class);
        startActivity(mainIn);
        finish();
    }



    public void dismissLoader(Dialog load){
        userLogin.setEnabled(true);
        if( load != null && load.isShowing())
            load.dismiss();
    }
}
