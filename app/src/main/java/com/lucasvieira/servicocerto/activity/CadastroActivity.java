package com.lucasvieira.servicocerto.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lucasvieira.servicocerto.R;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoSobrenome, campoEmail, campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.textNomeCadastro);
        campoSobrenome = findViewById(R.id.textSobrenomeCadastro);
        campoEmail = findViewById(R.id.textEmailCadastro);
        campoSenha = findViewById(R.id.textSenhaCadastro);
    }

    public void cadastrarUsuario(View view){
        String nome = campoNome.getText().toString();
        String sobrenome = campoSobrenome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if (!nome.isEmpty() && !sobrenome.isEmpty() && !email.isEmpty() && !senha.isEmpty()){
            if (senha.length() < 5){
                Toast.makeText(this, "Insira uma senha com 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(this, nome + " " + sobrenome + " você foi cadastrado!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha os dados!", Toast.LENGTH_SHORT).show();
        }
    }
}