package com.example.socialpostapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialpostapp.daos.PostDAO
import com.example.socialpostapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.tasks.await
import kotlin.math.sign

class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDAO
    private lateinit var auth: FirebaseAuth

    //On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionbar = supportActionBar

        auth = Firebase.auth

        faBtn.setOnClickListener {
            val intent = Intent(this, CreatePoastActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }


    //Recycle View
    private fun setUpRecyclerView() {

        postDao = PostDAO()
        val postCollection = postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    //Like Post
    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }


    // Actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.signoutBtn -> {
                showWarningDialogBuilder(findViewById(R.id.signoutBtn))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // Signout
    fun signOut() {

        val user = auth.currentUser
        user?.delete()
        auth.signOut()
        val signInActivityIntent = Intent(this, SigninActivity::class.java)
        startActivity(signInActivityIntent)
        finish()
    }

    // Dialog Box
    fun showWarningDialogBuilder(view: View) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setMessage("Sure About Signing Out?")
            .setNegativeButton("No") { dialog, which -> Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()}
            .setPositiveButton("Yes") {dialog, which -> signOut() }
            .show()
    }
}