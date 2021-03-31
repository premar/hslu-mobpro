package com.example.comandcon.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.comandcon.databinding.ActivityMainBinding
import com.example.comandcon.models.BandsViewModel
import com.example.comandcon.worker.DemoWorker
import com.squareup.picasso.Picasso
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var demoThread : Thread
    private val bandsViewModel: BandsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        demoThread = Thread()

        bandsViewModel.getBands().observe(this, Observer { list ->
            binding.communicationBandNameTextView.text = ""
            binding.communicationBandInfoTextView.text = ""
            binding.communicationBandImage.visibility = View.GONE
            binding.communicationShowBandCounterTextView.text = "#Bands = " +
                    if (list.isEmpty()) 0 else list.size
            if (list.isNotEmpty()) {
                showBandDialog()
            }
        })

        bandsViewModel.getCurrentBand().observe(this, Observer { bandInfo ->
            binding.communicationBandNameTextView.text = bandInfo?.name ?: ""
            if (bandInfo != null) {
                binding.communicationBandInfoTextView.text = bandInfo.homeCountry +
                        " Gründung: " + bandInfo.foundingYear
            } else {
                binding.communicationBandInfoTextView.text = ""
            }
            if (bandInfo?.bestOfCdCoverImageUrl != null) {
                Picasso.get()
                        .load(bandInfo.bestOfCdCoverImageUrl)
                        .into(binding.communicationBandImage)
                binding.communicationBandImage.visibility = View.VISIBLE
            } else {
                binding.communicationBandImage.visibility = View.GONE
            }
        })

        binding.concurrencyBlockMainThreadButton.setOnClickListener {
            Thread.sleep(7000)
        }

        binding.concurrencyStartNewThreadButton.setOnClickListener {
            if (!demoThread.isAlive) {
                demoThread = createDemoThread()
                demoThread.start()
                binding.concurrencyStartNewThreadButton.text = "Demo-Thread läuft"
            } else {
                Toast.makeText(this@MainActivity, "Demo Thread läuft bereits!", Toast.LENGTH_SHORT).show();
            }
        }

        binding.concurrencyStartNewWorkerButton.setOnClickListener {
            val workerManager = WorkManager.getInstance(application)
            val demoWorkerRequest = OneTimeWorkRequestBuilder<DemoWorker>().setInputData(Data.Builder().putLong("Team", 7000).build()).build()
            workerManager.enqueue(demoWorkerRequest)
        }

        binding.communicationStartServerRequestButton.setOnClickListener {
            bandsViewModel.requestBandCodeFromEndpoint()
        }

        binding.communicationResetViewModelButton.setOnClickListener {
            bandsViewModel.reset()
        }

        binding.communicationShowBandsButton.setOnClickListener {
            showBandDialog()
        }
    }

    private fun showBandDialog() {
        val bands = bandsViewModel.getBands().value!!
        AlertDialog.Builder(this)
                .setTitle("Welche Band?")
                .setItems(bands.map { bandCode -> bandCode.name }.toTypedArray())
                { _: DialogInterface, itemPos: Int ->
                    bandsViewModel.requestBandInfoFromEndpoint(bands[itemPos].code)
                }
                .setNegativeButton("Test") {_, _ -> }
                .create()
                .show()
    }

    private fun createDemoThread() : Thread {
        return object : Thread("demoThread") {
            override fun run() {
                try {
                    sleep(7000)
                    runOnUiThread() {
                        binding.concurrencyStartNewThreadButton.text = "Demo-Thread starten"
                        Toast.makeText(this@MainActivity, "Demo Thread beendet.", Toast.LENGTH_SHORT).show();
                    }
                } catch (e : Exception) {
                    Log.e("Exception", e.toString())
                }
            }
        }
    }
}