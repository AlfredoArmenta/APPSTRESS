package com.example.estres2.ui.registros;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentRegistro extends Fragment {
    private ArrayList<ListaRegistro> lRegistro = new ArrayList<>();
    public Context mContext;
    public Usuario user;
    public List<String> item = new ArrayList<>();

    public FragmentRegistro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<String> arregloArchivos = obtenerRegistros();
        if (!arregloArchivos.isEmpty()){
            for (int i = 0; i <= arregloArchivos.size() - 1; i++ ) {
                lRegistro.add(new ListaRegistro(arregloArchivos.get(i), R.drawable.ic_registros_menu));
            }
        } else {
            Toast.makeText(mContext, "Carpeta Vacia", Toast.LENGTH_LONG).show();
            lRegistro.add(new ListaRegistro("Carpeta Vacia", R.drawable.ic_sin_registro));
        }

        View root = inflater.inflate(R.layout.fragment_registro, container, false);
        RecyclerView mRecyclerView = root.findViewById(R.id.Resgistros);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayaoutManager = new LinearLayoutManager(this.getContext());
        Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> mAdapter = new ListaRegistroAdapter(lRegistro, getContext(), user.getBoleta());
        mRecyclerView.setLayoutManager(mLayaoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(itemDecoration);
        return root;
    }

    public List<String> obtenerRegistros() {
        if (!(getActivity() == null)) {
            user = ((MenuPrincipal)getActivity()).MandarUsuario();
            File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + user.getBoleta());
            if (Carpeta.exists()) {
                File[] files = Carpeta.listFiles();
                assert files != null;
                for (File file : files) {
                    //Sacamos del array files un fichero
                    if(file.getPath().endsWith(".csv"))
                        item.add(file.getName());
                }
                return item;
            } else {
                Toast.makeText(mContext, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("getActivity", "La actividad es nulla");
        }
        item.add("Carpeta vacia");
        return item;
    }
}