package com.example.gerenciamentos_de_viagens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val resumoViagem = intent.getStringArrayListExtra("resumoViagem") ?: arrayListOf()


        Log.d("TripPlanner", "[Tela 4] Resumo completo da viagem recebido:")
        resumoViagem.forEach { linha -> Log.d("TripPlanner", "  - $linha") }

        val textCabecalhoResumo = findViewById<TextView>(R.id.textCabecalhoResumo)
        val listResumo = findViewById<ListView>(R.id.listResumo)
        val btnNovaViagem = findViewById<Button>(R.id.btnNovaViagem)

        if (resumoViagem.isNotEmpty()) {
            textCabecalhoResumo.text = resumoViagem[0]
        }

        val itensAtividades = if (resumoViagem.size > 1) {
            resumoViagem.subList(1, resumoViagem.size)
        } else {
            emptyList()
        }
        listResumo.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itensAtividades)

        btnNovaViagem.setOnClickListener {
            Log.d("TripPlanner", "[Tela 4 -> Tela 1] Iniciando o planejamento de uma nova viagem")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
