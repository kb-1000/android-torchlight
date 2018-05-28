package com.fake.android.torchlight;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.fake.android.torchlight.core.TorchlightControl;
import com.fake.android.torchlight.core.TorchlightImpl;
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
            return torchlight = new TorchlightImpl(TorchlightControl.getInstance(this));
        }
    }
}
