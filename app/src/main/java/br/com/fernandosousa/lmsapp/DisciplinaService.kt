package br.com.fernandosousa.lmsapp

import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.net.URL

object DisciplinaService {

    //TROQUE PELA URL DE ONDE ESTÁ O WS
    // Veja um exemplo no repositório https://github.com/fesousa/aula-android-kotlin-api
    val host = "https://fesousa.pythonanywhere.com"
    val TAG = "WS_LMSApp"

    fun getDisciplinas (context: Context): List<Disciplina> {
        var disciplinas = ArrayList<Disciplina>()
        if (AndroidUtils.isInternetDisponivel()) {
            val url = "$host/disciplinas"
            val json = HttpHelper.get(url)
            disciplinas = parserJson(json)
            // salvar offline
            for (d in disciplinas) {
                saveOffline(d)
            }
            return disciplinas
        } else {
            val dao = DatabaseManager.getDisciplinaDAO()
            val disciplinas = dao.findAll()
            return disciplinas
        }

    }

    fun getDisciplina (context: Context, id: Long): Disciplina? {

        if (AndroidUtils.isInternetDisponivel()) {
            val url = "$host/disciplinas/${id}"
            val json = HttpHelper.get(url)
            val disciplina = parserJson<Disciplina>(json)

            return disciplina
        } else {
            val dao = DatabaseManager.getDisciplinaDAO()
            val disciplina = dao.getById(id)
            return disciplina
        }

    }

    fun save(disciplina: Disciplina): Response {
        if (AndroidUtils.isInternetDisponivel()) {
            val json = HttpHelper.post("$host/disciplinas", disciplina.toJson())
            return parserJson(json)
        }
        else {
            saveOffline(disciplina)
            return Response("OK", "Disciplina salva no dispositivo")
        }
    }

    fun saveOffline(disciplina: Disciplina) : Boolean {
        val dao = DatabaseManager.getDisciplinaDAO()

        if (! existeDisciplina(disciplina)) {
            dao.insert(disciplina)
        }

        return true

    }

    fun existeDisciplina(disciplina: Disciplina): Boolean {
        val dao = DatabaseManager.getDisciplinaDAO()
        return dao.getById(disciplina.id) != null
    }

    fun delete(disciplina: Disciplina): Response {
        if (AndroidUtils.isInternetDisponivel()) {
            val url = "$host/disciplinas/${disciplina.id}"
            val json = HttpHelper.delete(url)

            return parserJson(json)
        } else {
            val dao = DatabaseManager.getDisciplinaDAO()
            dao.delete(disciplina)
            return Response(status = "OK", msg = "Dados salvos localmente")
        }

    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}