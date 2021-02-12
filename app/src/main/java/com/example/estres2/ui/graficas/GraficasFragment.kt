package com.example.estres2.ui.graficas

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.estres2.databinding.FragmentGraphsBinding
import com.jjoe64.graphview.GridLabelRenderer
import kotlin.math.cos
import kotlin.math.sin

class GraficasFragment : Fragment() {
    private var _binding: FragmentGraphsBinding? = null
    private val binding get() = _binding!!
    private lateinit var gridLabelRendererFC: GridLabelRenderer
    private lateinit var gridLabelRendererGSR: GridLabelRenderer
    private lateinit var gridLabelRendererFCYGSR: GridLabelRenderer
    private lateinit var fc1: LineGraphSeries<DataPoint>
    private lateinit var gsr1: LineGraphSeries<DataPoint>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGraphsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObjects()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeObjects() {
        var x = 0.0
        var y: Double
        var y2: Double

        fc1 = LineGraphSeries()
        gsr1 = LineGraphSeries()
        fc1.color = Color.rgb(226, 91, 34)
        fc1.thickness = 6
        fc1.isDrawBackground = true
        fc1.backgroundColor = Color.argb(60, 95, 226, 156)

        val pointsNumber = 500
        for (i in 0 until pointsNumber) {
            y = sin(x)
            y2 = cos(x)
            fc1.appendData(DataPoint(x, y), true, 250)
            gsr1.appendData(DataPoint(x, y2), true, 250)
            x += 0.1
        }
        binding.apply {
            gridLabelRendererFC = FC.gridLabelRenderer
            gridLabelRendererFC.horizontalAxisTitle = "Tiempo (s)"
            gridLabelRendererFC.verticalAxisTitle = "Frecuencia Cardiaca"
            gridLabelRendererGSR = GSR.gridLabelRenderer
            gridLabelRendererGSR.horizontalAxisTitle = "Tiempo (s)"
            gridLabelRendererGSR.verticalAxisTitle = "Kilo Ohm"
            gridLabelRendererFCYGSR = FCYGSR.gridLabelRenderer
            gridLabelRendererFCYGSR.horizontalAxisTitle = "Tiempo (s)"
            gridLabelRendererFCYGSR.verticalAxisTitle = "Frecuencia Cardiaca y Kilo Ohm"

            FC.apply {
                addSeries(fc1)
                title = "Frecuencia Cardiaca"
                viewport.isScalable = true
                viewport.isScrollable = true
                viewport.setScalableY(true)
            }

            GSR.apply {
                addSeries(gsr1)
                title = "Respuesta Galvánica de la Piel"
            }

            FCYGSR.apply {
                addSeries(fc1)
                addSeries(gsr1)
                title = "Frecuencia Cardiaca y Respuesta Galvánica de la Piel"
            }
        }
    }
}