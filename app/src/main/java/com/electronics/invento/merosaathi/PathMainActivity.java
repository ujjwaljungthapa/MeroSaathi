package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class PathMainActivity extends AppCompatActivity {

    MapView map = null;

    double MAP_DEFAULT_ZOOM = 12.0;
    double MAP_DEFAULT_LATITUDE = 27.7;
    double MAP_DEFAULT_LONGITUDE = 85.3333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_main);
        map = findViewById(R.id.map);

        //This ensure that the application will work offline
        map.setUseDataConnection(false);

        //Set the tileSource, in order to use other tileSources, see https://github.com/osmdroid/osmdroid/wiki/Map-Sources
        map.setTileSource(TileSourceFactory.MAPNIK);

        //We set a point, center the map and add a marker on it
        IMapController controller = map.getController();
        GeoPoint startPoint = new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE);
        controller.setCenter(startPoint);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        //This enable more map controls
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setClickable(true);

        //Set current max and minimum zoom level
        controller.setZoom(MAP_DEFAULT_ZOOM);
        map.setMinZoomLevel(12.0);
        map.setMaxZoomLevel(16.0);
    }
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }
}
