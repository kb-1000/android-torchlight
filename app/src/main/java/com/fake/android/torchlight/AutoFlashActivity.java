package com.fake.android.torchlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fake.android.torchlight.camera.Camera;
import com.fake.android.torchlight.camera.CameraControl;

public class AutoFlashActivity extends AppCompatActivity {
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_flash);
        camera = CameraControl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CameraControl.hasFlash()) {
            camera.set(true);
        }
        if (!CameraControl.hasFlash()) {
            final Intent intent = new Intent(this, FlashActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (CameraControl.hasFlash()) {
            camera.set(false);
        }
    }

    @Override
    public void finish() {
        super.finish();
        camera.release();
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