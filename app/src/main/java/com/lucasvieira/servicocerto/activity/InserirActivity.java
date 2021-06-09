package com.lucasvieira.servicocerto.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.Base64Custom;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;
import com.lucasvieira.servicocerto.model.Servico;
import com.lucasvieira.servicocerto.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class InserirActivity extends AppCompatActivity {

    private EditText campoTitulo, campoDescricao;
    private TextView campoUsuario;
    private ImageView campoImagem;

    private FirebaseUser usuarioAtual;
    private StorageReference storage;

    private static final int SELECAO_CAMERA = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserir_servico);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        campoTitulo = findViewById(R.id.inserirServicoTitulo);
        campoUsuario = findViewById(R.id.inserirServicoUsuario);
        campoDescricao = findViewById(R.id.inserirServicoDescricao);
        campoImagem = findViewById(R.id.inserirServicoImagem);

        usuarioAtual = UsuarioFirebase.getUsuarioAtual();
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        campoUsuario.setText(usuarioAtual.getDisplayName());

        //TODO - evento de clique da foto

//        campoImagem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (i.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(i, SELECAO_CAMERA);
//                }
//            }
//        });

    }

    public void validarServico(View view) {

        String textoTitulo = campoTitulo.getText().toString();
        String textoUsuario = usuarioAtual.getDisplayName();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoTitulo.isEmpty() || !textoDescricao.isEmpty()) {
            Servico servico = new Servico();
            servico.setTitulo(textoTitulo);
            servico.setDescricao(textoDescricao);
            servico.setUsuario(textoUsuario);

            cadastrarServico(servico);

        } else {
            Toast.makeText(this, "Preencha os campos!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cadastrarServico(Servico servico) {

        try {
            String identificadorUsuario = Base64Custom.codificarBase64(servico.getUsuario());
            servico.setId(identificadorUsuario);
            servico.salvar();


        } catch (Exception e) {
            e.getMessage();
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if (imagem != null) {

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Salvar imagem no Firebase
                    StorageReference imageRef = storage
                            .child("imagens")
                            .child("servicos")
                            .child(usuarioAtual.getEmail())
                            .child(nomeImagem + ".jpeg");

                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                            Toast.makeText(InserirActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();

                                    Servico servico = new Servico();
                                    servico.setImagem(url.toString());
                                    servico.salvar();

//                                    Toast.makeText(InserirActivity.this, "Imagem inserida!", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}