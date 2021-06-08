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
import com.lucasvieira.servicocerto.model.Servico;

import java.util.List;

public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.MyViewHolder> {

    private List<Servico> listaServicos;
    private Context context;

    public ServicoAdapter(List<Servico> listaServicos, Context c) {
        this.listaServicos = listaServicos;
        this.context = c;
    }

    @NonNull
    @Override
    public ServicoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_servico, parent, false);
        return new MyViewHolder(layoutLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicoAdapter.MyViewHolder holder, int position) {

        Servico servico = listaServicos.get(position);

        holder.tituloServico.setText(servico.getTitulo());
        holder.usuarioServico.setText(servico.getUsuario());
        holder.descricaoServico.setText(servico.getDescricao());

        if (servico.getImagem() != null){
            Uri uri = Uri.parse(servico.getImagem());
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
            usuarioServico = itemView.findViewById(R.id.detalheServicoUsuario);
            descricaoServico = itemView.findViewById(R.id.listaServicoDescricao);
            imagemServico = itemView.findViewById(R.id.listaServicoImagem);
        }
    }
}
