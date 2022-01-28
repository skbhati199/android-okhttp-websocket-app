package com.fluper.workmangerwebsocket

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture

import android.widget.RemoteViews




private const val TAG = "DownloadFileWorker"

class DownloadFileWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: start job")
        createNotification()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Important Background job")
            .build()


        val foregroundInfo = ForegroundInfo(NOTIFICATION_ID, notification)
        setForeground(foregroundInfo)

        val data = inputData
        val number = data.getInt("number", -1)

        for (i in 0 until number) {
            Log.d(TAG, "doWork: $i")
            setProgress(workDataOf(ARG_PROGRESS to i))
            showProgress(i)
            try {
                Thread.sleep(1000)
            }catch (e:Throwable) {
                e.printStackTrace()
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun showProgress(progress: Int) {

        val mContentView = RemoteViews(applicationContext.packageName, R.layout.laouter_progress)
        mContentView.setTextViewText(R.id.textView, "Custom notification $progress")
        mContentView.setTextViewText(R.id.textView2, "This is a custom layout $progress")

        val notification = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("File Downloading")
//            .setProgress(100, progress, false)
            .setContent(mContentView)
            .build()
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null){
                notificationChannel = NotificationChannel(CHANNEL_ID,TAG, NotificationManager.IMPORTANCE_LOW)
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    companion object  {
        val CHANNEL_ID = "Download file"
        val NOTIFICATION_ID = 1
        val ARG_PROGRESS = "progress"
    }


}