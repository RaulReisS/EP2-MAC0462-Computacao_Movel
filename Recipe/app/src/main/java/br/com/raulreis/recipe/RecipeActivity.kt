package br.com.raulreis.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.raulreis.recipe.Common.Common
import br.com.raulreis.recipe.databinding.ActivityMainBinding
import br.com.raulreis.recipe.databinding.ActivityRecipeBinding
import com.squareup.picasso.Picasso

class RecipeActivity : AppCompatActivity() {

    private val images = arrayListOf<String>(
        "https://images.pexels.com/photos/5765/flour-powder-wheat-jar.jpg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/4002087/pexels-photo-4002087.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/792613/pexels-photo-792613.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.pexels.com/photos/1788912/pexels-photo-1788912.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
        "https://images.unsplash.com/photo-1581268497089-7a975fb491a3?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=880&q=80",
        "https://images.unsplash.com/photo-1574209908880-a2d3caa70f84?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=857&q=80",
        "https://images.unsplash.com/photo-1610487512810-b614ad747572?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1164&q=80"
    )



    private lateinit var binding : ActivityRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Picasso.with(this@RecipeActivity)
            .load("https://images.pexels.com/photos/698854/pexels-photo-698854.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
            .into(binding.imgCover)

        Picasso.with(this@RecipeActivity)
            .load(images[0])
            .into(binding.imgFlour)

        Picasso.with(this@RecipeActivity)
            .load(images[1])
            .into(binding.imgEgg)

        Picasso.with(this@RecipeActivity)
            .load(images[2])
            .into(binding.imgLemon)

        Picasso.with(this@RecipeActivity)
            .load(images[3])
            .into(binding.imgBerry)

        Picasso.with(this@RecipeActivity)
            .load(images[4])
            .into(binding.imgSugar)

        Picasso.with(this@RecipeActivity)
            .load(images[5])
            .into(binding.imgMind)

        Picasso.with(this@RecipeActivity)
            .load(images[6])
            .into(binding.imgVanilla)
    }
}