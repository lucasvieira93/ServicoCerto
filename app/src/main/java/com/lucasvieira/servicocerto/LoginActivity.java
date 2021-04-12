package com.lucasvieira.servicocerto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private int cont = 0, entrar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }

    public void logarUsuario(View view){
        Toast.makeText(this, "Verifique seu usuário/senha", Toast.LENGTH_SHORT).show();
        cont++;

        if (cont == 3){
            Toast.makeText(this, "ESQUECE, TU NÃO VAI ENTRAR", Toast.LENGTH_SHORT).show();
            cont = 0;
            entrar++;

            if (entrar == 3) {
                Toast.makeText(this, "Entra aí vai...", Toast.LENGTH_SHORT).show();
                entrar = 0;

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
    }
}