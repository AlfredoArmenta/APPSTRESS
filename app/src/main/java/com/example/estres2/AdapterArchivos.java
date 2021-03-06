package com.example.estres2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.almacenamiento.entidades.archivo.RegisterFile;

import java.util.List;

public class AdapterArchivos extends RecyclerView.Adapter<AdapterArchivos.ArchivoView> {
    // Creanos un objecto context y un objeto List
    Context context;
    List<RegisterFile> listaRegisterFiles;

    // Aquí inicializamos el constructor
    public AdapterArchivos(Context context, List<RegisterFile> listaRegisterFile) {
        this.context = context;
        this.listaRegisterFiles = listaRegisterFile;
    }

    @NonNull
    @Override
    public ArchivoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, null, false);
        return new ArchivoView(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivoView holder, int position) {
        // Aquí asignamos los valores de las listas a los objetos TextView
        holder.M_Id_archivo.setText(String.format("Id: %s", listaRegisterFiles.get(position).getId()));
        holder.M_Boleta.setText(String.format("Mac: %s", listaRegisterFiles.get(position).getBoleta()));
    }

    @Override
    public int getItemCount() {
        return listaRegisterFiles.size();
    }

    public static class ArchivoView extends RecyclerView.ViewHolder {
        // Creamos los objectos TextView para hacer la comunicación
        TextView M_Id_archivo, M_Boleta;

        // En esta función nos permite identificar e interlazar la parte logíca con la parte visual
        public ArchivoView(@NonNull View itemView) {
            super(itemView);
            M_Id_archivo = itemView.findViewById(R.id.m_id_archivo);
            M_Boleta = itemView.findViewById(R.id.m_boltea_archivo);
        }
    }
}