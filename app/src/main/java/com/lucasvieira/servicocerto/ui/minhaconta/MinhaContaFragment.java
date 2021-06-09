package com.lucasvieira.servicocerto.ui.minhaconta;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.Permissao;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;
import com.lucasvieira.servicocerto.model.Usuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class MinhaContaFragment extends Fragment {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private EditText editPerfilNome, editPerfilEmail;
    private Button botãoSalvar;
    private Usuario usuarioLogado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minha_conta, container, false);

        //Configurações Iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        imageButtonCamera = view.findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = view.findViewById(R.id.imageButtonFoto);
        circleImageViewPerfil = view.findViewById(R.id.fotoPerfil);
        editPerfilNome = view.findViewById(R.id.textNomeConta);
        editPerfilEmail = view.findViewById(R.id.textEmailConta);
        botãoSalvar = view.findViewById(R.id.botaoSalvarConta);

        //Recuperar dados do usuário
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if (url != null) {

            Glide.with(getActivity())
                    .load(url)
                    .into(circleImageViewPerfil);

        } else {
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }

        //Recuperar nome usuario
        editPerfilNome.setText(usuario.getDisplayName());
        editPerfilEmail.setText(usuario.getEmail());

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, (MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        botãoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarPerfilNome();
            }
        });

        //Evento para atualizar nome pressionando ENTER
        editPerfilNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        atualizarPerfilNome();
                    }

                return true;
            }
        });

        return view;
    }

    //Recuperar foto de perfil
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {
                    circleImageViewPerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no Firebase
                    StorageReference imageRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");

                    //Retorna objeto que irá controlar o upload
                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //recuperando foto
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);
                                }
                            });
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "Erro ao selecionar foto!", Toast.LENGTH_SHORT).show();
        }
    }

    //metodos do ConfiguraçõesActivity
    public void atualizaFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);

        if (retorno) {
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();

            Toast.makeText(getActivity(), "Sua foto foi alterada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidaçãoPermissao();
            }
        }
    }

    private void alertaValidaçãoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void atualizarPerfilNome() {
        String nome = editPerfilNome.getText().toString();
        boolean retorno = UsuarioFirebase.atualizarNomeUsuario(nome);

        if (retorno) {
            usuarioLogado.setNome(nome);
            usuarioLogado.atualizar();

            Toast.makeText(getActivity(), "Nome atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
}