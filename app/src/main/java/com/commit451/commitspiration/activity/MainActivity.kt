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
import butterknife.BindView
import butterknife.ButterKnife
import com.commit451.commitspiration.R
import com.commit451.commitspiration.api.WhatTheCommitClient
import com.commit451.commitspiration.extensions.snack
import com.commit451.commitspiration.model.WhatTheCommitData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    @BindView(R.id.root) lateinit var root: ViewGroup
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.text_message) lateinit var textMessage: TextView
    @BindView(R.id.progress) lateinit var progress: View

    var whatTheCommitData: WhatTheCommitData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.main)
        toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    if (whatTheCommitData != null) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, whatTheCommitData!!.url)
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    } else {
                        root.snack("Nothing to share yet!")
                    }
                    return@OnMenuItemClickListener true
                }
                R.id.action_copy -> {
                    if (whatTheCommitData != null) {
                        // Gets a handle to the clipboard service.
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        // Creates a new text clip to put on the clipboard
                        val clip = ClipData.newPlainText(whatTheCommitData!!.url, whatTheCommitData!!.message)
                        clipboard.primaryClip = clip

                        root.snack("Copied to clipboard")
                    } else {
                        root.snack("Nothing to copy yet")
                    }
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        root.setOnClickListener {
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
                    whatTheCommitData = response.body()
                    textMessage.text = whatTheCommitData!!.message
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
        textMessage.animate()?.alpha(0.0f)
        progress.animate()?.alpha(1.0f)
    }

    private fun hideLoading() {
        textMessage.animate()?.alpha(1.0f)
        progress.animate()?.alpha(0.0f)
    }

    private fun showError() {
        root.snack("Unable to get that commit message for ya, sorry")
    }
}
