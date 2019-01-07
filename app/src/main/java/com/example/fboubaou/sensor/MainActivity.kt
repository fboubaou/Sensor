package com.example.fboubaou.sensor

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


class SensorDrizzle(
    var sensor: String,
    var value: String,
    var value_x: String,
    var value_y: String,
    var value_z: String,
    var timestamp: String
)



class MainActivity : AppCompatActivity() , SensorEventListener{

    val nameTable = mutableMapOf<String, SensorDrizzle>()

//    Sensor Manager y sensores
    private lateinit var mSensorManager : SensorManager
    private lateinit var mAccelerometer : Sensor
    private lateinit var mGyroscope : Sensor
    private lateinit var mMagneticField : Sensor
    private lateinit var mBarometer : Sensor
    private lateinit var mTemperature : Sensor
    private lateinit var mLuminosity : Sensor
    private lateinit var mProximity : Sensor
    private lateinit var mHumidity : Sensor

//    TV's de los valores de acelerometro
    private lateinit var valorx_acc_tv : TextView
    private lateinit var valory_acc_tv : TextView
    private lateinit var valorz_acc_tv : TextView

//    TV'S de los valores de giroscopio
    private lateinit var valorx_gir_tv : TextView
    private lateinit var valory_gir_tv : TextView
    private lateinit var valorz_gir_tv : TextView

//    TV'S de los valores del campo magnético
    private lateinit var valorx_cmag_tv : TextView
    private lateinit var valory_cmag_tv : TextView
    private lateinit var valorz_cmag_tv : TextView

//    TV del valor del termómetro
    private lateinit var valor_temp_tv : TextView

//    TV del valor del termómetro
    private lateinit var valor_lum_tv : TextView

//    TV del barómetro
    private lateinit var valor_bar_tv : TextView

//    TV de la proximidad
    private lateinit var valor_prox_tv : TextView

//    TV de la humedad
    private lateinit var valor_hum_tv : TextView

//    BTN envío de Info
    private lateinit var btn_envia : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        for((key, value) in nameTable){
//
//
//        }

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

//        Obtenemos el BTN de envío de info
        this.btn_envia = findViewById(R.id.btn_send_data_drizzle)
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

//    Listener del botón para enviar datos
    btn_envia.setOnClickListener {
        Toast.makeText(this,"Envío de Información a Drizzle Iniciado",Toast.LENGTH_SHORT).show()
//        Thread para envío de info
//        Thread para envío de info
//        Thread para envío de info

    }

    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {

        when(event?.sensor?.type){


//            Detectado acelerómetro
            Sensor.TYPE_ACCELEROMETER -> {

                val tsLong = System.currentTimeMillis() / 1000
                var ts:String = tsLong.toString()

//                Asignamos datos recibidos al TV'S
                valorx_acc_tv.text = "Valor X: "+ event.values[0].toString()
                valory_acc_tv.text = "Valor Y: "+event.values[1].toString()
                valorz_acc_tv.text = "Valor Z: "+event.values[2].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Acc_Data = SensorDrizzle("Acelerómetro", "", event.values[0].toString(),event.values[1].toString(),event.values[2].toString(),ts)
                nameTable["Accelerometer"] = Acc_Data
//                metodo formateo json
//                metodo formateo json
//                metodo formateo json

            }
//            Detectado Giroscopio
            Sensor.TYPE_GYROSCOPE -> {
//                se ha de crear en cada iteración ?
//                se ha de crear en cada iteración ?
//                se ha de crear en cada iteración ?
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

//                Asignamos valores a los TV'S
                valorx_gir_tv.text = "Valor X: "+ event.values[0].toString()
                valory_gir_tv.text = "Valor Y: "+event.values[1].toString()
                valorz_gir_tv.text = "Valor Z: "+event.values[2].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Gir_Data = SensorDrizzle("Giroscopio", "", event.values[0].toString(),event.values[1].toString(),event.values[2].toString(),ts)
                nameTable["Gyroscope"] = Gir_Data
//                metodo formateo json
//                metodo formateo json
//                metodo formateo json
            }
//            Detectado Campo Magnético
            Sensor.TYPE_MAGNETIC_FIELD ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

//                Asignamos valores a los TV'S
                valorx_cmag_tv.text = "Valor X: "+ event.values[0].toString()
                valory_cmag_tv.text = "Valor Y: "+event.values[1].toString()
                valorz_cmag_tv.text = "Valor Z: "+event.values[2].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val CMag_Data = SensorDrizzle("Campos Magnéticos", "", event.values[0].toString(),event.values[1].toString(),event.values[2].toString(),ts)
                nameTable["Magnetic Fields"] = CMag_Data

//                metodo formateo json
//                metodo formateo json
//                metodo formateo json
            }
//            Detectado termómetro
            Sensor.TYPE_AMBIENT_TEMPERATURE ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

//                asignamos valor a TV
                valor_temp_tv.text = "Valor: "+event.values[0].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Term_Data = SensorDrizzle("Temperatura Ambiente", event.values[0].toString(), "","","",ts)
                nameTable["MagneticFields"] = Term_Data

            }
//            Detectada luminosidad
            Sensor.TYPE_LIGHT ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

//                Asignamos valor a TV
                valor_lum_tv.text = "Valor: "+event.values[0].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Lum_Data = SensorDrizzle("Luminosidad", event.values[0].toString(), "","","",ts)
                nameTable["Luminosity"] = Lum_Data


            }
//            Detectado barómetro
            Sensor.TYPE_PRESSURE ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

//                Asignamos valor a TV
                valor_bar_tv.text = "Valor: "+event.values[0].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Bar_Data = SensorDrizzle("Barómetro", event.values[0].toString(), "","","",ts)
                nameTable["Barometer"] = Bar_Data


            }
//            Detectado proximidad
            Sensor.TYPE_PROXIMITY ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

                valor_prox_tv.text = "Valor: "+event.values[0].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val Prox_Data = SensorDrizzle("Proximidad", event.values[0].toString(), "","","",ts)
                nameTable["Proximity"] = Prox_Data


            }

//            Detectado humedad
            Sensor.TYPE_RELATIVE_HUMIDITY ->{
                val tsLong = System.currentTimeMillis() / 1000
                val ts = tsLong.toString()

                valor_hum_tv.text = "Valor: "+event.values[0].toString()

//                Falta por definir el nombre de los sensores como toca, es decir pedir datos a Joan
                val HRel_Data = SensorDrizzle("HumedadRelativa", event.values[0].toString(), "","","",ts)
                nameTable["RelativeHumidity"] = HRel_Data



            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//
// To change body of created functions use File | Settings | File Templates.
    }


    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mGyroscope,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this, mMagneticField,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this,mTemperature,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this,mLuminosity,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this,mBarometer,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this,mHumidity,SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(this,mProximity,SensorManager.SENSOR_DELAY_NORMAL)
    }

    //Funcion para esconder barra de estado.
    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private fun json1Valor(dataArray:Array<Any>): JSONObject{

//          Creación objetos necesarios.
        val json:JSONObject = JSONObject()
        val jsonA: JSONArray = JSONArray()

//        Recorremos array para generar el mismo array en JSON
        for (i in 0 until dataArray.size) {

                val internalObject = JSONObject()
                when(i){
                    0->{
//                        creacción de objeto con los valores que tocan aqui. (SENSOR)
                        internalObject.put("Sensor", dataArray[i])
                    }
                    1->{
//                        creacción de objeto con los valores que tocan aqui. (VALOR/VALORX,VALORy/VALORZ)
//                        ARRAY DE VALORES O NO ???
//                        ARRAY DE VALORES O NO ???
//                        ARRAY DE VALORES O NO ???
                    }
                    2->{
//
//                        internalObject.put("Timestamp", ts)
                    }
                }

                jsonA.put(internalObject)


        }
        json.put("mode","sync")
        json.put("messageType","m0t0y0p0e1")
        json.put("message",jsonA)


        return json
    }
}





