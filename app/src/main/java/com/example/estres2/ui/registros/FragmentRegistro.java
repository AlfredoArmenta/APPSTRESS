package com.example.estres2.ui.registros;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.estres2.R;

import java.util.ArrayList;

public class FragmentRegistro extends Fragment {

    View root;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayaoutManager;
    private Drawable mDivider;


    public FragmentRegistro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<ListaRegistro> lRegistro = new ArrayList<>();

        lRegistro.add(new ListaRegistro("Registro1", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro2", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro3", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro4", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro5", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro6", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro7", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro8", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro9", R.drawable.ic_sin_registro));
        lRegistro.add(new ListaRegistro("Registro10", R.drawable.ic_sin_registro));

        root = inflater.inflate(R.layout.fragment_registro, container, false);
        mRecyclerView = root.findViewById(R.id.Resgistros);
        mRecyclerView.setHasFixedSize(true);
        mLayaoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ListaRegistroAdapter(lRegistro, getContext());
        mRecyclerView.setLayoutManager(mLayaoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(itemDecoration);

        return root;
    }
}