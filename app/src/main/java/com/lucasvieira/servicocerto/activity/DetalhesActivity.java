package com.lucasvieira.servicocerto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.model.Servicos;

public class DetalhesActivity extends AppCompatActivity {

    private TextView campoUsuario, campoTitulo, campoDescricao;
    private ImageView campoImagem;
    private Servicos servicoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(servicoSelecionado.getTitulo());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        campoTitulo = findViewById(R.id.inserirServicoTitulo);
        campoUsuario = findViewById(R.id.inserirServicoUsuario);
        campoDescricao = findViewById(R.id.inserirServicoDescricao);
        campoImagem = findViewById(R.id.inserirServicoImagem);

        //Recuperar dados do serviço
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            servicoSelecionado = (Servicos) bundle.getSerializable("detalheServico");
            campoTitulo.setText(servicoSelecionado.getTitulo());
            campoUsuario.setText(servicoSelecionado.getUsuario());
            campoDescricao.setText(servicoSelecionado.getDescricao());

            String foto = servicoSelecionado.getImagem();
            if (foto != null) {
                Uri url = Uri.parse(servicoSelecionado.getImagem());
                Glide.with(DetalhesActivity.this)
                        .load(url)
                        .into(campoImagem);
            } else {
                campoImagem.setImageResource(R.drawable.background);
            }


        }
    }
}