package me.tartikov.dgis_map_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.dgis.sdk.DGis
import ru.dgis.sdk.geometry.GeoPointWithElevation
import ru.dgis.sdk.map.SnapToMapLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DGis.initialize(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.message).apply {
            layoutParams = SnapToMapLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                GeoPointWithElevation(55.740444, 37.619524)
            )
        }
    }
}
