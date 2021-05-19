package com.lucasvieira.servicocerto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lucasvieira.servicocerto.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sairSplash();
    }

    private void sairSplash() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 2000);
    }
}