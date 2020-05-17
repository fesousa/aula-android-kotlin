package br.com.fernandosousa.lmsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MapasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapas)
    }

    override fun onResume() {
        super.onResume()
        val mapaFragment = MapaFragment()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.layoutMapas, mapaFragment)
                .commit()
    }
}
