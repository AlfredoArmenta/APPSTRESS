package com.example.estres2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.AdapterUsuarios.UsuariosView
import com.example.estres2.almacenamiento.entidades.usuario.Usuario

class AdapterUsuarios(
        var context: Context, var ListaUsuarios: List<Usuario>
) : RecyclerView.Adapter<UsuariosView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosView {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.vista_usuario, null, false)
        return UsuariosView(vista)
    }

    override fun onBindViewHolder(holder: UsuariosView, position: Int) {
        // Aquí asignamos los valores de las listas a los objetos TextView
        holder.MBoleta.text = String.format("Boleta: %s", ListaUsuarios[position].boleta)
        holder.MNombre.text = String.format("Nombre: %s", ListaUsuarios[position].nombre)
        holder.MEdad.text = String.format("Edad: %s", ListaUsuarios[position].edad)
        holder.MGenero.text = String.format("Genero: %s", ListaUsuarios[position].genero)
        holder.MSemestre.text =
                String.format("Semestre: %s", ListaUsuarios[position].semestre)
        holder.MContraseña.text = String.format("Contraseña: %s", ListaUsuarios[position].password)
    }

    override fun getItemCount(): Int {
        return ListaUsuarios.size
    }

    class UsuariosView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Creamos los objectos TextView para hacer la comunicación
        var MBoleta: TextView
        var MNombre: TextView
        var MEdad: TextView
        var MGenero: TextView
        var MSemestre: TextView
        var MContraseña: TextView

        // En esta función nos permite identificar e interlazar la parte logíca con la parte visual
        init {
            MBoleta = itemView.findViewById(R.id.mboleta)
            MNombre = itemView.findViewById(R.id.mnombre)
            MEdad = itemView.findViewById(R.id.medad)
            MGenero = itemView.findViewById(R.id.mgenero)
            MSemestre = itemView.findViewById(R.id.msemestre)
            MContraseña = itemView.findViewById(R.id.mcontraseña)
        }
    } // ********************** Fin de la clase ListaUsuario ************************ //
}