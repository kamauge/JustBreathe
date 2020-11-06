package com.regera.justbreathe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rooms: MutableList<Room>
    private lateinit var adapter: RoomsAdapter
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        loadData()

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> goHome()
                R.id.nav_room -> addRooms()
                R.id.log_out -> logOut()
            }
            true
        }
        rooms = mutableListOf()
        adapter = RoomsAdapter(this, rooms)
        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        roomsDashboard.layoutManager = gridLayoutManager
        roomsDashboard.adapter = adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()

        val builder = AlertDialog.Builder(this@HomeActivity)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure you want to logout?")

        builder.setPositiveButton(
            "YES"
        ) { dialog, i ->
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        builder.setNegativeButton(
            "NO"
        ) { dialogInterface, i -> }

        val alertDialog = builder.create()
        alertDialog.show()


    }

    private fun addRooms(){
        val intent = Intent(this@HomeActivity, AddRoomsActivity::class.java)
        startActivity(intent)
    }

    private fun goHome(){
    }

    private fun loadData(){
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            val email = user.email

        }
        val roomsCollectionRef = firestoreDb.collection("rooms").whereEqualTo("userId", uid)
        roomsCollectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null){
                //Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val roomList = snapshot.toObjects(Room::class.java)
            rooms.clear()
            rooms.addAll(roomList)
            adapter.notifyDataSetChanged()
            for (rm in roomList) {
               // Log.i(TAG, "Room $rm")
            }

        }
    }



}