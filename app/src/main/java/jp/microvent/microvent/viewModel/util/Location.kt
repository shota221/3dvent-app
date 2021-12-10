package jp.microvent.microvent.viewModel.util

import android.content.Context
import com.google.android.gms.location.*

class Location(val context: Context) {
    var latitude: String? = null
    var longitude: String? = null
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun setLocation() {

        fusedLocationClient
            .lastLocation
            .addOnSuccessListener {
                if (it != null) {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()
                } else {
                    //以下、端末で一度も位置情報を取得したことがない場合にnullが帰ってこないようにrequestLocationUpdateを走らせる
                    val request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(500)
                        .setFastestInterval(300)

                    fusedLocationClient
                        .requestLocationUpdates(
                            request,
                            object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    val location = result.lastLocation
                                    latitude = location.latitude.toString()
                                    longitude = location.longitude.toString()

                                    // 現在地だけ欲しいので、1回取得したらすぐに外す
                                    fusedLocationClient.removeLocationUpdates(this)
                                }
                            },
                            null
                        )
                }
            }
    }
}