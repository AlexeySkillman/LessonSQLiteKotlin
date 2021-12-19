package com.example.lessonsqlitekotlin

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
        onClickSave()

        tvText.setMovementMethod(ScrollingMovementMethod());
    }

    override fun onResume(){
        super.onResume()
        myDbManager.openDb()
        val dataList = myDbManager.readDbData()
        for (item in dataList){
            tvText.append(item)
            tvText.append("\n")
        }
    }


    private fun onClickSave(){
        onClickSave.setOnClickListener(){
            tvText.text = ""
            // myDbManager.openDb()
            myDbManager.insertDb(edTitle.text.toString(), edContent.text.toString())
            val dataList = myDbManager.readDbData()
            for (item in dataList){
                tvText.append(item)
                tvText.append("\n")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}