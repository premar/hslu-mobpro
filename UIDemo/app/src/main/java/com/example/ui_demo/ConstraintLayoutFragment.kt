package com.example.ui_demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_constraint_layout.*

class ConstraintLayoutFragment : Fragment(R.layout.fragment_constraint_layout) {

    private var counter = 0
    private val counterViewModel: CounterViewModel by activityViewModels()

    companion object {
        fun newInstance(): ConstraintLayoutFragment {
            return ConstraintLayoutFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelButton.text = "VIEWMODEL: $counterViewModel.getCounter() ++"
        fragmentButton.text = "FRAGMENT: " + counter + "++"


        viewModelButton.setOnClickListener {
            counterViewModel.incCounter()
            viewModelButton.text = "VIEWMODEL: " + counterViewModel.getCounter() + "++"
        }

        fragmentButton.setOnClickListener {
            counter++
            fragmentButton.text = "FRAGMENT: " + counter + "++"
        }
    }
}