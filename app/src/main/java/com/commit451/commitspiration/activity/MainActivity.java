package com.commit451.commitspiration.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commit451.commitspiration.R;
import com.commit451.commitspiration.api.WhatTheCommitClient;
import com.commit451.commitspiration.model.WhatTheCommitData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ViewGroup mRoot;
    TextView mMessage;

    WhatTheCommitData mWhatTheCommitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRoot = (ViewGroup) findViewById(R.id.root);
        mMessage = (TextView) findViewById(R.id.message);

        mToolbar.setTitle(R.string.app_name);
        mToolbar.inflateMenu(R.menu.main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if (mWhatTheCommitData != null) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, mWhatTheCommitData.url);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            Snackbar.make(mRoot, "Nothing to share yet!", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                        return true;
                    case R.id.action_refresh:
                        load();
                        return true;
                }
                return false;
            }
        });
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWhatTheCommitData != null) {
                    // Gets a handle to the clipboard service.
                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    // Creates a new text clip to put on the clipboard
                    ClipData clip = ClipData.newPlainText(mWhatTheCommitData.url, mWhatTheCommitData.message);
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(mRoot, "Copied to clipboard", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar.make(mRoot, "Nothing to copy yet", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        load();
    }

    private void load() {
        WhatTheCommitClient.instance().getMessage(WhatTheCommitClient.API_URL).enqueue(new Callback<WhatTheCommitData>() {
            @Override
            public void onResponse(Call<WhatTheCommitData> call, Response<WhatTheCommitData> response) {
                if (response.isSuccessful()) {
                    mWhatTheCommitData = response.body();
                    mMessage.setText(mWhatTheCommitData.message);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<WhatTheCommitData> call, Throwable t) {
                Timber.e(t, null);
                showError();
            }
        });
    }

    private void showError() {
        Snackbar.make(mRoot, "Unable to get that commit message for ya, sorry", Snackbar.LENGTH_LONG)
                .show();
    }
}
