package com.commit451.commitspiration.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.commit451.commitspiration.R
import com.commit451.commitspiration.api.WhatTheCommitClient
import com.commit451.commitspiration.model.WhatTheCommitData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var mRoot: ViewGroup? = null
    var mToolbar: Toolbar? = null
    var mMessage: TextView? = null

    var mWhatTheCommitData: WhatTheCommitData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.toolbar) as Toolbar?
        mRoot = findViewById(R.id.root) as ViewGroup?
        mMessage = findViewById(R.id.message) as TextView?

        mToolbar?.setTitle(R.string.app_name)
        mToolbar?.inflateMenu(R.menu.main)
        mToolbar?.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    if (mWhatTheCommitData != null) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, mWhatTheCommitData!!.url)
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    } else {
                        mRoot?.snack("Nothing to share yet!");
                    }
                    return@OnMenuItemClickListener true
                }
                R.id.action_refresh -> {
                    load()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        mMessage?.setOnClickListener {
            if (mWhatTheCommitData != null) {
                // Gets a handle to the clipboard service.
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // Creates a new text clip to put on the clipboard
                val clip = ClipData.newPlainText(mWhatTheCommitData!!.url, mWhatTheCommitData!!.message)
                clipboard.primaryClip = clip

                mRoot?.snack("Copied to clipboard");
            } else {
                mRoot?.snack("Nothing to copy yet");
            }
        }
        load()
    }

    private fun load() {
        WhatTheCommitClient.instance().getMessage(WhatTheCommitClient.API_URL).enqueue(object : Callback<WhatTheCommitData> {
            override fun onResponse(call: Call<WhatTheCommitData>, response: Response<WhatTheCommitData>) {
                if (response.isSuccessful) {
                    mWhatTheCommitData = response.body()
                    mMessage?.text = mWhatTheCommitData!!.message
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<WhatTheCommitData>, t: Throwable) {
                Timber.e(t, null)
                showError()
            }
        })
    }

    fun View.snack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
        val snack = Snackbar.make(this, message, length)
        snack.show()
    }

    private fun showError() {
        mRoot?.snack("Unable to get that commit message for ya, sorry");
    }
}
