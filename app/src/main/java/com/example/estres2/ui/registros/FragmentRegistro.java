package com.example.estres2.ui.registros;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.estres2.R;

import java.util.ArrayList;

public class FragmentRegistro extends Fragment {
    private ArrayList<ListaRegistro> lRegistro = new ArrayList<>();
    public Context mContext;

    public FragmentRegistro() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "Estoy en onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lRegistro.add(new ListaRegistro("Registro1", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro2", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro3", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro4", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro5", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro6", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro7", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro8", R.drawable.ic_registros_menu));
        lRegistro.add(new ListaRegistro("Registro9", R.drawable.ic_registros_menu));

        View root = inflater.inflate(R.layout.fragment_registro, container, false);
        RecyclerView mRecyclerView = root.findViewById(R.id.Resgistros);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayaoutManager = new LinearLayoutManager(this.getContext());
        Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> mAdapter = new ListaRegistroAdapter(lRegistro, getContext());
        mRecyclerView.setLayoutManager(mLayaoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(itemDecoration);
        return root;
    }
}