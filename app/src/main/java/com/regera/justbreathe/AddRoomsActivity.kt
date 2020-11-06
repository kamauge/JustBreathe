package com.regera.justbreathe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_rooms.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

private const val REQUEST_CODE = 42
private const val FILE_NAME = "rooms"
private lateinit var photoFile:File
private lateinit var takenImage: Bitmap
private lateinit var auth: FirebaseAuth


class AddRoomsActivity : AppCompatActivity() {

    private val roomsCollectionRef = Firebase.firestore.collection("rooms")

    private val progressDialog = CustomDialog()
    private lateinit var photoUri: Uri
    private var fileProvider: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rooms)

        auth = FirebaseAuth.getInstance()

        btnCamera.setOnClickListener {
            takeCamera()
        }

        btnRoom.setOnClickListener {
            progressDialog.show(this@AddRoomsActivity,"Please Wait...")

            saveRoom()

        }
    }



    private fun saveRoom() = CoroutineScope(Dispatchers.IO).launch{
        try {
            val user = auth.currentUser
            val uid = user?.uid
            val roomsName = roomName.text.toString()
            if (roomsName.isEmpty()){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@AddRoomsActivity, "Room name missing", Toast.LENGTH_LONG).show()
                }
            }

            if (fileProvider == null){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@AddRoomsActivity, "Room image missing", Toast.LENGTH_LONG).show()
                }
            }

            else{
                var imageRef = FirebaseStorage.getInstance().reference.child("images/${fileProvider!!.lastPathSegment}")
                imageRef.putFile(fileProvider!!).await()
                photoUri = imageRef.downloadUrl.await()
                val room = Room(
                    roomsName,
                    photoUri.toString(),
                    uid.toString()

                )
                  roomsCollectionRef.add(room).await()


                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@AddRoomsActivity,
                        "Room details successfully added",
                        Toast.LENGTH_LONG
                    ).show()
                        progressDialog.dialog.dismiss()

                }
            }

        }
        catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@AddRoomsActivity,e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun takeCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = getPhotoFile(FILE_NAME)

        fileProvider = FileProvider.getUriForFile(this, "com.regera.justbreathe", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null){
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        }
        else{
            Toast.makeText(this@AddRoomsActivity,"Unable to open camera app",Toast.LENGTH_LONG).show()
        }

    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            roomImage.setImageBitmap(takenImage)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }



}