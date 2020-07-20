package com.hiroyaoooooo.apitest2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{
            Log.d("###","setOnClickListener")
            textView.text = "buttonClicked"
            val client:OkHttpClient = OkHttpClient()
            val url:String = "http://maapi.net/apis/mecapi?"

            val body:FormBody = FormBody.Builder()
                .add("sentence", "今日は、いい天気ですね。")
                .add("response","")
                .add("filter","")
                .add("format","")
                .add("dic","")
                .build()
            val request = Request.Builder().url(url).post(body).build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("faild", e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseText:String? = response.body?.string()
                    textView.text = responseText
                    parseXml(responseText!!)
                }

            })
        }


    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseXml(inputString:String) {
        val factory =
            XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        val responseInputStream: InputStream = ByteArrayInputStream(inputString.toByteArray(charset("utf-8")))
        parser.setInput(responseInputStream,"utf-8")
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.name == "surface") {
                    Log.d("surface##:", parser.nextText())
                }
                if (parser.name == "feature") {
                    Log.d("feature##:", parser.nextText())
                }
            }
            eventType = parser.next()
        }
    }

}