package com.ncgtelevision.net.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.forgot_password.ForgotPasswordActivity;
import com.ncgtelevision.net.home_screen.HomeActivity;
import com.ncgtelevision.net.local_storage.TokenStorage;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.utilities.CommonUtility;
import com.ncgtelevision.net.utilities.ConstantUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;

public class SignInActivity  extends FragmentActivity {

    private String TAG = SignInActivity.class.getSimpleName();
    private EditText edEmail,edPass;
    private CheckBox checkBox;
    private Button btnSignIn;
    private TextView btnForgotPassword;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        dialog = custom_loader(SignInActivity.this);
        initviews();

//        /todo This is credentials for testing purpose to be harcoded
//        edEmail!!.setText("testbpract")
//        edEmail.setText("arshad");
//        edPass.setText("12345678");
//        edEmail.setText("AnnaNCG");
//        edPass.setText("123456");

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConstantUtility.isNetworkConnected){
                    if (edEmail.getText().toString().matches("")){
                        CommonUtility.showToast(SignInActivity.this,ConstantUtility.FILL_EMAIL);

                    } else if (edPass.getText().toString().matches("")){
                        CommonUtility.showToast(SignInActivity.this, ConstantUtility.FILL_PASSW);

                    }else {
                        signInCall();
                    }

                }else {
                    CommonUtility.showToast(SignInActivity.this,ConstantUtility.CHECK_INTERNET_CONNECTION);
                }
            }
        });

    }

    private void signInCall() {
       dialog.show();
       SendSignInBody jsonnBody=new SendSignInBody();
       jsonnBody.setUsername(edEmail.getText().toString());
       jsonnBody.setPassword(edPass.getText().toString());

        ApiInterface apiInterface= ApiClient.getSigInClient().create(ApiInterface.class);
        Call<SignInModel> call=apiInterface.postSignInCall(jsonnBody);



        call.enqueue(new Callback<SignInModel>() {
            @Override
            public void onResponse(Call<SignInModel> call, Response<SignInModel> response) {
                try {
                    Log.e(TAG, "resCode "+response.code());

                    if (response.isSuccessful() && response.body()!=null){
                       if (response.body().getSuccess()) {
                           if(response.body().isStatus()) {
                               CommonUtility.showToast(SignInActivity.this, response.body().getMessage());
                               List<Datum> datumList = response.body().getData();
                               if (datumList.size() > 0) {
                                   String accessToken = datumList.get(0).getToken();
                                   TokenStorage.savedSharedToken(SignInActivity.this, accessToken);
                                   startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                   finish();
                                   edEmail.setText("");
                                   edPass.setText("");
                               }
                           }
                       }else{
                           CommonUtility.showToast(SignInActivity.this, response.body().getMessage());
                           List<Datum> datumList = response.body().getData();
                           if (datumList.size() > 0) {
                               String accessToken = datumList.get(0).getToken();
                               TokenStorage.savedSharedToken(SignInActivity.this, accessToken);
                               startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                               finish();
                               edEmail.setText("");
                               edPass.setText("");
                           }
//                           CommonUtility.showToast(SignInActivity.this,response.body().getMessage());
                    }

                    }else if (response.code()==500){
                        CommonUtility.showToast(SignInActivity.this,ConstantUtility.SERVER_ISSUE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,"Exception: "+e.toString());
                } dialog.dismiss();
                Log.e(TAG,"ResponCode "+response.code()+"");
            }

            @Override
            public void onFailure(Call<SignInModel> call, Throwable t) {
                Log.e(TAG,"failure: "+t.getMessage());
                Log.e(TAG,"failRequest: "+call.request());
                CommonUtility.showToast(SignInActivity.this,ConstantUtility.SERVER_ISSUE);
                dialog.dismiss();
            }
        });


    }

    private void initviews() {
        edEmail = findViewById(R.id.ed_email);
        edPass = findViewById(R.id.ed_password);
        checkBox = findViewById(R.id.checkbox);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnForgotPassword = findViewById(R.id.text_forgot_pass);
    }

}
