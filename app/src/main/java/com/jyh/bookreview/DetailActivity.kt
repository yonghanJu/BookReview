
package com.jyh.bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.bumptech.glide.Glide
import com.jyh.bookreview.databinding.ActivityDetailBinding
import com.jyh.bookreview.model.Book
import com.jyh.bookreview.model.Review

class DetailActivity : AppCompatActivity() {

    lateinit var binding:ActivityDetailBinding
    lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDB()
        initLayout()
    }

    private fun initDB() {
        db = getAppDatabase(this)
    }

    private fun initLayout() {
        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTextView.text = model?.title
        binding.descriptionTextView.text = model?.description

        Glide
            .with(this)
            .load(model?.coverSmallUrl)
            .into(binding.coverImageView)

        binding.saveButton.setOnClickListener {
            Thread{
                db.reviewDao().saveReview(Review(model?.id?.toInt() ?: 0, binding.reviewEditText.text.toString()))
            }.start()
        }

        Thread{
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread{
                binding.reviewEditText.setText(review?.review.toString() ?: "")
            }
        }.start()
    }
}