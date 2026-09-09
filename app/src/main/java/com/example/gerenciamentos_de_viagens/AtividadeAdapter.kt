package com.example.gerenciamentos_de_viagens

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView


class AtividadeAdapter(
    private val context: Context,
    private val atividades: List<Atividade>
) : BaseAdapter() {

    private val selecionadas = BooleanArray(atividades.size)

    override fun getCount(): Int = atividades.size

    override fun getItem(position: Int): Any = atividades[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_atividade, parent, false)

        val imgIcone = view.findViewById<ImageView>(R.id.imgAtividade)
        val textNome = view.findViewById<TextView>(R.id.textNomeAtividade)
        val textDescricao = view.findViewById<TextView>(R.id.textDescricaoAtividade)
        val checkSelecionar = view.findViewById<CheckBox>(R.id.checkAtividade)

        val atividade = atividades[position]
        imgIcone.setImageResource(atividade.iconRes)
        textNome.text = atividade.nome
        textDescricao.text = atividade.descricao


        checkSelecionar.setOnCheckedChangeListener(null)
        checkSelecionar.isChecked = selecionadas[position]
        checkSelecionar.setOnCheckedChangeListener { _, isChecked ->
            selecionadas[position] = isChecked
        }

        view.setOnClickListener {
            checkSelecionar.isChecked = !checkSelecionar.isChecked
        }

        return view
    }


    fun getSelecionadas(): List<Atividade> {
        return atividades.filterIndexed { index, _ -> selecionadas[index] }
    }
}
