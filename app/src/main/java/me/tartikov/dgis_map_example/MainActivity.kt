package me.tartikov.dgis_map_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.dgis.sdk.ApiKeys
import ru.dgis.sdk.DGis
import ru.dgis.sdk.map.MapView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DGis.initialize(applicationContext, ApiKeys(
            map = BuildConfig.DGIS_MAP_API_KEY,
            directory = ""
        ))
        super.onCreate(savedInstanceState)
        setContentView(MapView(this))
    }
}
