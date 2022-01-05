package com.example.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lessonsqlitekotlin.db.MyDbManager
import com.example.lessonsqlitekotlin.db.MyIntentConstants
import kotlinx.android.synthetic.main.edit_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyInstance()
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
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun onClickAddImage(view: View) {
        mainImageLayout.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        mainImageLayout.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty"
        imMainImage.setImageBitmap(null)
    }

    fun onClickChooseImage(view: View) {
        // Открываем Галерею и выбераем картинки
//        val intent = Intent(Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        // intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // просим систему дать постоянную ссылку а не временную
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {
        val myTitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()

        if (myTitle != "" && myDesc != "") {

            CoroutineScope(Dispatchers.Main).launch {
                if(isEditState){
                    myDbManager.updateToDb(myTitle, myDesc, tempImageUri, id, getCurrentTime())
                } else {
                    myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
                }
                finish()
            }
        }

    }

    fun onEditEnable(view: View){
        edTitle.isEnabled = true
        edDesc.isEnabled = true
        fbEdit.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
        if(tempImageUri == "empty") return
        imButtonEditImage.visibility = View.VISIBLE
        imButtonDeleteImage.visibility = View.VISIBLE
    }

    fun getMyInstance() {
        fbEdit.visibility = View.GONE
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                fbAddImage.visibility = View.GONE
                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                isEditState = true
                edTitle.isEnabled = false
                edDesc.isEnabled = false
                fbEdit.visibility = View.VISIBLE
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    mainImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    imMainImage.setImageURI(Uri.parse(tempImageUri))
                    imButtonDeleteImage.visibility = View.GONE
                    imButtonEditImage.visibility = View.GONE
                }
            }
        }
    }

    private fun getCurrentTime():String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}