package br.dc.ufscar.devmobile.configs

import android.content.Context
import br.dc.ufscar.devmobile.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationService{
    @SuppressWarnings("MissingPermission")
    fun getCurrentLocation(
        context : Context,
        onSuccess : (Double, Double) -> Unit,
        onError : (String) -> Unit
    ){
        val client = LocationServices.getFusedLocationProviderClient(context)
        val cancellationTokenSource = CancellationTokenSource()

        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null){
                onSuccess(location.latitude, location.longitude)
            } else {
                client.lastLocation.addOnSuccessListener { lastLoc ->
                    if (lastLoc != null) {
                        onSuccess(lastLoc.latitude, lastLoc.longitude)
                    } else {
                        onError(context.getString(R.string.error_location_unavailable))
                    }
                }
            }
        }.addOnFailureListener {
            onError(context.getString(R.string.error_location_getting))
        }
    }
}
