package com.example.gerenciamentos_de_viagens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editDestino: EditText
    private lateinit var datePartida: DatePicker
    private lateinit var dateRetorno: DatePicker
    private lateinit var timeHorario: TimePicker
    private lateinit var radioPreferencias: RadioGroup
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editDestino = findViewById(R.id.editDestino)
        datePartida = findViewById(R.id.datePartida)
        dateRetorno = findViewById(R.id.dateRetorno)
        timeHorario = findViewById(R.id.timeHorario)
        radioPreferencias = findViewById(R.id.radioPreferencias)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        btnConfirmar.setOnClickListener {
            confirmarConfiguracao()
        }
    }

    private fun confirmarConfiguracao() {
        val destino = editDestino.text.toString().trim()
        if (destino.isEmpty()) {
            Toast.makeText(this, "Informe o destino da viagem", Toast.LENGTH_SHORT).show()
            return
        }

        val idPreferenciaSelecionada = radioPreferencias.checkedRadioButtonId
        if (idPreferenciaSelecionada == -1) {
            Toast.makeText(this, "Escolha uma preferência de viagem", Toast.LENGTH_SHORT).show()
            return
        }
        val preferencia = findViewById<RadioButton>(idPreferenciaSelecionada).text.toString()

        val dataPartida = "${datePartida.dayOfMonth}/${datePartida.month + 1}/${datePartida.year}"
        val dataRetorno = "${dateRetorno.dayOfMonth}/${dateRetorno.month + 1}/${dateRetorno.year}"
        val horario = String.format("%02d:%02d", timeHorario.hour, timeHorario.minute)


        Log.d(
            "TripPlanner",
            "[Tela 1 -> Tela 2] destino=$destino | partida=$dataPartida | retorno=$dataRetorno | " +
                "horario=$horario | preferencia=$preferencia"
        )

        val intent = Intent(this, ActivitiesListActivity::class.java)
        intent.putExtra("destino", destino)
        intent.putExtra("dataPartida", dataPartida)
        intent.putExtra("dataRetorno", dataRetorno)
        intent.putExtra("horario", horario)
        intent.putExtra("preferencia", preferencia)
        startActivity(intent)
    }
}
