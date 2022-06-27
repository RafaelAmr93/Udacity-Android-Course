package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.diceroller.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        val diceResult = binding.diceResult
        val rollButton = binding.rollButton



        rollButton.setOnClickListener {
            //Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show()
            random(diceResult)
        }


        setContentView(rootView)
    }

    private fun random(some: ImageView) {
        val imageSelector = when (Random().nextInt(6) + 1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.ic_launcher_background
        }
        binding.previousImg.setImageResource(imageSelector)
        binding.diceResult.setImageResource(imageSelector)
    }
}