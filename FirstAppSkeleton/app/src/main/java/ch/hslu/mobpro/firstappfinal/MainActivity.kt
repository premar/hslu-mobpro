package ch.hslu.mobpro.firstappfinal


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mobpro.firstappfinal.lifecyclelog.LifecycleLogActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

	companion object {
		val QUESTION = "question"
		val ANSWER = "answer"
	}

	private val openQuestionActivity =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
			// check if the result is ok and set the content to the textview
			if (result.resultCode == RESULT_OK) {
				val resultTextView = findViewById<TextView>(R.id.main_textView_result)
				val answer = result.data?.extras?.getString(ANSWER)
				resultTextView.text = answer
			}
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		findViewById<Button>(R.id.main_button_logActivity).setOnClickListener { startLogActivity() }
		findViewById<Button>(R.id.main_button_startBrowser).setOnClickListener { startBrowser() }
		findViewById<Button>(R.id.main_button_questionActivity).setOnClickListener { startQuestionActivity() }
	}


	private fun startLogActivity() {
		// start LifecylceLogActivity mit LifecycleLogFragment
		val intent = Intent(this, LifecycleLogActivity::class.java )
		startActivity(intent)
	}

	private fun startBrowser() {
		// start Browser with http://www.hslu.ch
		var url = "http://www.hslu.ch"
		val intent = Intent(Intent.ACTION_VIEW)
		intent.data = Uri.parse(url)
		startActivity(intent)
	}

	private fun startQuestionActivity() {
		// launch QuestionActivity with Intent
		val intent = Intent(this, QuestionActivity::class.java )
		intent.putExtra(QUESTION, "Und, wie läuft's so...")
		openQuestionActivity.launch(intent)
	}
}