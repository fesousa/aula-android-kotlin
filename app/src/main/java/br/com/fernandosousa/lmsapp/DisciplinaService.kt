package br.com.fernandosousa.lmsapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DisciplinaService {

    //TROQUE PELA URL DE ONDE ESTÁ O WS
    // Veja um exemplo no repositório https://github.com/fesousa/aula-android-kotlin-api
    val host = "https://fesousa.pythonanywhere.com"
    val TAG = "WS_LMSApp"

    fun getDisciplinas (context: Context): List<Disciplina> {
        if (AndroidUtils.isInternetDisponivel(context)) {
            val url = "$host/disciplinas"
            val json = HttpHelper.get(url)
            return parserJson(json)
        } else {
            return ArrayList<Disciplina>()
        }
    }

    fun save(disciplina: Disciplina): Response {
        val json = HttpHelper.post("$host/disciplinas", disciplina.toJson())
        return parserJson(json)
    }

    fun delete(disciplina: Disciplina): Response {
        Log.d(TAG, disciplina.id.toString())
        val url = "$host/disciplinas/${disciplina.id}"
        val json = HttpHelper.delete(url)
        Log.d(TAG, json)
        return parserJson(json)
    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}