package com.example.basicmapnew.navigation

import com.example.basicmapnew.R
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_alarm.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

var iconName = ""
var nowTemp = ""
var maxTemp = ""
var minTemp = ""
var humidity: String? = ""
var speed: String? = ""
var main: String? = ""
var description: String? = ""


class AlarmFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(com.example.basicmapnew.R.layout.fragment_alarm,container,false)
        getWeatherData(currentlat, currentlng)
        var imageUrl = "http://openweathermap.org/img/w/$iconName.png"
        Glide.with(view).load(imageUrl).into(icon_img)
        weather.text=main
        currentTemp.text = "현재온도: $nowTemp"
        max.text = "최고: $maxTemp"
        min.text = "최저: $minTemp"
        //description=transferWeather(description);
        // final String msg = iconimg+"날씨"+main + "습도" + humidity + "%,풍속" + speed + "m/s" + "온도 현재:" + nowTemp + "/최저" + minTemp + "/최고:" + maxTemp;
        // Toast.makeText(MapsActivity.this,msg,Toast.LENGTH_LONG).show();
        return view
    }





    private fun getWeatherData(lat: Double, lng: Double) {
        val url = "http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lng&units=metric&lang=kr&appid=7cb064f98d125108a0372a58e20a0d52"
        val receiveUseTask = ReceiveWeatherTask()
        receiveUseTask.execute(url)
    }

   private class ReceiveWeatherTask : AsyncTask<String?, Void?, JSONObject?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

       protected override fun doInBackground(vararg datas: String?): JSONObject? {

            try {
                val conn = URL(datas[0]).openConnection() as HttpURLConnection
                conn.connectTimeout = 10000
                conn.readTimeout = 10000
                conn.connect()
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val `is` = conn.inputStream
                    val reader = InputStreamReader(`is`)
                    val `in` = BufferedReader(reader)
                    var readed: String?
                    while (`in`.readLine().also { readed = it } != null) {
                        val jObject = JSONObject(readed)
                        val result = jObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                        return jObject
                    }
                } else {
                    return null
                }
                return null
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: JSONObject?) {
            // Log.i(TAG,result.toString());
            if (result != null) {

                try {
                    iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon")
                    nowTemp = result.getJSONObject("main").getString("temp")
                    humidity = result.getJSONObject("main").getString("humidity")
                    minTemp = result.getJSONObject("main").getString("temp_min")
                    maxTemp = result.getJSONObject("main").getString("temp_max")
                    speed = result.getJSONObject("wind").getString("speed")
                    main = result.getJSONArray("weather").getJSONObject(0).getString("main")
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }



            }
        }

    }}
