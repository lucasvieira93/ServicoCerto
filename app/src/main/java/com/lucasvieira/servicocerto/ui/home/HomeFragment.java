package com.lucasvieira.servicocerto.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.activity.DetalhesActivity;
import com.lucasvieira.servicocerto.activity.InserirActivity;
import com.lucasvieira.servicocerto.adapter.ServicosAdapter;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.RecyclerItemClickListener;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;
import com.lucasvieira.servicocerto.model.Servicos;
import com.lucasvieira.servicocerto.model.Usuario;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewHome;
    private ServicosAdapter servicosAdapter;
    private ArrayList<Servicos> listaServicos = new ArrayList<>();
    private DatabaseReference servicosRef;
    private ValueEventListener childEventListenerServicos;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewHome = view.findViewById(R.id.recyclerHome);
        servicosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("servicos");

        //configurar adapter
        servicosAdapter = new ServicosAdapter(listaServicos, getActivity());

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setAdapter(servicosAdapter);

        //FAB --> Novo Serviço
        FloatingActionButton fab = view.findViewById(R.id.fabHome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InserirActivity.class);
                startActivity(i);
            }
        });

        //Configurar evento de clique no recyclerView
        recyclerViewHome.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewHome,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Servicos servicoSelecionado = listaServicos.get(position);
                                Intent i = new Intent(getActivity(), DetalhesActivity.class);
                                i.putExtra("detalheServico", servicoSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getActivity(), "Titulo: " + listaServicos.get(position).getTitulo(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarServicos();
    }

    @Override
    public void onStop() {
        super.onStop();
        servicosRef.removeEventListener(childEventListenerServicos);
    }

    public void recuperarServicos() {
        listaServicos.clear();

        childEventListenerServicos = servicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Servicos servicos = dados.getValue(Servicos.class);
                        listaServicos.add(servicos);

                }

                servicosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}