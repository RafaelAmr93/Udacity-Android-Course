package com.example.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.InputDevice
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

/*
The big idea about data binding is to create an object that connects/maps/binds two pieces of
distant information together at compile time, so that you don't have to look for it at runtime.
 */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val authorNameData: AuthorName = AuthorName("Thomas Mann")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.sendButton.setOnClickListener {
            addBookName(it)
        }

        //id do textview precisa ser diferente da variável q será referenciada aqui, pra n dar conflito
        binding.authorName = authorNameData
    }

    private fun addBookName(view: View){

        binding.apply {
            authorNameData.nickname = binding.typedBook.text.toString()
            binding.typedBook.text = binding.bookInput.text
            binding.bookInput.visibility = View.GONE
            //o botao eh passado como parametro para depois do click sumir
            view.visibility = View.GONE
            binding.typedBook.visibility = View.VISIBLE
        }
        //esconde o teclado
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}