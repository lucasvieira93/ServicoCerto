package com.lucasvieira.servicocerto.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Servicos implements Serializable {

    private String id;
    private String titulo;
    private String usuario;
    private String descricao;
    private String imagem;

    public Servicos() {
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference servicos = firebaseRef.child("servicos")
                .child(usuario + "-" + getId());

        servicos.setValue(this);
    }

    public void atualizar() {
        String identificadorServico = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference servicoRef = database.child("servico").child(identificadorServico);

        Map<String, Object> valoresServico = converterParaMap();

        servicoRef.updateChildren(valoresServico);
    }

    @Exclude
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> servicoMap = new HashMap<>();
        servicoMap.put("titulo", getTitulo());
        servicoMap.put("usuario", getUsuario());
        servicoMap.put("descricao", getDescricao());
        servicoMap.put("imagem", getImagem());

        return servicoMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
