package com.commit451.commitspiration.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.commit451.commitspiration.R
import com.commit451.commitspiration.api.WhatTheCommitClient
import com.commit451.commitspiration.extensions.snack
import com.commit451.commitspiration.model.WhatTheCommitData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var mRoot: ViewGroup? = null
    var mToolbar: Toolbar? = null
    var mMessage: TextView? = null
    var mProgress: View? = null

    var mWhatTheCommitData: WhatTheCommitData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.toolbar) as Toolbar?
        mRoot = findViewById(R.id.root) as ViewGroup?
        mMessage = findViewById(R.id.message) as TextView?
        mProgress = findViewById(R.id.progress);

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
                        mRoot?.snack("Nothing to share yet!")
                    }
                    return@OnMenuItemClickListener true
                }
                R.id.action_copy -> {
                    if (mWhatTheCommitData != null) {
                        // Gets a handle to the clipboard service.
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        // Creates a new text clip to put on the clipboard
                        val clip = ClipData.newPlainText(mWhatTheCommitData!!.url, mWhatTheCommitData!!.message)
                        clipboard.primaryClip = clip

                        mRoot?.snack("Copied to clipboard")
                    } else {
                        mRoot?.snack("Nothing to copy yet")
                    }
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        mMessage?.setOnClickListener {
            load()
        }
        load()
    }

    private fun load() {
        showLoading()
        WhatTheCommitClient.service.getMessage(WhatTheCommitClient.API_URL).enqueue(object : Callback<WhatTheCommitData> {
            override fun onResponse(call: Call<WhatTheCommitData>, response: Response<WhatTheCommitData>) {
                hideLoading()
                if (response.isSuccessful) {
                    mWhatTheCommitData = response.body()
                    mMessage?.text = mWhatTheCommitData!!.message
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<WhatTheCommitData>, t: Throwable) {
                Timber.e(t)
                hideLoading()
                showError()
            }
        })
    }

    private fun showLoading() {
        mMessage?.animate()?.alpha(0.0f)
        mProgress?.animate()?.alpha(1.0f)
    }

    private fun hideLoading() {
        mMessage?.animate()?.alpha(1.0f)
        mProgress?.animate()?.alpha(0.0f)
    }

    private fun showError() {
        mRoot?.snack("Unable to get that commit message for ya, sorry")
    }
}
