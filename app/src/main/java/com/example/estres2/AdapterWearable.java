package com.example.estres2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.almacenamiento.entidades.wearable.Wearable;

import java.util.List;

public class AdapterWearable extends RecyclerView.Adapter<AdapterWearable.WearableView> {
    // Creanos un objecto context y un objeto List
    Context context;
    List<Wearable> ListaWearable;

    // Aquí inicializamos el constructor
    public AdapterWearable(Context context, List<Wearable> ListaWearable) {
        this.context = context;
        this.ListaWearable = ListaWearable;
    }

    @NonNull
    @Override
    public WearableView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_wearable, null, false);
        return new WearableView(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull WearableView holder, int position) {
        // Aquí asignamos los valores de las listas a los objetos TextView
        holder.MId.setText(String.format("Id: %s", ListaWearable.get(position).getId()));
        holder.MMac.setText(String.format("Mac: %s", ListaWearable.get(position).getMac()));
    }

    @Override
    public int getItemCount() {
        return ListaWearable.size();
    }

    public static class WearableView extends RecyclerView.ViewHolder {
        // Creamos los objectos TextView para hacer la comunicación
        TextView MId, MMac;

        // En esta función nos permite identificar e interlazar la parte logíca con la parte visual
        public WearableView(@NonNull View itemView) {
            super(itemView);
            MId = itemView.findViewById(R.id.mid);
            MMac = itemView.findViewById(R.id.mmac);
        }
    }
    // ********************** Fin de la clase ListaUsuario ************************ //
}
