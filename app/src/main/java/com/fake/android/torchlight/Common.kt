package com.fake.android.torchlight

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.annotation.StringRes
import android.widget.Toast
import com.fake.android.torchlight.v1.ITorchlight
import timber.log.Timber

import java.util.concurrent.BrokenBarrierException
import java.util.concurrent.CyclicBarrier

object Common {
    private const val defaultDuration = Toast.LENGTH_SHORT

    @JvmOverloads
    internal fun toast(context: Context, @StringRes resId: Int, duration: Int = defaultDuration) {
        toast(context, context.getText(resId), duration)
    }

    @JvmOverloads
    fun toast(context: Context, msg: CharSequence, duration: Int = defaultDuration) {
        Toast.makeText(context, msg, duration).show()
    }

    //TODO: reimplement following function as a inlined template
    fun <T : IBinder> blockingBind(context: Context, clazz: Class<out Service>, serviceDisconnectCallback: ServiceDisconnectCallback): T {
        val intent = Intent(context, clazz)
        val barrier = CyclicBarrier(2)
        val serviceConnection = BlockingServiceConnection(serviceDisconnectCallback, barrier)

        context.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT or Context.BIND_ADJUST_WITH_ACTIVITY or Context.BIND_AUTO_CREATE or Context.BIND_IMPORTANT)
        try {
            barrier.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: BrokenBarrierException) {
            Timber.e(e)
            throw RuntimeException(e)
        }


        return serviceConnection.service as T
    }

    fun blockingTorchlightBind(context: Context): ITorchlight {
        return blockingBind<ITorchlight.Stub>(context, TorchlightService::class.java, NullServiceDisconnectCallback())
    }

    interface ServiceDisconnectCallback {
        fun serviceDisconnected()
    }

    class NullServiceDisconnectCallback : ServiceDisconnectCallback {
        override fun serviceDisconnected() {}
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private class BlockingServiceConnection internal constructor(private val disconnectCallback: ServiceDisconnectCallback, private val barrier: CyclicBarrier) : ServiceConnection {
        internal var service: IBinder? = null
            private set


        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            this.service = service
            try {
                barrier.await()
            } catch (e: InterruptedException) {
                Timber.e(e)
            } catch (e: BrokenBarrierException) {
                Timber.e(e)
                throw RuntimeException(e)
            }

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            disconnectCallback.serviceDisconnected()
        }
    }
}
