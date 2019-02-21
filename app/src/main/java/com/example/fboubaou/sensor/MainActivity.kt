package com.example.fboubaou.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONArray
import java.sql.Timestamp
import android.hardware.usb.UsbDevice.getDeviceId
import android.support.annotation.RequiresApi
import android.telephony.TelephonyManager
import android.Manifest.permission
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.TargetApi
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import com.example.fboubaou.sensor.R.id.message
import com.github.kittinunf.fuel.Fuel
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread


class SimpleThread: Thread() {
    public override fun run() {
        Log.d("THREAD","empezado")
    }
}
class MainActivity : AppCompatActivity() , SensorEventListener {

    //    variables Locales
    private var value_acel_x: Double = 0.0
    private var value_acel_y: Double = 0.0
    private var value_acel_z: Double = 0.0

    private var value_gir_x: Double = 0.0
    private var value_gir_y: Double = 0.0
    private var value_gir_z: Double = 0.0

    private var value_mag_x: Double = 0.0
    private var value_mag_y: Double = 0.0
    private var value_mag_z: Double = 0.0

    private var value_temp: Double = 0.0

    private var value_lux: Double = 0.0

    private var value_bar: Double = 0.0

    private var value_prox: Double = 0.0

    private var value_hum: Double = 0.0

    private val json_array_sensors: JSONArray = JSONArray()
    private val json_array_sensors_topost: JSONArray = JSONArray()
    private val json_sensors: JSONObject = JSONObject()
    private var exit: Boolean = false

    private var json_topost: JSONObject = JSONObject()

    //    Sensor Manager y sensores
    private lateinit var mSensorManager: SensorManager
    private var mAccelerometer: Sensor? = null
    private var mGyroscope: Sensor? = null
    private var mMagneticField: Sensor? = null
    private var mBarometer: Sensor? = null
    private var mTemperature: Sensor? = null
    private var mLuminosity: Sensor? = null
    private var mProximity: Sensor? = null
    private var mHumidity: Sensor? = null

    //    Manager para la ubicacion
    private var locationManager: LocationManager? = null

    //    TV's de los valores de acelerometro
    private lateinit var valorx_acc_tv: TextView
    private lateinit var valory_acc_tv: TextView
    private lateinit var valorz_acc_tv: TextView

    //    TV'S de los valores de giroscopio
    private lateinit var valorx_gir_tv: TextView
    private lateinit var valory_gir_tv: TextView
    private lateinit var valorz_gir_tv: TextView

    //    TV'S de los valores del campo magnético
    private lateinit var valorx_cmag_tv: TextView
    private lateinit var valory_cmag_tv: TextView
    private lateinit var valorz_cmag_tv: TextView

    //    TV del valor del termómetro
    private lateinit var valor_temp_tv: TextView

    //    TV del valor del termómetro
    private lateinit var valor_lum_tv: TextView

    //    TV del barómetro
    private lateinit var valor_bar_tv: TextView

    //    TV de la proximidad
    private lateinit var valor_prox_tv: TextView

    //    TV de la humedad
    private lateinit var valor_hum_tv: TextView

    //    BTN's manipulación de Info
    private lateinit var btn_envia: Button
    private lateinit var btn_stop: Button

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        obtenemos el TV del acelerómetro
        this.valorx_acc_tv = findViewById(R.id.acc_valorx_tv)
        this.valory_acc_tv = findViewById(R.id.acc_valory_tv)
        this.valorz_acc_tv = findViewById(R.id.acc_valorz_tv)

//        obtenemos los TV'S del giroscopio
        this.valorx_gir_tv = findViewById(R.id.gir_valorx_tv)
        this.valory_gir_tv = findViewById(R.id.gir_valory_tv)
        this.valorz_gir_tv = findViewById(R.id.gir_valorz_tv)

//        obtenemos los TV'S del giroscopio
        this.valorx_cmag_tv = findViewById(R.id.cmag_valorx_tv)
        this.valory_cmag_tv = findViewById(R.id.cmag_valory_tv)
        this.valorz_cmag_tv = findViewById(R.id.cmag_valorz_tv)

//        obtenemos el TV del termómetro
        this.valor_temp_tv = findViewById(R.id.term_valor_tv)

//        obtenemos el tv de la luminosidad
        this.valor_lum_tv = findViewById(R.id.lum_valor_tv)

//        obtenemos el TV del barómetro
        this.valor_bar_tv = findViewById(R.id.bar_valor_tv)

//        obtenemos el TV de proximidad
        this.valor_prox_tv = findViewById(R.id.prox_valor_tv)

//        obtenemos el TV de humedad
        this.valor_hum_tv = findViewById(R.id.hum_valor_tv)

//        Obtenemos el BTN's manipulacion de info
        this.btn_envia = findViewById(R.id.btn_send_data_drizzle)
        this.btn_stop = findViewById(R.id.btn_stop_send_data_drizzle)
/*-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------*/
//        Sensor Manager
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

//        Obtenemos el Hardware del accelerómetro
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

//        Obtenemos Hardware del giroscopio
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

//        Obtenemos Hardware de los campos magnéticos
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

//        Obtenemos Hardware del termómetro
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

//        Obtenemos Hardware de la luminosidad
        mLuminosity = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

//        Obtenemos Hardware de barómetro
        mBarometer = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

//        Obtenemos Hardware de proximidad
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

//        Obtenemos Hardware de humedad
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)


        val t1 = ThreadExample1()
//    Listener del botón para enviar datos

        var first_entry:Boolean = false
        btn_envia.setOnClickListener {

            if (!first_entry) {
                Toast.makeText(this, "Envío de Información a Drizzle Iniciado", Toast.LENGTH_LONG).show()
                t1.start()
                first_entry = true
            } else {
                this.exit = false
            }




        }
        btn_stop.setOnClickListener {
            Toast.makeText(this, "Envío de Información a Drizzle Parado", Toast.LENGTH_SHORT).show()

            this.exit = true
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {

        when (event?.sensor?.type) {


//            Detectado acelerómetro
            Sensor.TYPE_ACCELEROMETER -> {

                //             registramos el valor en la variable local
                value_acel_x = event.values[0].toDouble()
                value_acel_y = event.values[1].toDouble()
                value_acel_z = event.values[2].toDouble()

//                Asignamos datos recibidos al TV'S
                valorx_acc_tv.text = "Valor X: " + value_acel_x.toString()
                valory_acc_tv.text = "Valor Y: " + value_acel_y.toString()
                valorz_acc_tv.text = "Valor Z: " + value_acel_z.toString()

            }
//            Detectado Giroscopio
            Sensor.TYPE_GYROSCOPE -> {

                //             registramos el valor en la variable local
                value_gir_x = event.values[0].toDouble()
                value_gir_y = event.values[1].toDouble()
                value_gir_z = event.values[2].toDouble()

//                Asignamos valores a los TV'S
                valorx_gir_tv.text = "Valor X: " + value_gir_x.toString()
                valory_gir_tv.text = "Valor Y: " + value_gir_y.toString()
                valorz_gir_tv.text = "Valor Z: " + value_gir_z.toString()


            }
            Sensor.TYPE_MAGNETIC_FIELD -> {

//                registramos el valor en la variable local
                value_mag_x = event.values[0].toDouble()
                value_mag_y = event.values[1].toDouble()
                value_mag_z = event.values[2].toDouble()
//                Asignamos valores a los TV'S
                valorx_cmag_tv.text = "Valor X: " + value_mag_x.toString()
                valory_cmag_tv.text = "Valor Y: " + value_mag_y.toString()
                valorz_cmag_tv.text = "Valor Z: " + value_mag_z.toString()


            }
//            Detectado termómetro
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
//                registramos el valor en la variable local
                value_temp = event.values[0].toDouble()
//                asignamos valor a TV
                valor_temp_tv.text = "Valor: " + value_temp.toString()


            }
//            Detectada luminosidad
            Sensor.TYPE_LIGHT -> {
//                registramos el valor en la variable local
                value_lux = event.values[0].toDouble()
//                Asignamos valor a TV
                valor_lum_tv.text = "Valor: " + value_lux.toString()


            }
//            Detectado barómetro
            Sensor.TYPE_PRESSURE -> {
//                registramos el valor en la variable local
                value_bar = event.values[0].toDouble()
//                Asignamos valor a TV
                valor_bar_tv.text = "Valor: " + value_bar.toString()


            }

//            Detectado proximidad
            Sensor.TYPE_PROXIMITY -> {
//                registramos el valor en la variable local
                value_prox = event.values[0].toDouble()
//                asignamos valor a TV
                valor_prox_tv.text = "Valor: " + value_prox.toString()


            }
//            Detectado Campo Magnético

//            Detectado humedad
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
//                registramos el valor en la variable local
                value_hum = event.values[0].toDouble()
//               asignamos valor a TV
                valor_hum_tv.text = "Valor: " + value_hum.toString()


            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mLuminosity, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mBarometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL)
    }


    private fun appendDataJsonArray(json_array: JSONArray, json_k: String, json_v: Any, json_obj: JSONObject) {
        val obj = JSONObject()

//        incluimos los K,V en nuestro objeto para incluirlos en el array.
        obj.put(json_k, json_v)
        json_obj.put(json_k, json_v)
        json_array.put(obj)

    }

    private fun jsonValorTotal(array_sensors: JSONArray, json_obj: JSONObject): JSONObject {

        val json_res = JSONObject()
        array_sensors.put(json_obj)

        json_res.put("mode", "async")
        json_res.put("messageType", "09a60d4e0c11fc1a6756")
        json_res.put("messages", array_sensors)

        return json_res
    }

    private fun checkValue(value: Double): Boolean {
        return (value <= 0.0 || value >= 0.0)
    }

    private fun appendCheckedValue(key: String, value: Double) {
        if (checkValue(value)) {
            appendDataJsonArray(json_array_sensors, key, value, json_sensors)
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    inner class ThreadExample1 : Thread() {

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun run() {
            while (!exit){


//            generamos timestamp y lo appendizamos.
                val tsLong = System.currentTimeMillis()
                appendDataJsonArray(json_array_sensors, "TimeStamp", tsLong, json_sensors)
                appendDataJsonArray(json_array_sensors, "ID", "24:71:89:E9:AA:86", json_sensors)


                appendCheckedValue("TempAmb", value_temp)
                appendCheckedValue("TempSensor", 0.0)
                appendCheckedValue("AcelX", value_acel_x)
                appendCheckedValue("AcelY", value_acel_y)
                appendCheckedValue("AcelZ", value_acel_z)
                appendCheckedValue("GiroX", value_gir_x)
                appendCheckedValue("GiroY", value_gir_y)
                appendCheckedValue("GiroZ", value_gir_z)
                appendCheckedValue("Lux", value_lux)
                appendCheckedValue("Hum", value_hum)
                appendCheckedValue("MagX", value_mag_x)
                appendCheckedValue("MagY", value_mag_y)
                appendCheckedValue("MagZ", value_mag_z)
                appendCheckedValue("Bar", value_bar)
                appendDataJsonArray(json_array_sensors, "Lat", 0, json_sensors)
                appendDataJsonArray(json_array_sensors, "Long", 0, json_sensors)

                json_topost = jsonValorTotal(json_array_sensors_topost, json_sensors)



            Log.d("JSON TEST TO POST", json_topost.toString())
            Fuel.post("https://iotmmsad277ddd4.hana.ondemand.com/com.sap.iotservices.mms/v1/api/http/data/0a380ddd-b307-4018-8b3e-85f4bf46b832")
                .header("Authorization" to "Bearer 4fc9de4f207fadb0af66bbfe4b871762","content-type" to "application/json")
                .body(json_topost.toString(),Charsets.UTF_8)
                .responseString()
//            limpiamos el valor recién enviado.
                json_topost.remove("message")
                json_array_sensors_topost.remove(0)
                sleep(500)


            }
        }

        fun main(args: Array<String>) {

            }
        }

    }






