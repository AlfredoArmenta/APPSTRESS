package com.example.estres2.ui.registros

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.MainViewModel
import com.example.estres2.R
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentRegistersBinding
import com.example.estres2.util.EntropyObject
import com.example.estres2.util.FileCharacteristics
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.util.normalizer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.io.File

class RegisterFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRegistersBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var user: User
    private val lRegister: MutableList<UserRegister> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = binding.root.context
        showRegister()
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setObservers() {
        mainViewModel.apply {
            updateRegisters.observe(viewLifecycleOwner) {
                if (it) {
                    println("Se actualizo el estado del observador: $it")
                    showRegister()
                }
            }
            stateGraph.observe(viewLifecycleOwner) { update ->
                binding.apply {
                    if (update) {
                        setVisibilityGraph()
                        setGraphs()
                    } else {
                        registerGraph.visibility = View.GONE

                        FC.visibility = View.GONE
                        textFc.visibility = View.GONE
                        textTimeFc.visibility = View.GONE

                        GSR.visibility = View.GONE
                        textGsr.visibility = View.GONE
                        textTimeGsr.visibility = View.GONE

                        FCYGSR.visibility = View.GONE
                        textFcgsr.visibility = View.GONE
                        textTimeFcgsr.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setVisibilityGraph() {
        binding.apply {
            registerGraph.visibility = View.VISIBLE

            FC.visibility = View.VISIBLE
            textFc.visibility = View.VISIBLE
            textTimeFc.visibility = View.VISIBLE

            GSR.visibility = View.VISIBLE
            textGsr.visibility = View.VISIBLE
            textTimeGsr.visibility = View.VISIBLE

            FCYGSR.visibility = View.VISIBLE
            textFcgsr.visibility = View.VISIBLE
            textTimeFcgsr.visibility = View.VISIBLE
        }
    }

    private fun setGraphs() {

        val lineDataSetFcAndGsr = ArrayList<ILineDataSet>()

        val lineDataSetFC = LineDataSet(EntropyObject.getGraphFC(), "Frecuencia Cardiaca")
        lineDataSetFC.setDrawCircles(false)
        lineDataSetFC.color = Color.MAGENTA
        lineDataSetFC.setDrawValues(false)

        val lineDataSetGSR = LineDataSet(EntropyObject.getGraphGSR(), "Ohms")
        lineDataSetGSR.setDrawCircles(false)
        lineDataSetGSR.color = Color.CYAN
        lineDataSetGSR.setDrawValues(false)

        val lineDataSetNormalizerFC = LineDataSet(normalizer(FileCharacteristics.getFc(), FileCharacteristics.getMaxFc()), "Frecuencia Cardiaca")
        lineDataSetNormalizerFC.setDrawCircles(false)
        lineDataSetNormalizerFC.color = Color.MAGENTA
        lineDataSetNormalizerFC.setDrawValues(false)

        val lineDataSetNormalizerGSR = LineDataSet(normalizer(FileCharacteristics.getGsr(),FileCharacteristics.getMaxGsr()), "Respuesta Galvánica de la Piel")
        lineDataSetNormalizerGSR.setDrawCircles(false)
        lineDataSetNormalizerGSR.color = Color.CYAN
        lineDataSetNormalizerGSR.setDrawValues(false)

        lineDataSetFcAndGsr.apply {
            add(lineDataSetNormalizerFC)
            add(lineDataSetNormalizerGSR)
        }

        binding.apply {

            FC.apply {
                clear()
                setDrawBorders(true)
                setDrawGridBackground(false)
                legend.textSize = 16F
                legend.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textSize = 14f
                axisRight.isEnabled = false
                xAxis.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textSize = 14f
                xAxis.setDrawLabels(true)
                setBorderColor(ContextCompat.getColor(context, R.color.colorPrimaryText))
                setDrawGridBackground(false)
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                data = LineData(lineDataSetFC)
            }
            GSR.apply {
                clear()
                setDrawBorders(true)
                setDrawGridBackground(false)
                legend.textSize = 16F
                legend.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textSize = 14f
                axisRight.isEnabled = false
                xAxis.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textSize = 14f
                xAxis.setDrawLabels(true)
                setBorderColor(ContextCompat.getColor(context, R.color.colorPrimaryText))
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                data = LineData(lineDataSetGSR)
            }
            FCYGSR.apply {
                clear()
                setDrawBorders(true)
                setDrawGridBackground(false)
                legend.textSize = 14F
                legend.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                axisLeft.textSize = 14f
                axisRight.isEnabled = false
                xAxis.textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textSize = 14f
                xAxis.setDrawLabels(true)
                setBorderColor(ContextCompat.getColor(context, R.color.colorPrimaryText))
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                data = LineData((lineDataSetFcAndGsr))
            }
        }
    }

    private fun showRegister() {
        val arrayFiles = getRegisters()
        lRegister.clear()
        if (arrayFiles.isNotEmpty()) {
            for (element in arrayFiles) {
                lRegister.add(UserRegister(element, R.drawable.ic_options_register))
            }
        } else {
            Toast.makeText(mContext, "Carpeta Vacia", Toast.LENGTH_LONG).show()
            lRegister.add(UserRegister("Carpeta Vacia", R.drawable.ic_not_registered))
        }
        val itemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(mContext, R.drawable.divider)?.let {
            itemDecoration.setDrawable(it)
        }
        binding.Resgistros.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
            adapter = RegisterListAdapter(lRegister, mainViewModel)
            addItemDecoration(itemDecoration)
        }
    }

    private fun getRegisters(): List<String> {
        val item: MutableList<String> = ArrayList()
        val db = DB(mContext)
        user = getObjectBoleta()
        val folder = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + user.boleta)
        println("Ubicación de los archivos: ${folder.path}")
        if (folder.exists() && !folder.listFiles().isNullOrEmpty()) {
            folder.listFiles().also {
                if (!it.isNullOrEmpty()) {
                    for (file in (it)) if (file.path.endsWith(".csv") && db.getRecord(file.name))
                        item.add(file.name)
                }
            }
            return item
        } else {
            Toast.makeText(mContext, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show()
        }
        return item
    }
}
