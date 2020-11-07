package com.example.estres2.ui.registros;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.R;

import java.util.ArrayList;

public class ListaRegistroAdapter extends RecyclerView.Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> {
    private FragmentRegistro fragmento = new FragmentRegistro();
    public int position;
    public Context mContext;
    public static ArrayList<ListaRegistro> mExampleList;
    private static final String TAG = "MyViewHolder";

    public ListaRegistroAdapter(ArrayList<ListaRegistro> exampleList, Context mContext) {
        mExampleList = exampleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ListaRegistroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_registros, parent, false);
        return new ListaRegistroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListaRegistroAdapter.ListaRegistroViewHolder holder, int position) {
        ListaRegistro currentRegistro = mExampleList.get(position);
        holder.registroImagen.setImageResource(currentRegistro.getImagenEliminar());
        holder.registroTexto.setText(currentRegistro.getNombreRegistro());
        /*holder.registroImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.registroImagen);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_registros);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_graficar:
                                //handle menu1 click
                                Toast.makeText(mContext, "Estoy en graficar",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.action_analizar:
                                //handle menu2 click
                                Toast.makeText(mContext, "Estoy en analizar",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.action_ambas:
                                //handle menu3 click
                                Toast.makeText(mContext, "Estoy en ambas",Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    class ListaRegistroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        //class ListaRegistroViewHolder extends RecyclerView.ViewHolder {
        public TextView registroTexto;
        public ImageView registroImagen;

        public ListaRegistroViewHolder(View itemView) {
            super(itemView);
            registroTexto = itemView.findViewById(R.id.RVText_View);
            registroImagen = itemView.findViewById(R.id.RVEliminar);
            registroImagen.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //println("OnClick: " + getAdapterPosition());
            Log.d(TAG, "onClick: " + getAdapterPosition());
            position = getAdapterPosition();
            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu_registros);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(TAG, "Entro al onMenuItemClick");
            switch (item.getItemId()) {
                case R.id.action_graficar:
                    //Toast.makeText(mContext, "Estoy en Graficar",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onMenuItemClick: action_graficar");
                    return true;
                case R.id.action_analizar:
                    //Toast.makeText(mContext, "Estoy en Analizar",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onMenuItemClick: action_analizar");
                    return true;
                case R.id.action_ambas:
                    //Toast.makeText(mContext, "Estoy en Graficar y Analizar",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onMenuItemClick: action_graficar y analizar");
                    return true;
                case R.id.action_eliminar:
                    //Toast.makeText(mContext, "Estoy en Eliminar",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onMenuItemClick: action_eliminar");
                    removeAt(position);
                    return true;
                default:
                    Log.d(TAG, "Default");
                    return false;
            }
        }
    }

    public void removeAt(int position) {
        mExampleList.remove(position);
        notifyDataSetChanged();
    }
}
