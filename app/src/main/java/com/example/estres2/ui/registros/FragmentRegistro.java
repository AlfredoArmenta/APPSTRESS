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
import com.example.estres2.Usuario;

import java.io.File;
import java.lang.reflect.Array;
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

        // Paso 1
        List<String> arregloArchivos = obtenerRegistros();
        if (!arregloArchivos.isEmpty()){
            Toast.makeText(mContext, arregloArchivos.get(0), Toast.LENGTH_LONG).show();
            lRegistro.add(new ListaRegistro(arregloArchivos.get(0), R.drawable.ic_registros_menu));
        } else {
            Toast.makeText(mContext, "Carpeta Vacia", Toast.LENGTH_LONG).show();
        }

        // Inflate the layout for this fragment
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

    public List<String> obtenerRegistros() {
        if (!(getActivity() == null)) {
            user = ((MenuPrincipal)getActivity()).MandarUsuario();
            File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + user.getBoleta());
            if (Carpeta.exists()) {
                File[] files = Carpeta.listFiles();
                assert files != null;
                for (int i = 0; i < files.length; i++)
                {
                    //Sacamos del array files un fichero
                    File file = files[i];
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