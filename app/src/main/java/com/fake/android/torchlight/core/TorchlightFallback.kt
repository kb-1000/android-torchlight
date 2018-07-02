package com.fake.android.torchlight.core

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fake.android.torchlight.R

class TorchlightFallback: Torchlight() {
    override fun init(context: Context?) {}

    override fun _release() {}

    override fun _set(enable: Boolean) {
        TODO("not implemented")
    }

    /**
     * Created by kaeptmblaubaer1000 on 23.02.2017.
     * This [android.app.Activity] provides compatibility for devices without a flash.
     */
    class Activity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_flash)
        }
    }
}