package com.example.estres2.ui.registros;

import android.annotation.SuppressLint;
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

import androidx.recyclerview.widget.RecyclerView;
import com.example.estres2.R;
import java.util.ArrayList;
import static java.sql.DriverManager.println;

public class ListaRegistroAdapter extends RecyclerView.Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> {
    private Context mContext;
    private static ArrayList<ListaRegistro> mExampleList;

    private static final String TAG = "MyViewHolder";

    public ListaRegistroAdapter(ArrayList<ListaRegistro> exampleList, Context mContext){
            mExampleList = exampleList;
            this.mContext = mContext;
    }

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
        holder.registroImagen.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(mContext, "Hola",Toast.LENGTH_LONG).show();
                        switch (item.getItemId()) {
                            case R.id.action_graficar:
                                //handle menu1 click
                                Log.d(TAG, "onMenuItemClick: action_graficar");
                                break;
                            case R.id.action_analizar:
                                //handle menu2 click
                                break;
                            case R.id.action_ambas:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    //class ListaRegistroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    class ListaRegistroViewHolder extends RecyclerView.ViewHolder {
        public TextView registroTexto;
        public ImageView registroImagen;

        public ListaRegistroViewHolder(View itemView){
            super(itemView);
            registroTexto = itemView.findViewById(R.id.RVText_View);
            registroImagen = itemView.findViewById(R.id.RVEliminar);
            //registroImagen.setOnClickListener(this);
        }
        /*
        @Override
        public void onClick(View v) {
            //println("OnClick: " + getAdapterPosition());
            Log.d(TAG, "onClick: " + getAdapterPosition());
            showPopupMenu(v);
        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu_registros);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d(TAG, "Entro al onMenuItemClick");
            switch (item.getItemId()){
                case R.id.action_graficar:
                    Log.d(TAG, "onMenuItemClick: action_graficar");
                    return true;
                case R.id.action_analizar:
                    Log.d(TAG, "onMenuItemClick: action_analizar");
                    return true;
                case R.id.action_ambas:
                    Log.d(TAG, "onMenuItemClick: action_graficar y analizar");
                    return true;
                case R.id.action_eliminar:
                    Log.d(TAG, "onMenuItemClick: action_eliminar");
                    mExampleList.remove(getAdapterPosition());
                    return true;
                default: Log.d(TAG, "Default");
                    return false;
            }
        }*/
    }

    private static void Eliminar(int registro){
        Log.d(TAG, "onClick: " + registro);
        mExampleList.remove(registro);
    }
}
