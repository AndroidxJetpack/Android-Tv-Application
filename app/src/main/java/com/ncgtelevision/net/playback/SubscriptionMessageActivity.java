package com.ncgtelevision.net.playback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.home_screen.HomeActivity;

public class SubscriptionMessageActivity extends FragmentActivity {

    private Button mGoToHomeBtn;
    private ImageView mLogoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_message);
        mGoToHomeBtn = (Button) findViewById(R.id.back_to_home);
        mLogoIV = (ImageView) findViewById(R.id.logo_IB);
        mLogoIV.setFocusable(true);
        mLogoIV.setFocusableInTouchMode(true);
        mLogoIV.requestFocus();
        initializeKeyListener();
    }

    private void initializeKeyListener() {
        mGoToHomeBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if(i== KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
                        Intent intent = new Intent(SubscriptionMessageActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
        mLogoIV.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                mGoToHomeBtn.requestFocus();
            }
            return false;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }
}