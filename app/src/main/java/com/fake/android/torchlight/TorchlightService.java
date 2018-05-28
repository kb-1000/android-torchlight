package com.fake.android.torchlight;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.fake.android.torchlight.camera.CameraControl;
import com.fake.android.torchlight.camera.TorchlightImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TorchlightService extends Service {
    @Nullable
    private TorchlightImpl torchlight;

    public TorchlightService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getImpl();
    }

    @NotNull
    private synchronized TorchlightImpl getImpl() {
        if(torchlight != null)
        {
            return torchlight;
        } else {
            return torchlight = new TorchlightImpl(CameraControl.getInstance(this));
        }
    }
}
