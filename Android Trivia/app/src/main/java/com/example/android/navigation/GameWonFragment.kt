/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameWonBinding


class GameWonFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false)

        binding.nextMatchButton.setOnClickListener {
            it.findNavController().navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment2())
        }

        val args = GameWonFragmentArgs.fromBundle(arguments!!)
        Toast.makeText(context, "Correct: ${args.numCorrect} | Total: ${args.numQuestions}", Toast.LENGTH_LONG).show()

        setHasOptionsMenu(true)

        return binding.root
    }

    //creating the intent
    private fun getShareIntent() : Intent {
        //get the args passed by gamefragment
        val args = GameWonFragmentArgs.fromBundle(arguments!!)
        //intent action to perform
        val shareIntent = Intent(Intent.ACTION_SEND)
        //intent action sends the args on key/value
        shareIntent
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.share_success_text, args.numQuestions, args.numCorrect))
        return shareIntent
    }

    //start the activity with intent
    private fun shareSucess() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu, menu)

        if (null == getShareIntent().resolveActivity(activity!!.packageManager)){
            menu?.findItem(R.id.share).setVisible(false)
     }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item!!.itemId){
            R.id.share -> shareSucess()
        }
        return super.onOptionsItemSelected(item)
    }
}