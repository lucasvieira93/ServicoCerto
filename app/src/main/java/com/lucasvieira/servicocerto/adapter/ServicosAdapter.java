package com.lucasvieira.servicocerto.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.model.Servicos;

import java.util.List;

public class ServicosAdapter extends RecyclerView.Adapter<ServicosAdapter.MyViewHolder> {

    private List<Servicos> listaServicos;
    private Context context;

    public ServicosAdapter(List<Servicos> listaServicos, Context c) {
        this.listaServicos = listaServicos;
        this.context = c;
    }

    @NonNull
    @Override
    public ServicosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_servico, parent, false);
        return new MyViewHolder(layoutLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicosAdapter.MyViewHolder holder, int position) {

        Servicos servicos = listaServicos.get(position);

        holder.tituloServico.setText(servicos.getTitulo());
        holder.usuarioServico.setText(servicos.getUsuario());
        holder.descricaoServico.setText(servicos.getDescricao());

        if (servicos.getImagem() != null){
            Uri uri = Uri.parse(servicos.getImagem());
            Glide.with(context).load(uri).into(holder.imagemServico);
        } else {
            holder.imagemServico.setImageResource(R.drawable.ic_servico);
        }

    }

    @Override
    public int getItemCount() {
            return this.listaServicos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tituloServico;
        private TextView usuarioServico;
        private TextView descricaoServico;
        private ImageView imagemServico;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloServico = itemView.findViewById(R.id.listaServicoTitulo);
            usuarioServico = itemView.findViewById(R.id.listaServicoUsuario);
            descricaoServico = itemView.findViewById(R.id.listaServicoDescricao);
            imagemServico = itemView.findViewById(R.id.listaServicoImagem);
        }
    }
}
