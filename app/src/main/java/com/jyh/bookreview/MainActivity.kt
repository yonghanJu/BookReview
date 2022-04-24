package com.jyh.bookreview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.jyh.bookreview.adapter.BookAdapter
import com.jyh.bookreview.adapter.HistoryAdapter
import com.jyh.bookreview.api.BookService
import com.jyh.bookreview.databinding.ActivityMainBinding
import com.jyh.bookreview.model.BestSellerDTO
import com.jyh.bookreview.model.Book
import com.jyh.bookreview.model.History
import com.jyh.bookreview.model.SearchBookDTO
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //viewBinding 적용
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: BookAdapter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var bookService: BookService
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDB()
        initSearchEditText()
        initBookRecyclerView()
        initHistoryRecyclerView()
        initRetrofit()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        // 키 이벤트 KeyEvent = 타자
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) { // 엔터가 눌리면
                val keyword = (v as EditText).text.toString()
                search(keyword)
                saveSearchKeyword(keyword)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // 모션 이벤트 MotionEvent = 터치
        binding.searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter { deleteSearchKeyword(it) }
        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
        }.start()
        showHistoryView()
    }

    private fun initDB() {
        db = getAppDatabase(this)
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()
        adapter.onItemClickedListener = object :BookAdapter.OnItemClickedListener{
            override fun onItemClicked(bookModel: Book) {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("bookModel", bookModel)
                startActivity(intent)
            }
        }
        binding.bookRecyclerView.adapter = adapter
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)
        bookService.getBestSeller(getString(R.string.interparkAPIKey))
            .enqueue(object : Callback<BestSellerDTO> {

                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interparkAPIKey), keyword)
            .enqueue(object : Callback<SearchBookDTO> {

                override fun onResponse(
                    call: Call<SearchBookDTO>,
                    response: Response<SearchBookDTO>
                ) {

                    hideHistoryView()
                    saveSearchKeyword(keyword)
                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<SearchBookDTO>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    hideHistoryView()
                }

            })
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                historyAdapter.submitList(keywords)
            }
        }.start()
        binding.historyRecyclerView.isVisible=true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible=false
    }

    companion object {
        const val TAG = "MainActivity"
    }
}