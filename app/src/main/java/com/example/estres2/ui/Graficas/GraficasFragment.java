package com.example.estres2.ui.Graficas;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.estres2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraficasFragment extends Fragment {
    private TextView Graficas;
    private LineGraphSeries<DataPoint> FC1, GSR1;

    public GraficasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_graficas, container, false);

        Graficas = (TextView) root.findViewById(R.id.text_graficas);
        Graficas.setText("Vista de las gráficas");

        double x, y, y2;
        x = 0;

        GraphView grafica = (GraphView) root.findViewById(R.id.FC);
        GraphView grafica2 = (GraphView) root.findViewById(R.id.GSR);
        GraphView grafica3 = (GraphView) root.findViewById(R.id.FCYGSR);

        GridLabelRenderer gridLabelFC = grafica.getGridLabelRenderer();
        gridLabelFC.setHorizontalAxisTitle("Tiempo (s)");
        gridLabelFC.setVerticalAxisTitle("Frecuencia Cardiaca");

        GridLabelRenderer gridLabelGSR = grafica2.getGridLabelRenderer();
        gridLabelGSR.setHorizontalAxisTitle("Tiempo (s)");
        gridLabelGSR.setVerticalAxisTitle("Kilo Ohm");

        GridLabelRenderer gridLabelFCYGSR = grafica3.getGridLabelRenderer();
        gridLabelFCYGSR.setHorizontalAxisTitle("Tiempo (s)");
        gridLabelFCYGSR.setVerticalAxisTitle("Frecuencia Cardiaca y Kilo Ohm");

        FC1 = new LineGraphSeries<>();
        GSR1 = new LineGraphSeries<>();

        int NumeroPuntos = 500;

        for (int i = 0; i < NumeroPuntos; i++) {
            y = Math.sin(x);
            y2 = Math.cos(x);

            FC1.appendData(new DataPoint(x, y), true, 250);
            GSR1.appendData(new DataPoint(x, y2), true, 250);

            x = x + 0.1;
        }
        grafica.addSeries(FC1);
        grafica.setTitle("Frecuencia Cardiaca");

        grafica2.addSeries(GSR1);
        grafica2.setTitle("Respuesta Galvánica de la Piel");

        grafica3.addSeries(FC1);
        grafica3.addSeries(GSR1);
        grafica3.setTitle("Frecuencia Cardiaca y Respuesta Galvánica de la Piel");

        FC1.setColor(Color.rgb(226, 91, 34));
        FC1.setThickness(6);
        FC1.setDrawBackground(true);
        FC1.setBackgroundColor(Color.argb(60, 95, 226, 156));

        grafica.getViewport().setScalable(true);
        grafica.getViewport().setScrollable(true);
        grafica.getViewport().setScrollableY(true);

        return root;
    }

}
