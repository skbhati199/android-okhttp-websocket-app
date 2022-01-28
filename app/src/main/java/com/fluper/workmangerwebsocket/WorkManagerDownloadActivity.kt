package com.fluper.workmangerwebsocket

import android.icu.util.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.work.*
import com.fluper.workmangerwebsocket.DownloadFileWorker.Companion.ARG_PROGRESS

class WorkManagerDownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_work_manager_download)

        val data = Data.Builder()
            .putInt("number", 100)
            .build()

        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadFileWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(1, java.util.concurrent.TimeUnit.MICROSECONDS)
            .addTag("download")
            .build()

        val workManager = WorkManager.getInstance(this)

        workManager.enqueue(oneTimeWorkRequest)

        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this){
            if (it != null){
                val progress = it.progress
                val value = progress.getInt(ARG_PROGRESS, 0)
                findViewById<ProgressBar>(R.id.progressBar).progress = value


            }
        }

    }
}