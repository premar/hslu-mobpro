package com.example.comandcon.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DemoWorker(appContext: Context, private val workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            val waitingTime = workerParams.inputData.getLong("Time", 7000)
            Thread.sleep(waitingTime)
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}