package com.example.estres2.ui.registros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.R;

import java.util.ArrayList;

public class ListaRegistroAdapter extends RecyclerView.Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> {
    private ArrayList<ListaRegistro> mExampleList;

    public static class ListaRegistroViewHolder extends RecyclerView.ViewHolder{
        public TextView registroTexto;
        public ImageView registroImagen;

        public ListaRegistroViewHolder(View itemView){
            super(itemView);
            registroTexto = itemView.findViewById(R.id.RVText_View);
            registroImagen = itemView.findViewById(R.id.RVEliminar);

        }
    }

    public ListaRegistroAdapter(ArrayList<ListaRegistro> exampleList){
            mExampleList = exampleList;
    }

    @Override
    public ListaRegistroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_registros, parent, false);
        ListaRegistroViewHolder lvh = new ListaRegistroViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder( ListaRegistroAdapter.ListaRegistroViewHolder holder, int position) {
        ListaRegistro currentRegistro = mExampleList.get(position);
        holder.registroImagen.setImageResource(currentRegistro.getImagenEliminar());
        holder.registroTexto.setText(currentRegistro.getNombreRegistro());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
