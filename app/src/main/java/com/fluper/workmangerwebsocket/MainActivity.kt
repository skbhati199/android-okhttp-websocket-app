package com.fluper.workmangerwebsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.decodeHex

private  val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {


     class EchoWebSocket(val mainActivity:MainActivity) : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            webSocket.send("Hello, it's SSaurel !")
            webSocket.send("What's up?")
//            webSocket.send("try it !".decodeHex())
            webSocket.close(1000, "Goodbye !")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            output(mainActivity,"${text.toString()}")
            Log.d(TAG, "onMessage: $$$$$$text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(TAG, "onMessage: ${bytes.hex()}")
            output(mainActivity,"${bytes.toString()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(1000, null)
            output(mainActivity,"Closing $code $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            output(mainActivity,"Closing $code $reason")
        }

         override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
             super.onFailure(webSocket, t, response)
             output(mainActivity,"Closing ${t.message}")
         }

         private fun output(mainActivity:MainActivity,text: String){
             mainActivity?.runOnUiThread(Runnable {
                 mainActivity?.tvHelloWorld?.setText("${mainActivity?.tvHelloWorld?.text} \n\n $text")
             })
         }
    }

    private lateinit var client: OkHttpClient
    private var  tvHelloWorld: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvHelloWorld = findViewById<TextView>(R.id.tvHelloWorld)
        val button = findViewById<Button>(R.id.button)
        client  = OkHttpClient.Builder().build()

        button.setOnClickListener {
            start()
        }
    }

    fun start(){
        val request = Request.Builder().url("http://192.168.1.3:10000").build()
        val ehoWebSocket = EchoWebSocket(this@MainActivity)
        val ws = client.newWebSocket(request, ehoWebSocket)



        client.dispatcher.executorService.shutdown()
    }




}