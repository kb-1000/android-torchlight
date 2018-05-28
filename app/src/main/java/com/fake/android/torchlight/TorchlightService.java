package com.fake.android.torchlight;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.fake.android.torchlight.core.Torchlight;
import com.fake.android.torchlight.core.TorchlightControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TorchlightService extends Service {
    @Nullable
    private Torchlight torchlight;

    public TorchlightService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getImpl();
    }

    @NotNull
    private synchronized Torchlight getImpl() {
        if (torchlight != null) {
            return torchlight;
        } else {
            return torchlight = TorchlightControl.getInstance(this);
        }
    }
}
