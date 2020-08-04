package cn.reanni.image

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClick(view: View) {
        postFrom()
    }

    private val okHttpClient by lazy { OkHttpClient() }

    /* 用表单方式提交一个post请求*/
    private fun postFrom() {
        val requestBody = FormBody.Builder()
            .add("username", "PingAnCx")
            .add("password", "123456") //这里只能是string......
            .build()
        val request = Request.Builder()
            .url("https://www.wanandroid.com/user/login")
            .method("POST", requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "请求网络失败:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "网络返回数据:${response.body()?.string()}")
                //网络返回数据:{"data":{"admin":false,"chapterTops":[],"coinCount":0,"collectIds":[],"email":"","icon":"","id":72647,"nickname":"PingAnCx","password":"","publicName":"PingAnCx","token":"","type":0,"username":"PingAnCx"},"errorCode":0,"errorMsg":""}

            }
        })
    }


}