package com.example.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lessonsqlitekotlin.db.MyAdapter
import com.example.lessonsqlitekotlin.db.MyDbManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initSearchView()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume(){
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
    }


    fun onClickNew(view: View){
        val i  = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init(){
        rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView) // Присоединили SwapTouch
        rcView.adapter = myAdapter
    }

    fun initSearchView(){
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)
                return true
            }
        } )
    }

    private fun fillAdapter(newText: String){

        job?.cancel()

        // Элементы экрана нельзя изменять со второго потока,
        // Поэтому мы сделали Dispatchers.Main - проходит на главном потоке
        job = CoroutineScope(Dispatchers.Main).launch{

            val list = myDbManager.readDbData(newText)
            myAdapter.updateAdapter(list)

            if(list.size > 0){
                tvNoElements.visibility = View.GONE
            } else {
                tvNoElements.visibility = View.VISIBLE
            }

        }

    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper((object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
                }
        }))
    }

}