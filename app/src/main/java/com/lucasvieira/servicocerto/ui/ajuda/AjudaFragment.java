package com.lucasvieira.servicocerto.ui.ajuda;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.activity.InserirActivity;

public class AjudaFragment extends Fragment {

    public AjudaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ajuda, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fabAjuda);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarEmail();
            }
        });

        return view;

    }

    private void enviarEmail() {

        //configurando intent para email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"servicocertosc@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ajuda - Serviço Certo");
        intent.putExtra(Intent.EXTRA_TEXT, "(Insira o motivo do contato de forma objetiva!)");

        intent.setType("message/rfc822");

        startActivity(Intent.createChooser(intent, "Ajuda via email"));

    }

}