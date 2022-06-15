package com.example.colormyview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colormyview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setListeners()
    }

    private fun setListeners(){

        val clickableViews: List<View> =
            listOf(binding.boxOneText, binding.boxTwoText, binding.boxThreeText,
                binding.boxFourText, binding.boxFiveText, binding.constraintLayout,
            binding.buttonRed, binding.buttonYellow, binding.buttonBlue)

        for (i in clickableViews){
            i.setOnClickListener { makeColored(it) }
        }
    }

    private fun makeColored(view: View){
        when(view.id){
            //classe color
            R.id.box_one_text -> view.setBackgroundColor(Color.BLUE)
            R.id.box_two_text -> view.setBackgroundColor(Color.MAGENTA)
            R.id.box_three_text -> view.setBackgroundColor(Color.CYAN)
            R.id.box_four_text -> view.setBackgroundColor(Color.DKGRAY)
            //recursos do android
            R.id.box_five_text -> view.setBackgroundResource(android.R.color.holo_purple)
            //click nos botoes
            R.id.button_red -> binding.boxFourText.setBackgroundResource(android.R.color.holo_red_dark)
            R.id.button_yellow -> binding.boxTwoText.setBackgroundResource(android.R.color.holo_orange_light)
            R.id.button_blue -> binding.boxThreeText.setBackgroundColor(Color.WHITE)
            else -> view.setBackgroundColor(Color.BLACK)
        }
    }
}