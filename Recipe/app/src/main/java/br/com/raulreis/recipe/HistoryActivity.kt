package br.com.raulreis.recipe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.raulreis.recipe.databinding.ActivityHistoryBinding
import br.com.raulreis.recipe.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPrefs = getSharedPreferences("HISTORICO", Context.MODE_PRIVATE)
        var count = sharedPrefs.getInt("COUNT", 0)

        var infos =  ArrayList<Info>()


        if (count != 0)
            for (i in 1..count) {
                val nome = sharedPrefs.getString("name_$i", "")
                val data = sharedPrefs.getString("update_$i", "")
                val temp = sharedPrefs.getString("temp_$i", "")

                val info = Info(nome!!, data!!, temp!!)

                infos.add(info)
            }

        val adapter = InfoAdapter(this, infos)
        listView.adapter = adapter
    }
}