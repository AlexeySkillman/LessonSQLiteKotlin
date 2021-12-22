package com.example.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lessonsqlitekotlin.db.MyDbManager
import kotlinx.android.synthetic.main.edit_activity.*

class EditActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)

    val imageRequestCode = 10
    var tempImageUri = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        // val dataList = myDbManager.readDbData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Принимаем Картинку после выбора
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString() // записываем ссылку
        }
    }

    fun onClickAddImage(view: View) {
        mainImageLayout.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        mainImageLayout.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
    }

    fun onClickChooseImage(view: View) {
        // Открываем Галерею и выбераем картинки
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {
        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()

        if (myTitle != "" && myDesc != "") {
            myDbManager.insertToDb(myTitle, myDesc, tempImageUri)
        }
    }
}