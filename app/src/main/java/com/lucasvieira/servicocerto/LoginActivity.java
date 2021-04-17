package com.lucasvieira.servicocerto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void logarUsuario(View view){

        if (cont == 3){
                Toast.makeText(this, "Entra aí vai...", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);

                cont = 0;
        }

        Toast.makeText(this, "Verifique seu usuário/senha", Toast.LENGTH_SHORT).show();
        cont++;
    }

    public void cadastrarUsuario(View view){
        Intent i = new Intent(this, CadastroActivity.class);
        startActivity(i);
    }
}