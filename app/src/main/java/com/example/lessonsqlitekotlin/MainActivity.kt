package com.example.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.example.lessonsqlitekotlin.db.MyDbManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume(){
        super.onResume()
        myDbManager.openDb()
        val dataList = myDbManager.readDbData()
    }


    fun onClickNew(view: View){
        val i  = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}