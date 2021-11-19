package br.com.raulreis.recipe

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import br.com.raulreis.recipe.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    private var PERMISSION_ID = 1000
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val txvTeste = binding.txvTeste
        txvTeste.text = "Teste"

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    private fun getLastLocation() {
        if(checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null) {
                        getNewLocation()
                    }
                    else {
                        binding.txvTeste.text = "Lat:${location.latitude}\nLong:${location.longitude}"
                    }
                }
            }
            else {
                Toast.makeText(this,"Please Enable your Location Service", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            requestPermission()
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
            if (checkPermission()) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
        )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lasLocation = p0.lastLocation
            binding.txvTeste.text = "Lat:${lasLocation.latitude}\nLong:${lasLocation.longitude}"
        }
    }

    // Checando a permissão
    private fun checkPermission() : Boolean {
        if (
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID)
    }

    private fun isLocationEnabled() : Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You have the permission")
            }
        }
    }
}