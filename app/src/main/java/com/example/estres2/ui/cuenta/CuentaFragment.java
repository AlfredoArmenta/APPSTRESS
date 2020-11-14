package com.example.estres2.ui.cuenta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;

public class CuentaFragment extends Fragment {
    private Usuario user;
    private TextView Boleta;
    private EditText Nombre;
    private EditText Edad;
    private RadioButton Masculino;
    private RadioButton Femenino;
    private Spinner Semestre;
    private EditText Contraseña;
    private TextView Numero;
    private TextView CaracterEspecial;
    private TextView Mayuscula;
    private TextView Minuscula;
    private TextView Longitud;
    private Button Aplicar;
    private Context mContext;

    public CuentaFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        iniciarObjetos(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    public void iniciarObjetos(View root) {
        if ((getActivity() == null)) {
            requireActivity().finish();
        } else {
            // Obtenemos el usuario con todos sus parametros para llenar los campos
            user = ((MenuPrincipal) getActivity()).MandarUsuario();
        }
        // Creamos el vinculo de la parte visual con la parte lógica
        Boleta = (TextView) root.findViewById(R.id.CFBoleta);
        Nombre = (EditText) root.findViewById(R.id.CFNombre);
        Edad = (EditText) root.findViewById(R.id.CFEdad);
        Masculino = (RadioButton) root.findViewById(R.id.CFMasculino);
        Femenino = (RadioButton) root.findViewById(R.id.CFFemenino);
        Semestre = (Spinner) root.findViewById(R.id.CFSemestre);
        Contraseña = (EditText) root.findViewById(R.id.CFContraseña);

        // String que nos ayudan a llenar a los spinners que sirven para la selección del semestre y la materia
        String[] semestre = {"Selecciona tu semestre actual", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        ArrayAdapter<String> AdapterSemestre = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, semestre);
        Semestre.setAdapter(AdapterSemestre);

        // Elementos de texto que nos proporcionan información sobre los parametros de la contraseña
        Numero = (TextView) root.findViewById(R.id.CFNumero);
        CaracterEspecial = (TextView) root.findViewById(R.id.CFCaracterEspecial);
        Mayuscula = (TextView) root.findViewById(R.id.CFMayuscula);
        Minuscula = (TextView) root.findViewById(R.id.CFMinuscula);
        Longitud = (TextView) root.findViewById(R.id.CFLongitud);
        Aplicar = (Button) root.findViewById(R.id.CFAplicar);
        Aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarDatos();
            }
        });

        // Llenamos los campos con los valores del usuario, para que pueda modificarlos
        Boleta.setText(user.getBoleta());
        Nombre.setText(user.getNombre());
        Edad.setText(user.getEdad());

        if (user.getGenero().equals("Masculino")) {
            Masculino.setChecked(true);
        } else {
            Femenino.setChecked(true);
        }

        Semestre.setSelection(AdapterSemestre.getPosition(user.getSemestre()));
        Contraseña.setText(user.getContraseña());

        // Se llama a esta función para validar la contraseña del usuario
        password();

        // Esta función nos permite estar checacodo si se ha modificado el texto del EditText Contraseña
        Contraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void ActualizarDatos() {
        DB bd = new DB(getContext());
        user.setBoleta(Boleta.getText().toString());
        user.setNombre(Nombre.getText().toString());
        user.setEdad(Edad.getText().toString());

        if (Masculino.isChecked()) {
            user.setGenero("Masculino");
        } else {
            user.setGenero("Femenino");
        }
        user.setSemestre(Semestre.getSelectedItem().toString());
        user.setContraseña(Contraseña.getText().toString());

        if (VerifyCampos()) {
            if (bd.ActualizarUsuario(user) > 0 && !(getActivity() == null)) {
                Toast.makeText(getContext(), "Se actualizo correctamente", Toast.LENGTH_SHORT).show();
                ((MenuPrincipal) getActivity()).Nombre.setText(user.getNombre());
            } else {
                Toast.makeText(getContext(), "Ocurrio un error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Función que nos proporciona si se esta cumpliendo con el llenado de los paramestros para el registro
    private boolean VerifyCampos() {
        // Preguntamos si esta vacio el campo de nombre
        if (Nombre.getText().toString().isEmpty()) {
            Nombre.setError(getString(R.string.SinNombre), null);
            Toast.makeText(mContext, getString(R.string.SinNombre), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si esta vacio el campo de Edad
        if (Edad.getText().toString().isEmpty()) {
            Edad.setError(getString(R.string.SinEdad), null);
            Toast.makeText(mContext, getString(R.string.SinEdad), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Resteingimos la Edad en un rango de 10 a 99 años
        if (Integer.parseInt(String.valueOf(Edad.getText())) < 20 || Integer.parseInt(String.valueOf(Edad.getText())) > 25) {
            Edad.setError(getString(R.string.RangoEdad), null);
            Toast.makeText(mContext, getString(R.string.RangoEdad), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si no se selecciona un semestre
        if (Semestre.getSelectedItem().toString().equals("Selecciona tu semestre actual")) {
            Toast.makeText(mContext,
                    getText(R.string.SinSemestre), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si esta vacio el campo de Contraseña esta vacio
        if (Contraseña.getText().toString().isEmpty()) {
            Contraseña.setError(getString(R.string.SinContraseña), null);
            Toast.makeText(mContext, getString(R.string.SinContraseña), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si todas las restricciones para la contraseña estan en verde si es así la contraseña es valida
        // Variable del color verde
        int colorVerde = Color.GREEN;
        return Longitud.getCurrentTextColor() == colorVerde && CaracterEspecial.getCurrentTextColor() == colorVerde &&
                Numero.getCurrentTextColor() == colorVerde && Minuscula.getCurrentTextColor() == colorVerde &&
                Mayuscula.getCurrentTextColor() == colorVerde;
    }

    // Función que valida los parametros con las restrigcciones de la contraseña
    private void password() {
        String Password = Contraseña.getText().toString().trim();

        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
        if (!Password.matches(".{8,15}")) {
            Longitud.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_too_short_password), null);
        } else {
            Longitud.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún caracter especial
        if (!Password.matches(".*[!@#$%^*+=¿?_-].*")) {
            CaracterEspecial.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_special_caracter), null);
        } else {
            CaracterEspecial.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún digito
        if (!Password.matches(".*\\d.*")) {
            Numero.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_number), null);
        } else {
            Numero.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna minúscula
        if (!Password.matches(".*[a-z].*")) {
            Minuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_lowercase_caracter), null);
        } else {
            Minuscula.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna mayúscula
        if (!Password.matches(".*[A-Z].*")) {
            Mayuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_uppercase_caracter), null);
        } else {
            Mayuscula.setTextColor(Color.GREEN);
        }
    }
}