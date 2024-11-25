package com.example.storyapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.ui.main.ViewModelFactory
import com.example.storyapp.utils.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.getStoriesWithLocation().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE // Tampilkan progress bar
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                    val stories = result.data.listStory
                    for (story in stories) {
                        story.lat?.let { lat ->
                            story.lon?.let { lon ->
                                val position = LatLng(lat, lon as Double)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(story.name) // Nama cerita
                                        .snippet(story.description) // Deskripsi cerita
                                )
                                boundsBuilder.include(position) // Tambahkan ke boundsBuilder
                            }
                        }
                    }
                    // Atur tampilan peta agar mencakup semua marker
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Menambahkan listener untuk info window
        mMap.setOnInfoWindowClickListener { marker ->
            Toast.makeText(this, "Klik pada: ${marker.title}", Toast.LENGTH_SHORT).show()
        }
    }
}
