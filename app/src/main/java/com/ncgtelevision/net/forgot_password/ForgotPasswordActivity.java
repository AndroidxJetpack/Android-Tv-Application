package com.ncgtelevision.net.forgot_password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.utilities.CommonUtility;
import com.ncgtelevision.net.utilities.ConstantUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;
import static com.ncgtelevision.net.utilities.CommonUtility.showToast;

public class ForgotPasswordActivity extends FragmentActivity {
    private String TAG = "ForgotPasswordActivity";
    private EditText email;
    private Button sendButton;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        dialog = custom_loader(this);
        email =(EditText) findViewById(R.id.ed_email);
        sendButton = (Button) findViewById(R.id.btn_sign_in);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmail();
            }
        });
    }

    private void verifyEmail() {
        if (email.getText().toString().equals("")) {
            showToast(this, ConstantUtility.FILL_EMAIL);
        } else if(CommonUtility.isEmailValid(email.getText().toString())){
            sendEmail();
        } else {
            showToast(this, ConstantUtility.EMAIL_VALIDATION);
        }
    }

    private void sendEmail() {
        if (ConstantUtility.isNetworkConnected) {
            dialog.show();
            ForgotPasswordRQBody body = new ForgotPasswordRQBody();
            body.setEmail(email.getText().toString());
            ApiClient.getClient(ForgotPasswordActivity.this).create(ApiInterface.class)
                    .postForgotPasswordCall(body)
                    .enqueue(new Callback<ForgotPasswordModel>() {
                @Override
                public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                    dialog.dismiss();
                    if(response.body().getStatus()){
                        showToast(ForgotPasswordActivity.this, response.body().getMessage());
                        finish();
                    }else{
                        showToast(ForgotPasswordActivity.this, response.body().getMessage());
                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                }
            });
        }else {
            showToast(this, ConstantUtility.CHECK_INTERNET_CONNECTION);
        }
    }


}