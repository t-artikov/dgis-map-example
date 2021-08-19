package me.tartikov.dgis_map_example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.dgis.sdk.ApiKeys
import ru.dgis.sdk.Context
import ru.dgis.sdk.DGis
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.map.*
import ru.dgis.sdk.map.Map
import ru.dgis.sdk.navigation.NavigationManager
import ru.dgis.sdk.positioning.registerPlatformLocationSource
import ru.dgis.sdk.routing.RouteOptions
import ru.dgis.sdk.routing.RouteSearchPoint

class MainActivity : AppCompatActivity(), TouchEventsObserver {
    private lateinit var sdkContext: Context
    private lateinit var map: Map
    private var navigator: NavigationManager? = null
    private val closeables = mutableListOf<AutoCloseable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        closeables.forEach { it.close() }
        closeables.clear()
    }

    private fun create() {
        sdkContext = DGis.initialize(
            applicationContext, ApiKeys(
                map = BuildConfig.DGIS_MAP_API_KEY,
                directory = ""
            )
        )
        registerPlatformLocationSource(sdkContext, ManagerLocationSource(applicationContext))
        val mapView = MapView(this, MapOptions().apply {
            position = CameraPosition(
                point = GeoPoint(55.740444, 37.619524),
                zoom = Zoom(9.5f))
        })
        setContentView(mapView)
        mapView.getMapAsync {
            map = it
            map.addSource(
                MyLocationMapObjectSource(sdkContext,
                    MyLocationDirectionBehaviour.FOLLOW_SATELLITE_HEADING,
                    createSmoothMyLocationController())
            )
            map.setAttribute("navigatorOn", AttributeValue(true))
            mapView.setTouchEventsObserver(this)
        }
    }

    override fun onLongTouch(point: ScreenPoint) {
        val position = map.camera.projection.screenToMap(point) ?: return
        startNavigation(position)
    }

    private fun startNavigation(targetPosition: GeoPoint) {
        if (navigator == null) {
            navigator = NavigationManager(sdkContext, listOf(map))
        }
        navigator!!.apply {
            start(RouteSearchPoint(targetPosition), RouteOptions())
            closeables += uiModel.maxSpeedLimitChannel.connect {
                Log.w("APP", "maxSpeed: $it")
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        val granted = permissions.map { ContextCompat.checkSelfPermission(this, it) }
            .all { it == PackageManager.PERMISSION_GRANTED }

        if (!granted) {
            ActivityCompat.requestPermissions(this, permissions, 123)
        } else {
            create()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        create()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
