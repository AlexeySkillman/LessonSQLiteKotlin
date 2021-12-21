package com.example.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.edit_activity.*

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
    }

    fun onClickAddImage(view: View){
        mainImageLayout.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }
    fun onClickDeleteImage(view: View){
        mainImageLayout.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
    }
    fun onClickEditImage(view: View){
    }
}