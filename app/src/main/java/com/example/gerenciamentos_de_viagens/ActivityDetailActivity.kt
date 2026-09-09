package com.example.gerenciamentos_de_viagens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ActivityDetailActivity : AppCompatActivity() {

    private lateinit var textIndiceAtividade: TextView
    private lateinit var imgAtividadeDetalhe: ImageView
    private lateinit var textNomeAtividadeDetalhe: TextView
    private lateinit var seekDuracao: SeekBar
    private lateinit var textDuracaoValor: TextView
    private lateinit var radioDificuldade: RadioGroup
    private lateinit var ratingInteresse: RatingBar
    private lateinit var btnConfirmarDetalhe: Button

    private var atividadesSelecionadas: ArrayList<String> = arrayListOf()
    private var indiceAtual: Int = 0
    private var resumoViagem: ArrayList<String> = arrayListOf()
    private var nomeAtividadeAtual: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_detail)

        atividadesSelecionadas = intent.getStringArrayListExtra("atividadesSelecionadas") ?: arrayListOf()
        indiceAtual = intent.getIntExtra("indiceAtual", 0)
        resumoViagem = intent.getStringArrayListExtra("resumoViagem") ?: arrayListOf()
        nomeAtividadeAtual = atividadesSelecionadas.getOrElse(indiceAtual) { "" }


        Log.d(
            "TripPlanner",
            "[Tela 3] Configurando atividade ${indiceAtual + 1}/${atividadesSelecionadas.size}: $nomeAtividadeAtual"
        )

        textIndiceAtividade = findViewById(R.id.textIndiceAtividade)
        imgAtividadeDetalhe = findViewById(R.id.imgAtividadeDetalhe)
        textNomeAtividadeDetalhe = findViewById(R.id.textNomeAtividadeDetalhe)
        seekDuracao = findViewById(R.id.seekDuracao)
        textDuracaoValor = findViewById(R.id.textDuracaoValor)
        radioDificuldade = findViewById(R.id.radioDificuldade)
        ratingInteresse = findViewById(R.id.ratingInteresse)
        btnConfirmarDetalhe = findViewById(R.id.btnConfirmarDetalhe)

        textIndiceAtividade.text = "Atividade ${indiceAtual + 1} de ${atividadesSelecionadas.size}"
        textNomeAtividadeDetalhe.text = nomeAtividadeAtual
        imgAtividadeDetalhe.setImageResource(iconePara(nomeAtividadeAtual))

        textDuracaoValor.text = "${seekDuracao.progress + 1} hora(s)"
        seekDuracao.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textDuracaoValor.text = "${progress + 1} hora(s)"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnConfirmarDetalhe.text =
            if (indiceAtual == atividadesSelecionadas.size - 1) "Finalizar viagem" else "Próxima atividade"

        btnConfirmarDetalhe.setOnClickListener {
            confirmarDetalhe()
        }
    }

    private fun confirmarDetalhe() {
        val idDificuldade = radioDificuldade.checkedRadioButtonId
        if (idDificuldade == -1) {
            Toast.makeText(this, "Escolha o nível de dificuldade", Toast.LENGTH_SHORT).show()
            return
        }

        val dificuldade = findViewById<RadioButton>(idDificuldade).text.toString()
        val duracao = seekDuracao.progress + 1
        val interesse = ratingInteresse.rating.toInt()

        val linhaResumo =
            "$nomeAtividadeAtual — duração: ${duracao}h, dificuldade: $dificuldade, interesse: $interesse★"

        val novoResumo = ArrayList(resumoViagem)
        novoResumo.add(linhaResumo)

        val ehUltimaAtividade = indiceAtual == atividadesSelecionadas.size - 1

        if (!ehUltimaAtividade) {
            Log.d("TripPlanner", "[Tela 3 -> Tela 3] $linhaResumo")

            val intent = Intent(this, ActivityDetailActivity::class.java)
            intent.putStringArrayListExtra("atividadesSelecionadas", atividadesSelecionadas)
            intent.putExtra("indiceAtual", indiceAtual + 1)
            intent.putStringArrayListExtra("resumoViagem", novoResumo)
            startActivity(intent)
        } else {
            // Impressão no console na transição Tela 3 -> Tela 4
            Log.d("TripPlanner", "[Tela 3 -> Tela 4] $linhaResumo")
            Log.d("TripPlanner", "[Tela 3 -> Tela 4] Resumo final da viagem: $novoResumo")

            val intent = Intent(this, SummaryActivity::class.java)
            intent.putStringArrayListExtra("resumoViagem", novoResumo)
            startActivity(intent)
        }
        finish()
    }

    private fun iconePara(nome: String): Int {
        return when {
            nome.contains("Trilha") || nome.contains("Rapel") || nome.contains("Rafting") ->
                R.drawable.ic_aventura
            nome.contains("museu", ignoreCase = true) ||
                nome.contains("histórico", ignoreCase = true) ||
                nome.contains("gastronomia", ignoreCase = true) ->
                R.drawable.ic_cultura
            else -> R.drawable.ic_praia
        }
    }
}
