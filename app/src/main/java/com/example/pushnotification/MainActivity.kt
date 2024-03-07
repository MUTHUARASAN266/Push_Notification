package com.example.pushnotification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pushnotification.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e(TAG, "onCreate: ")

        notificationPermission()

        val message = intent.getStringExtra("Intent_message")
        Log.e(TAG, "onCreate: $message")

//        if (!message.isNullOrEmpty()){
//            AlertDialog.Builder(this)
//                .setTitle("Notification")
//                .setMessage(message)
//                .setPositiveButton("ok") { dialoge, which ->
//                }
//                .show()
//        }

    }

    private fun pushNotificationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "User accepted the notifications!")
            Toast.makeText(this, "User accepted the notifications!", Toast.LENGTH_SHORT).show()
        } else {
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            Snackbar.make(
                findViewById(R.id.main_activity),
                "The user denied the notifications ):",
                4000
            )
                .setAction("Settings") {
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:com.example.pushnotification")
                        )
                    )

                }
                .show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            Log.e(TAG, "User accepted the notifications Permission is granted!")
        } else {

            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
            Log.e(TAG, "features requires a permission that the user has denied!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun notificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "User accepted the notifications!")
        } else {
            requestLocationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.POST_NOTIFICATIONS
                ), 100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "PERMISSION_NOT_GRANTED", Toast.LENGTH_SHORT).show()
            pushNotificationPermission()

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        //  val message= intent.getStringExtra("Intent_message_new")
        val message = intent.getStringExtra("Intent_message_newMessage")

        Log.e(TAG, "onResume: ")
        Log.e(TAG, "message_body: $message")

        // set the message body to textview
        binding.textViewData.text = message
        Snackbar.make(findViewById(R.id.main_activity), message.toString(), 3000)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.black))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            
            .show()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                // Handle the error here if token retrieval fails
                return@addOnCompleteListener
            }

            // Get the device token
            val token = task.result

            Log.e(TAG, "FCM Device Token: $token")
        }

    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(TAG, "onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }
}