package com.example.roomorganizer

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OutputActivity : AppCompatActivity() {
    private lateinit var roomOrganizationText: TextView
    private val studentObjectList = mutableListOf<Student>()
    private var roomOrganizationString = ""
    companion object {
        const val KEY_MESSAGE = "KEY_MESSAGE"
    }
    class Student(name: String, gender: String, age: String) {
        var name: String = name
        var gender: String = gender
        var age: String = age
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output)
        bind()
        roomOrganizationText.setMovementMethod(ScrollingMovementMethod())
        populateStudentObjectList(getIntentData())
        makeStudentPairs()
        roomOrganizationText.text = roomOrganizationString
    }

    private fun bind() {
        roomOrganizationText = findViewById(R.id.roomOrganization_textView)
    }

    private fun getIntentData(): String {
        return intent.getStringExtra(KEY_MESSAGE).toString()
    }

    private fun populateStudentObjectList(studentString: String) {
        val studentList = studentString.split(";").toTypedArray()

        var studentListSplit = emptyList<String>()
        studentList.forEach {
            studentListSplit = it.split(",")
            studentObjectList.add(
                    Student(studentListSplit[0],
                            studentListSplit[1],
                            studentListSplit[2])
            )
        }
    }

    private fun makeStudentPairs() {
        roomOrganizationString += getString(R.string.roomOrganization_textView_rooms) + "\n" + "\n"

        var i = 0
        var j: Int
        var size = studentObjectList.size
        while (i < size) {
            j = i + 1
            while (j < size) {
                if (studentObjectList[i].gender.toLowerCase() == studentObjectList[j].gender.toLowerCase()
                        && kotlin.math.abs(studentObjectList[i].age.toInt() - studentObjectList[j].age.toInt()) <= 2) {

                    fillRoomOrganizationText(studentObjectList[i].name, studentObjectList[j].name,
                            studentObjectList[i].gender, studentObjectList[i].age, studentObjectList[j].age)

                    studentObjectList.removeAt(j)
                    studentObjectList.removeAt(i)
                    i = 0
                    j = 1
                    size -= 2
                }
                j++
            }
            i++
        }
        if (studentObjectList.isNotEmpty()) {
            fillRoomOrganizationText_leftOuts()
        }
    }

    private fun fillRoomOrganizationText(name1: String, name2: String, gender: String, age1: String, age2: String) {
            roomOrganizationString += gender.toUpperCase() + getString(R.string.tab) +
                name1 + "(" + age1 + ")" + getString(R.string.tab) +
                name2 + "(" + age2 + ")" + getString(R.string.tab) + "\n"
    }

    private fun fillRoomOrganizationText_leftOuts() {
        roomOrganizationString += "\n" + "\n" + getString(R.string.roomOrganization_text_leftOut) + "\n" + "\n"
        studentObjectList.forEach {
            roomOrganizationString += it.gender.toUpperCase() + getString(R.string.tab) + it.name + "(" + it.age + ")" + "\n"
        }
    }
}