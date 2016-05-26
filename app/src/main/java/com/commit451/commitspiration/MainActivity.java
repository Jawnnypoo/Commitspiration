package com.commit451.commitspiration;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commit451.commitspiration.api.WhatTheCommitClient;
import com.commit451.commitspiration.model.WhatTheCommitData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    ViewGroup mRoot;
    TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = (ViewGroup) findViewById(R.id.root);
        mMessage = (TextView) findViewById(R.id.message);

        load();
    }

    private void load() {
        WhatTheCommitClient.instance().getMessage(WhatTheCommitClient.API_URL).enqueue(new Callback<WhatTheCommitData>() {
            @Override
            public void onResponse(Call<WhatTheCommitData> call, Response<WhatTheCommitData> response) {
                if (response.isSuccessful()) {
                    mMessage.setText(response.body().message);
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
