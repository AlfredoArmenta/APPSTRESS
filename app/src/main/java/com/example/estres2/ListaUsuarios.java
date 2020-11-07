package com.example.estres2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaUsuarios extends RecyclerView.Adapter<ListaUsuarios.UsuariosView> {
    // Creanos un objecto context y un objeto List
    Context context;
    List<Usuario> ListaUsuarios;

    // Aquí inicializamos el constructor
    public ListaUsuarios(Context context, List<Usuario> ListaUsuarios) {
        this.context = context;
        this.ListaUsuarios = ListaUsuarios;
    }

    @NonNull
    @Override
    public UsuariosView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_usuario, null, false);
        return new UsuariosView(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosView holder, int position) {
        // Aquí asignamos los valores de las listas a los objetos TextView
        holder.MBoleta.setText(String.format("Boleta: %s", ListaUsuarios.get(position).getBoleta()));
        holder.MNombre.setText(String.format("Nombre: %s", ListaUsuarios.get(position).getNombre()));
        holder.MEdad.setText(String.format("Edad: %s", ListaUsuarios.get(position).getEdad()));
        holder.MGenero.setText(String.format("Genero: %s", ListaUsuarios.get(position).getGenero()));
        holder.MSemestre.setText(String.format("Semestre: %s", ListaUsuarios.get(position).getSemestre()));
        holder.MContraseña.setText(String.format("Contraseña: %s", ListaUsuarios.get(position).getContraseña()));
    }

    @Override
    public int getItemCount() {
        return ListaUsuarios.size();
    }

    public static class UsuariosView extends RecyclerView.ViewHolder {
        // Creamos los objectos TextView para hacer la comunicación
        TextView MBoleta, MNombre, MEdad, MGenero, MSemestre, MContraseña;

        // En esta función nos permite identificar e interlazar la parte logíca con la parte visual
        public UsuariosView(@NonNull View itemView) {
            super(itemView);
            MBoleta = itemView.findViewById(R.id.mboleta);
            MNombre = itemView.findViewById(R.id.mnombre);
            MEdad = itemView.findViewById(R.id.medad);
            MGenero = itemView.findViewById(R.id.mgenero);
            MSemestre = itemView.findViewById(R.id.msemestre);
            MContraseña = itemView.findViewById(R.id.mcontraseña);
        }
    }
    // ********************** Fin de la clase ListaUsuario ************************ //
}
