package com.example.persistenz.models

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class StorageModel(app: Application) : AndroidViewModel(app) {

    companion object {
        var FILENAME_INTERNAL = "internalStorage.txt"
        var DIRNAME_EXTERNAL = "Persistenz"
        var FILENAME_EXTERNAL = "externalStorage.txt"
        var LOG_TARGET = "StorageModel"
    }

    private var externalText : String = ""

    public fun writeInternalStorage(text: String) {
        var context = getApplication<Application>().applicationContext
        try {
            context.openFileOutput(FILENAME_INTERNAL, Context.MODE_PRIVATE).use { output ->
                output.write(text.toByteArray())
            }
        }
        catch (e: Exception) {
            Log.e(LOG_TARGET, e.toString())
        }
    }

    public fun readInternalStorage() : String {
        var text = ""
        var context = getApplication<Application>().applicationContext
        try {
            context.openFileInput(FILENAME_INTERNAL).use { stream ->
                text = stream.bufferedReader().use {
                    it.readText()
                }
            }
        }
        catch (e: Exception) {
            Log.e(LOG_TARGET, e.toString())
        }
        return text;
    }

    public fun writeExternalStorage(text: String) {
        var context = getApplication<Application>().applicationContext
        try {
            if (isExternalStorageWritable()) {
                FileOutputStream(File(context.getExternalFilesDir(DIRNAME_EXTERNAL), FILENAME_EXTERNAL)).use { output ->
                    output.write(text.toByteArray())
                }
            }
        }
        catch (e: Exception) {
            Log.e(LOG_TARGET, e.toString())
        }
    }

    public fun readExternalStorage() : String {
        var context = getApplication<Application>().applicationContext
        var text = ""
        try {
            if (isExternalStorageReadable()) {
                FileInputStream(File(context.getExternalFilesDir(DIRNAME_EXTERNAL), FILENAME_EXTERNAL)).use { stream ->
                    text = stream.bufferedReader().use {
                        it.readText()
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e(LOG_TARGET, e.toString())
        }
        return text
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }
}