package com.example.estres2.ui.registros;

import android.content.Context;
import android.os.Environment;
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
import com.example.estres2.almacenamiento.database.DB;

import java.io.File;
import java.util.ArrayList;

public class ListaRegistroAdapter extends RecyclerView.Adapter<ListaRegistroAdapter.ListaRegistroViewHolder> {
    public int position;
    public Context mContext;
    public String Boleta;
    public static ArrayList<ListaRegistro> mExampleList;
    private static final String TAG = "MyViewHolder";

    public ListaRegistroAdapter(ArrayList<ListaRegistro> exampleList, Context mContext, String boletaUsuario) {
        mExampleList = exampleList;
        this.mContext = mContext;
        Boleta = boletaUsuario;
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
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    class ListaRegistroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView registroTexto;
        public ImageView registroImagen;

        public ListaRegistroViewHolder(View itemView) {
            super(itemView);
            registroTexto = itemView.findViewById(R.id.RVText_View);
            registroImagen = itemView.findViewById(R.id.RVEliminar);
            if (!mExampleList.get(0).getNombreRegistro().equals("Carpeta Vacia"))
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
                    int c = eliminarRegistro(position);

                    if(c <= 1){
                        registroImagen.setImageResource(R.drawable.ic_sin_registro);
                        registroImagen.setOnClickListener(null);
                        registroTexto.setText("Carpeta Vacia");
                    }else{removeAt(position);}
                    return true;
                default:
                    Log.d(TAG, "Default");
                    return false;
            }
        }
    }

    private int eliminarRegistro(int position) {
        int conteo = 0;
        int cuentacsv = 0;
        DB db = new DB(mContext);

        File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + Boleta);
        if (Carpeta.exists()) {
            File[] files = Carpeta.listFiles();
            assert files != null;
                for(int i=0; i<files.length; i++) {
                    if(files[i].getPath().endsWith(".csv")) {
                        cuentacsv++;
                        if (i == position + conteo){
                            if(db.BorrarArchivo(files[i].getName()) > 0){
                                Toast.makeText(mContext, "Se elimino registro correctamente", Toast.LENGTH_LONG).show();
                                files[i].delete();
                            }
                        }
                    } else {
                        conteo++;
                    }
                }
            } else {
            Toast.makeText(mContext, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show();
        }
        return cuentacsv;
    }

    public void removeAt(int position) {
        mExampleList.remove(position);
        notifyDataSetChanged();
    }
}
