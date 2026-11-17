package com.example.appdadosbrian

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appdadosbrian.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // el scope lo pongo como atributo para poder cancelarlo en el onDestroy
    private val scope = CoroutineScope(Dispatchers.Main)

    private var suma = 0
    private var numeroUsuario = 0
    private var intentos = 0
    private val maxIntentos = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.txtResultado.visibility = View.INVISIBLE

        binding.imgCubilete.setOnClickListener {
            empezar()
        }
    }

    private fun empezar() {
        val texto = binding.editNumero.text.toString()

        if (texto.isEmpty()) {
            Toast.makeText(this, "Introduce un número", Toast.LENGTH_SHORT).show()
            return
        }

        numeroUsuario = texto.toInt()

        if (numeroUsuario < 3 || numeroUsuario > 18) {
            Toast.makeText(this, "El número tiene que estar entre 3 y 18", Toast.LENGTH_SHORT).show()
            return
        }

        // bloqueo el cubilete y el edittext hasta que termine
        bloquear(true)
        intentos = 0
        averiguarNumero()
    }

    private fun averiguarNumero() {
        scope.launch {
            binding.txtResultado.visibility = View.INVISIBLE

            // tiro los dados hasta que la suma sea el numero o se acaben los intentos
            do {
                intentos++
                binding.txtIntentos.text = "Intento $intentos"

                repeat(3) {
                    delay(150)
                    mostrarDados()
                }
                mostrarResultado()
                delay(400)
            } while (suma != numeroUsuario && intentos < maxIntentos)

            if (suma == numeroUsuario) {
                val intent = Intent(this@MainActivity, AcertadoActivity::class.java)
                intent.putExtra("numero", numeroUsuario)
                intent.putExtra("intentos", intentos)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity,
                    "No lo he adivinado en $maxIntentos intentos", Toast.LENGTH_LONG).show()
            }

            // ya ha terminado, se puede volver a pulsar
            bloquear(false)
        }
    }

    private fun mostrarDados() {
        val valor1 = (1..6).random()
        val valor2 = (1..6).random()
        val valor3 = (1..6).random()

        cambiarImagen(binding.imgDado1, valor1)
        cambiarImagen(binding.imgDado2, valor2)
        cambiarImagen(binding.imgDado3, valor3)

        suma = valor1 + valor2 + valor3
    }

    private fun cambiarImagen(imagen: ImageView, valor: Int) {
        when (valor) {
            1 -> imagen.setImageResource(R.drawable.dado1)
            2 -> imagen.setImageResource(R.drawable.dado2)
            3 -> imagen.setImageResource(R.drawable.dado3)
            4 -> imagen.setImageResource(R.drawable.dado4)
            5 -> imagen.setImageResource(R.drawable.dado5)
            6 -> imagen.setImageResource(R.drawable.dado6)
        }
    }

    private fun mostrarResultado() {
        binding.txtResultado.text = "Suma: $suma"
        binding.txtResultado.visibility = View.VISIBLE
    }

    private fun bloquear(bloqueado: Boolean) {
        binding.imgCubilete.isEnabled = !bloqueado
        binding.editNumero.isEnabled = !bloqueado
        // lo pongo medio transparente para que se vea que no se puede pulsar
        if (bloqueado) {
            binding.imgCubilete.alpha = 0.4f
        } else {
            binding.imgCubilete.alpha = 1f
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
