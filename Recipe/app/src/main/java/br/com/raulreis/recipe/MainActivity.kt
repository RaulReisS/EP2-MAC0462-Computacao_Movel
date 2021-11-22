package br.com.raulreis.recipe


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.com.raulreis.recipe.Common.Common
import br.com.raulreis.recipe.Common.Helper
import br.com.raulreis.recipe.Model.OpenWeatherMap
import br.com.raulreis.recipe.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Variáveis globais
    val PERMISSION_REQUEST_CODE = 1001
    val PLAY_SERVICE_RESOLUTION_REQUEST = 1000

    var mGoogleApiClient : GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    internal var openWetaherMap = OpenWeatherMap()

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.txvHistory.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }

        requestPermission()
        if (checkPlayService())
            buildGoogleApiClient()

        auth = Firebase.auth
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayService()) {
                        buildGoogleApiClient()
                    }
                }
            }
        }
    }

    private fun checkPlayService() : Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RESOLUTION_REQUEST).show()
            }
            else {
                Toast.makeText(applicationContext, "This device is not supported", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return true
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

     fun logout(view: android.view.View){
        Firebase.auth.signOut()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onConnected(p0: Bundle?) {
        createLocationRequest()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("ERROR", "Connection failed: ${p0.errorCode}")
    }

    override fun onLocationChanged(p0: Location?) {
        GetWeather().execute(Common.apiRequest(p0!!.latitude.toString(), p0!!.longitude.toString()))
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null)
            mGoogleApiClient!!.connect()

        val currentUser = auth.currentUser
        binding.nome.text = currentUser?.displayName
        binding.email.text = currentUser?.email
        Glide.with(this).load(currentUser?.photoUrl).into(binding.profileImage)
    }

    override fun onDestroy() {
        mGoogleApiClient!!.disconnect()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        checkPlayService()
    }

    private inner class GetWeather : AsyncTask<String, Void, String>()
    {
        internal var pd = ProgressDialog(this@MainActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setTitle("Carregando")
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            var stream : String? = null
            var urlString = params[0]

            val http = Helper()
            stream = http.getGTTPData(urlString)
            return stream!!
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result!!.contains("Error: Not found city")) {
                pd.dismiss()
                return
            }
            val gson = Gson()
            val mType = object : TypeToken<OpenWeatherMap>(){}.type

            openWetaherMap = gson.fromJson<OpenWeatherMap>(result, mType)
            pd.dismiss()

            // Ajustar a informação na interface
            binding.txvCity.text = "${openWetaherMap.name}"
            binding.txvDescription.text = "${openWetaherMap.weather!![0].description!!.capitalize()}"
            binding.txvCelsius.text = "${openWetaherMap.main!!.temp} °C"
            Picasso.with(this@MainActivity)
                .load(Common.getImage(openWetaherMap.weather!![0].icon!!))
                .into(binding.imgIcon)

            val sharedPrefs = getSharedPreferences("HISTORICO", Context.MODE_PRIVATE)
            var count = sharedPrefs.getInt("COUNT", 0)
            val editPrefs = sharedPrefs.edit()

            count += 1
            editPrefs.putString("name_$count", "${openWetaherMap.name}")
            editPrefs.putString("update_$count", "${Common.dateNow}")
            editPrefs.putString("temp_$count", "${openWetaherMap.main!!.temp} °C")
            editPrefs.putInt("COUNT", count)

            editPrefs.apply()

        }

    }

    fun goRecipe(view: android.view.View) {
        startActivity(Intent(this@MainActivity, RecipeActivity::class.java))
    }
}