package com.fake.android.torchlight;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.StringRes;
import android.widget.Toast;
import com.fake.android.torchlight.v1.ITorchlight;
import timber.log.Timber;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class Common {
    private static final int defaultDuration = Toast.LENGTH_SHORT;

    private Common() {
    }

    static void toast(Context context, @StringRes int resId) {
        toast(context, resId, defaultDuration);
    }

    static void toast(Context context, @StringRes int resId, int duration) {
        toast(context, context.getText(resId), duration);
    }

    public static void toast(Context context, CharSequence msg) {
        toast(context, msg, defaultDuration);
    }

    public static void toast(Context context, CharSequence msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static <T extends IBinder> T blockingBind(Context context, Class<? extends Service> clazz, ServiceDisconnectCallback serviceDisconnectCallback) {
        final Intent intent = new Intent(context, clazz);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final BlockingServiceConnection serviceConnection = new BlockingServiceConnection(serviceDisconnectCallback, barrier);

        context.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT | Context.BIND_ADJUST_WITH_ACTIVITY | Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }

        //noinspection unchecked
        return (T) serviceConnection.getService();
    }

    public static ITorchlight blockingTorchlightBind(Context context) {
        return blockingBind(context, TorchlightService.class, new NullServiceDisconnectCallback());
    }

    public interface ServiceDisconnectCallback {
        void serviceDisconnected();
    }

    public static class NullServiceDisconnectCallback implements ServiceDisconnectCallback {
        @Override
        public void serviceDisconnected() {
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private static class BlockingServiceConnection implements ServiceConnection {
        private final CyclicBarrier barrier;
        private ServiceDisconnectCallback disconnectCallback;
        private IBinder service;

        private BlockingServiceConnection(ServiceDisconnectCallback disconnectCallback, CyclicBarrier barrier) {
            this.disconnectCallback = disconnectCallback;
            this.barrier = barrier;
        }


        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            this.service = service;
            try {
                barrier.await();
            } catch (InterruptedException e) {
                Timber.e(e);
            } catch (BrokenBarrierException e) {
                Timber.e(e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            disconnectCallback.serviceDisconnected();
        }

        IBinder getService() {
            return service;
        }
    }
}
