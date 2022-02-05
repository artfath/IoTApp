package com.apps.iotapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.iotapp.databinding.FragmentWeatherStationBinding
import com.google.firebase.database.*


class WeatherStation : Fragment() {
    private lateinit var database:DatabaseReference
    private var _binding:FragmentWeatherStationBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentWeatherStationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database=FirebaseDatabase.getInstance().reference
        val dataListener=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempData=snapshot.child("data").child("monitoring")
                    .child("temperature").getValue()
                val humdata=snapshot.child("data").child("monitoring")
                    .child("humidity").getValue()
                val pressData=snapshot.child("data").child("monitoring")
                    .child("pressure").getValue()
                val altData=snapshot.child("data").child("monitoring")
                    .child("altitude").getValue()
                binding.tvTemperature.text=tempData.toString()
                binding.tvHumidity.text=humdata.toString()
                binding.tvPressure.text=pressData.toString()
                binding.tvAltitude.text=altData.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        database.addValueEventListener(dataListener)

    }

}