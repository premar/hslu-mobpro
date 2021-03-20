package com.example.ui_demo

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutLinearRadioButton.setOnClickListener {
            layoutLinearRadioButtonSelected()
        }

        layoutConstraintRadioButton.setOnClickListener {
            layoutConstraintRadioButtonSelected()
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingBarResponseTextView.text = "Aha, Du wählst " + rating
        }

        dialogButton.setOnClickListener {
            dialogButtonPressed()
        }

    }

    override fun onStart() {
        spinner.setSelection(0,false)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                Toast.makeText(requireActivity(), "Du hast '" + parent?.getItemAtPosition(position).toString() + "' gewählt.", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        super.onStart()
    }

    private fun layoutLinearRadioButtonSelected() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_host, LinearLayoutFragment.newInstance())
            .addToBackStack("MainFragment")
            .commit();
    }

    private fun layoutConstraintRadioButtonSelected() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_host, ConstraintLayoutFragment.newInstance())
            .addToBackStack("MainFragment")
            .commit();
    }

    private fun dialogButtonPressed() {
        val items = arrayOf("Alles", "Ein bisschen was", "Nichts")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Was willst Du?")
        builder.setItems(items) {
            _, itemPos ->
            Toast.makeText(requireActivity(), "Du hast '" + items[itemPos] + "' gewählt.", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("Weiss Nicht"){ _, _ ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}