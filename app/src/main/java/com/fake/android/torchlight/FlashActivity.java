package com.fake.android.torchlight;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kaeptmblaubaer1000 on 23.02.2017.
 * This {@link android.app.Activity} provides compatibility for devices without a flash.
 */
public class FlashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
    }
}
