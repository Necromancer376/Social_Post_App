package com.example.socialpostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialpostapp.daos.PostDAO
import kotlinx.android.synthetic.main.activity_create_poast.*

class CreatePoastActivity : AppCompatActivity() {

    private lateinit var postDao: PostDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poast)

        postDao = PostDAO()

        postBtn.setOnClickListener {
            val input = postInput.text.toString()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }
    }
}