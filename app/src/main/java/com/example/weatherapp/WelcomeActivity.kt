package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapihelper.MyApiThread

class WelcomeActivity: AppCompatActivity() {
    private val GOTO_MainActivity = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        /* 初始化 */
        LocaleHelper.loadLocale(this) //取得APP設定的地區資訊
        val myApiThread = MyApiThread(this)
        myApiThread.start() //開始執行緒
        try {
            myApiThread.join() //當執行緒完成後就執行finally
        } catch (e: InterruptedException) {
            Log.d("myApiThread InterruptedException", e.toString())
        } finally {
            mHandler.sendEmptyMessageDelayed(GOTO_MainActivity, 500) //跳轉
        }
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GOTO_MainActivity -> {
                    startMainActivity()
                }
            }
        }
    }

    private fun startMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out)
        finish()
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}