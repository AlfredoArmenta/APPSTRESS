package com.example.estres2.ui.registros

import android.content.Context
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
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentRegistersBinding
import com.example.estres2.util.EntropyObject
import com.example.estres2.util.setDataGraph
import com.jjoe64.graphview.GridLabelRenderer
import java.io.File

class RegisterFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRegistersBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var user: User
    private lateinit var gridLabelRendererFC: GridLabelRenderer
    private lateinit var gridLabelRendererGSR: GridLabelRenderer
    private lateinit var gridLabelRendererFCYGSR: GridLabelRenderer
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
        initializedParameterGraphs()
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
                        removeSeries()
                        setDataGraph()
                        setVisibilityGraph()
                        setGraphs()
                    } else {
                        FC.visibility = View.GONE
                        GSR.visibility = View.GONE
                        FCYGSR.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setVisibilityGraph() {
        binding.apply {
            FC.visibility = View.VISIBLE
            GSR.visibility = View.VISIBLE
            FCYGSR.visibility = View.VISIBLE
        }
    }

    private fun initializedParameterGraphs() {
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
        }
    }

    private fun setGraphs() {
        binding.apply {
            FC.apply {
                addSeries(EntropyObject.getGraphFC())
                title = "Frecuencia Cardiaca"
                viewport.isScalable = true
                viewport.isScrollable = true
                viewport.setScalableY(true)
            }

            GSR.apply {
                addSeries(EntropyObject.getGraphGSR())
                title = "Respuesta Galvánica de la Piel"
                viewport.isScalable = true
                viewport.isScrollable = true
                viewport.setScalableY(true)
            }

            FCYGSR.apply {
                addSeries(EntropyObject.getGraphFC())
                addSeries(EntropyObject.getGraphGSR())
                title = "Frecuencia Cardiaca y Respuesta Galvánica de la Piel"
                viewport.isScalable = true
                viewport.isScrollable = true
                viewport.setScalableY(true)
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

    private fun removeSeries() {
        binding.apply {
            FC.removeAllSeries()
            GSR.removeAllSeries()
            FCYGSR.removeAllSeries()
        }
    }
}