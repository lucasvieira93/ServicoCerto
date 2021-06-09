package com.lucasvieira.servicocerto.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucasvieira.servicocerto.R;
import com.lucasvieira.servicocerto.activity.InserirActivity;
import com.lucasvieira.servicocerto.activity.MainActivity;
import com.lucasvieira.servicocerto.adapter.ServicoAdapter;
import com.lucasvieira.servicocerto.config.ConfiguracaoFirebase;
import com.lucasvieira.servicocerto.helper.UsuarioFirebase;
import com.lucasvieira.servicocerto.model.Servico;
import com.lucasvieira.servicocerto.model.Usuario;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewHome;
    private ServicoAdapter servicoAdapter;
    private List<Servico> listaServico = new ArrayList<>();
    private ChildEventListener childEventListenerServicos;
    private DatabaseReference database;
    private DatabaseReference servicosRef;
    private Usuario usuarioLogado;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewHome = view.findViewById(R.id.recyclerHome);
        servicosRef = ConfiguracaoFirebase.getFirebaseDatabase().child("servicos");
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        FloatingActionButton fab = view.findViewById(R.id.fabHome);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InserirActivity.class);
                startActivity(i);
            }
        });

        //configurar adapter
        servicoAdapter = new ServicoAdapter(listaServico, getActivity());

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setAdapter(servicoAdapter);


        //Configura servicosRef
//        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        database = ConfiguracaoFirebase.getFirebaseDatabase();
        servicosRef = database.child("Servicos");

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
        listaServico.clear();

        childEventListenerServicos = servicosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Servico servico = snapshot.getValue(Servico.class);
                listaServico.add(servico);
                servicoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}