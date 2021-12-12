package com.example.roomorganizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.util.regex.Pattern

private const val STUDENT_LIST = "STUDENT_LIST"

class MainActivity : AppCompatActivity() {
    private lateinit var studentListInput: TextView
    private lateinit var organizeButton: Button
    private var studentListText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind()
        savedInstanceState?.let {
            studentListText = it.getString(STUDENT_LIST, "")
        }
        addEvents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STUDENT_LIST, studentListText)
    }

    private fun bind() {
            studentListInput = findViewById(R.id.studentList_textInput)
            organizeButton = findViewById(R.id.organize_button)
    }

    private fun addEvents() {
        organizeButton.setOnClickListener() {
            val studentString = studentListInput.text.replace("\\s".toRegex(), "")

            if (validateStudentString(studentString.toString())) {
                val intent = Intent(this, OutputActivity::class.java)
                intent.putExtra(OutputActivity.KEY_MESSAGE, studentString.toString())
                startActivity(intent)
            }
        }
    }

    fun <T> hasDuplicates(arr: MutableList<T>): Boolean {
        return arr.size != arr.distinct().count();
    }

    private fun validateStudentString(studentString: String): Boolean {
        try {
            if (studentString.isEmpty()) {
                throw Exception(getString(R.string.studentList_error_empty))
            }

            if (studentString.last().equals(';'))
                throw Exception(getString(R.string.studentList_error_format))

            if (!Pattern.matches("(\\w+,(H|M|h|m),\\d+(;|$))+", studentString)) 
                throw Exception(getString((R.string.studentList_error_format)))

            val studentList = studentString.split(";").toTypedArray()
            val studentList_names = mutableListOf<String>()
            studentList.forEach {
                studentList_names.add(it.substringBefore(","))
            }
            if (hasDuplicates<String>(studentList_names)) {
                throw Exception(getString(R.string.studentList_error_duplicate))
            }
            return true
        }
        catch(e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            return false
        }
    }
}