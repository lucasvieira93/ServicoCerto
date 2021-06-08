package com.lucasvieira.servicocerto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private View hearderView;
    private FirebaseAuth autenticacao;
    private FirebaseUser usuarioAtual;

    private TextView navNome;
    private TextView navEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        navigationView = findViewById(R.id.nav_view);
        hearderView = navigationView.getHeaderView(0);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        navNome = hearderView.findViewById(R.id.textNavNome);
        navEmail = hearderView.findViewById(R.id.textNavEmail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_conta, R.id.nav_chat, R.id.nav_ajuda, R.id.nav_termos)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Setando configurações do usuário
        String nomeUsuarioAtual = usuarioAtual.getDisplayName();
        String emailUsuarioAtual = usuarioAtual.getEmail();

        navNome.setText(nomeUsuarioAtual);
        navEmail.setText(emailUsuarioAtual);
    }

    //layout mais opções
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mais_opcoes, menu);
        return true;
    }

    //botões mais opções
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Botão de mais opções, adicionar no switch abaixo

        switch (item.getItemId()) {

            case R.id.botao_logout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                //Configura título e msg
                dialog.setTitle("Logout");
                dialog.setMessage("Deseja realmente sair?");
                dialog.setIcon(R.drawable.ic_logout_24);

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deslogarUsuario();
                        Toast.makeText(MainActivity.this, "Até logo!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                dialog.setNegativeButton("Não", null);
                dialog.create();
                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}