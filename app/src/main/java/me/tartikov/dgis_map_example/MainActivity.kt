package me.tartikov.dgis_map_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dgis.sdk.ApiKeys
import ru.dgis.sdk.DGis
import ru.dgis.sdk.File
import ru.dgis.sdk.coordinates.GeoPoint
import ru.dgis.sdk.map.CameraPosition
import ru.dgis.sdk.map.MapOptions
import ru.dgis.sdk.map.MapView
import ru.dgis.sdk.map.Zoom

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sdkContext = DGis.initialize(applicationContext, ApiKeys(
            map = BuildConfig.DGIS_MAP_API_KEY,
            directory = ""
        ))
        super.onCreate(savedInstanceState)

        val options = MapOptions().apply {
            position = CameraPosition(
                GeoPoint(55.740444, 37.619524),
                Zoom(9.0f)
            )
            styleFile = File.fromAsset(sdkContext, "dgis_style/style.2gis")
        }
        setContentView(MapView(this, options))
    }
}
