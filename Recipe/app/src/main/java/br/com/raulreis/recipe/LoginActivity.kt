package br.com.raulreis.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.raulreis.recipe.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val btnEntrar = binding.btnLoginEntrar
    }

    fun login(view: android.view.View) {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}