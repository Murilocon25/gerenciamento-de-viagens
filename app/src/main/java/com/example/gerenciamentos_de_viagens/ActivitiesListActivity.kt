package com.example.gerenciamentos_de_viagens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivitiesListActivity : AppCompatActivity() {

    private lateinit var textResumoTopo: TextView
    private lateinit var listAtividades: ListView
    private lateinit var btnConfirmarAtividades: Button
    private lateinit var adapter: AtividadeAdapter

    private var destino: String = ""
    private var dataPartida: String = ""
    private var dataRetorno: String = ""
    private var horario: String = ""
    private var preferencia: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_list)

        destino = intent.getStringExtra("destino") ?: ""
        dataPartida = intent.getStringExtra("dataPartida") ?: ""
        dataRetorno = intent.getStringExtra("dataRetorno") ?: ""
        horario = intent.getStringExtra("horario") ?: ""
        preferencia = intent.getStringExtra("preferencia") ?: ""


        Log.d(
            "TripPlanner",
            "[Tela 2] Recebido de Tela 1 -> destino=$destino | partida=$dataPartida | " +
                "retorno=$dataRetorno | horario=$horario | preferencia=$preferencia"
        )

        textResumoTopo = findViewById(R.id.textResumoTopo)
        listAtividades = findViewById(R.id.listAtividades)
        btnConfirmarAtividades = findViewById(R.id.btnConfirmarAtividades)

        textResumoTopo.text = "Destino: $destino  •  Preferência: $preferencia"

        val atividadesDisponiveis = gerarAtividades(preferencia)
        adapter = AtividadeAdapter(this, atividadesDisponiveis)
        listAtividades.adapter = adapter

        btnConfirmarAtividades.setOnClickListener {
            confirmarAtividades()
        }
    }

    private fun confirmarAtividades() {
        val selecionadas = adapter.getSelecionadas()
        if (selecionadas.isEmpty()) {
            Toast.makeText(this, "Selecione ao menos uma atividade", Toast.LENGTH_SHORT).show()
            return
        }

        // Impressão no console na transição Tela 2 -> Tela 3
        Log.d(
            "TripPlanner",
            "[Tela 2 -> Tela 3] Atividades selecionadas: ${selecionadas.joinToString { it.nome }}"
        )

        val nomesSelecionados = ArrayList(selecionadas.map { it.nome })

        // Primeira linha do resumo, que acompanha a viagem até a Tela 4
        val resumoViagem = ArrayList<String>()
        resumoViagem.add(
            "Viagem para $destino de $dataPartida a $dataRetorno " +
                "(horário: $horario, preferência: $preferencia)"
        )

        val intent = Intent(this, ActivityDetailActivity::class.java)
        intent.putStringArrayListExtra("atividadesSelecionadas", nomesSelecionados)
        intent.putExtra("indiceAtual", 0)
        intent.putStringArrayListExtra("resumoViagem", resumoViagem)
        startActivity(intent)
    }

    private fun gerarAtividades(preferencia: String): List<Atividade> {
        return when (preferencia) {
            "Aventura" -> listOf(
                Atividade("Trilha na montanha", "Caminhada guiada por trilhas naturais", R.drawable.ic_aventura),
                Atividade("Rapel", "Descida com corda em paredão rochoso", R.drawable.ic_aventura),
                Atividade("Rafting", "Descida de corredeiras em bote inflável", R.drawable.ic_aventura)
            )
            "Cultura" -> listOf(
                Atividade("Visita a museu", "Tour guiado por museus históricos da região", R.drawable.ic_cultura),
                Atividade("Passeio histórico", "Caminhada pelo centro histórico da cidade", R.drawable.ic_cultura),
                Atividade("Aula de gastronomia local", "Aprenda receitas típicas com um chef local", R.drawable.ic_cultura)
            )
            else -> listOf(
                Atividade("Mergulho", "Exploração de recifes e vida marinha", R.drawable.ic_praia),
                Atividade("Passeio de barco", "Passeio pela orla e ilhas próximas", R.drawable.ic_praia),
                Atividade("Vôlei de praia", "Partida amistosa na areia com outros turistas", R.drawable.ic_praia)
            )
        }
    }
}
