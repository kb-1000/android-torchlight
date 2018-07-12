package com.fake.android.torchlight;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.fake.android.torchlight.v1.ITorchlight;
import timber.log.Timber;

public class AutoFlashActivity extends AppCompatActivity {
    private ITorchlight torchlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_flash);
        try {
            torchlight = Common.blockingTorchlightBind(this).retain();
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            torchlight.set(true);
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            torchlight.set(false);
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        try {
            torchlight.release();
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int nothing, Intent data) {
        super.onActivityResult(requestCode, nothing, data);
        finish();
    }

    public void finish(@SuppressWarnings("UnusedParameters") View view) {
        finish();
    }

}
