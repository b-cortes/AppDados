package com.example.appdadosbrian

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appdadosbrian.databinding.ActivityAcertadoBinding

class AcertadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcertadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAcertadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val numero = intent.getIntExtra("numero", 0)
        val intentos = intent.getIntExtra("intentos", 0)

        binding.txtNumero.text = "Tu número era el $numero"
        binding.txtIntentos.text = "Lo he sacado en $intentos intentos"

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
