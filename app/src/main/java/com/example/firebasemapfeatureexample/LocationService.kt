package com.example.firebasemapfeatureexample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat


class LocationService : Service() {
    var context: Context? = null
    private var notification: Notification? = null


//    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
//    private val TAG = "LocationService"
//    override fun onCreate() {
//        super.onCreate()
//        isServiceStarted = true
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setOngoing(false)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager: NotificationManager =
//                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            val notificationChannel = NotificationChannel(
//                NOTIFICATION_CHANNEL_ID,
//                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
//            )
//            notificationChannel.description = NOTIFICATION_CHANNEL_ID
//            notificationChannel.setSound(null, null)
//            notificationManager.createNotificationChannel(notificationChannel)
//            startForeground(1, builder.build())
//        }
//    }
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val timer = Timer()
//        LocationHelper().startListeningUserLocation(
//            this, object : MyLocationListener {
//                override fun onLocationChanged(location: Location?) {
//                    mLocation = location
//                    mLocation?.let {
//                        Log.e(TAG, "onLocationChanged: ${location?.latitude}" )
//                    }
//                }
//            })
//        return START_STICKY
//    }
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        isServiceStarted = false
//    }
//    companion object {
//        var mLocation: Location? = null
//        var isServiceStarted = false
//    }
//}
//
//class BootDeviceReceivers : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        context?.let {
//            ContextCompat.startForegroundService(it, Intent(it, LocationService::class.java))
//        }
//    }

    override fun onCreate() {
        super.onCreate()
        context=this;
        val POWERMANAGER_INTENTS = arrayOf(
            Intent().setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.letv.android.letvsafe",
                    "com.letv.android.letvsafe.AutobootManageActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.startupapp.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.oppo.safe",
                    "com.oppo.safe.permission.startup.StartupAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.battery.ui.BatteryActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.htc.pitroad",
                    "com.htc.pitroad.landingpage.activity.LandingPageActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.asus.mobilemanager",
                    "com.asus.mobilemanager.MainActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.transsion.phonemanager",
                    "com.itel.autobootmanager.activity.AutoBootMgrActivity"
                )
            )
        )

        for (intent in POWERMANAGER_INTENTS) if (packageManager.resolveActivity(
                intent, PackageManager.MATCH_DEFAULT_ONLY
            ) != null
        ) {
            // show dialog to ask user action
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context?.startActivity(intent)
            break
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val channelId = "some_channel_id"
//        val channelName: CharSequence = "Some Channel"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val chan = NotificationChannel(
//                channelId,
//                channelName, NotificationManager.IMPORTANCE_NONE
//            )
//            chan.lightColor = Color.BLUE
//            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(chan)
//        } else {
//            val builder = NotificationCompat.Builder(this)
//                .setContentText("SmartTracker is Running...")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//            val notification = builder.build()
//            startForeground(1, notification)
//            Log.e("home_button_value", "home_button_value")
//        }
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(
//                applicationContext,
//                "My Awesome service toast...",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
        return START_STICKY
}

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}