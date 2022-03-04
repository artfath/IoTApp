package com.apps.iotapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.iotapp.databinding.FragmentSmartLampBinding
import com.google.android.material.slider.Slider
import com.google.firebase.database.*


class SmartLamp : Fragment() {
    private lateinit var database:DatabaseReference
    private lateinit var brightdata:DataSnapshot
    private var _binding:FragmentSmartLampBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSmartLampBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get a database reference
        database=FirebaseDatabase.getInstance().reference
        // Read data once when start application
        readDataCondition()
        // Check condition checked button
        binding.swOnoff.setOnCheckedChangeListener { compoundButton, condition ->
            if (condition==true){
                binding.tvSwitch.text="Lamp On"
                database.child("data").child("switch").setValue("on")
                binding.tvBrightness.setTextColor(Color.parseColor("#FF1CC3F8"))
                binding.slOnoff.isEnabled=true
                energyCount(binding.slOnoff)
                sliderChange()
            }else{
                binding.tvSwitch.text="Lamp Off"
                database.child("data").child("switch").setValue("off")
                binding.tvBrightness.setTextColor(Color.parseColor("#CACACA"))
                val valBright=binding.slOnoff.value.toInt().toString()
                binding.tvBrightness.text=valBright+"%"
                binding.tvEnergy.text="0"
                database.child("data").child("brightness").setValue(valBright)
                binding.slOnoff.isEnabled=false
            }
        }


    }

    private fun readDataCondition() {
        database.child("data").child("brightness").get()
            .addOnSuccessListener {
                brightdata=it
                Log.i("firebase", "Got value ${it.value}")
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        database.child("data").child("switch").get()
            .addOnSuccessListener {
                Log.i("switch", "Got value ${it.value}")
                if (it.value=="on"){
                    binding.swOnoff.isChecked=true
                    binding.slOnoff.isEnabled=true
                    binding.tvSwitch.text="Lamp On"
                    binding.slOnoff.value=brightdata.value.toString().toFloat()
                    binding.tvBrightness.text="${brightdata.value.toString().toInt()}%"
                    binding.tvBrightness.setTextColor(Color.parseColor("#FF1CC3F8"))
                    val energyData=((brightdata.value.toString().toFloat()/100)*10)
                    binding.tvEnergy.text="%.1f".format(energyData)
                    Log.i("switch", "sukses")
                }else{
                    binding.swOnoff.isChecked=false
                    binding.slOnoff.value=brightdata.value.toString().toFloat()
                    val valBright=binding.slOnoff.value.toInt().toString()
                    binding.tvBrightness.text=valBright+"%"
                    binding.tvBrightness.setTextColor(Color.parseColor("#CACACA"))
                    binding.slOnoff.isEnabled=false
                    binding.tvSwitch.text="Lamp Off"
                    binding.tvEnergy.text="0"
                }


            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
    }




    private fun sliderChange() {
        binding.slOnoff.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                val valBright=slider.value.toInt().toString()
                binding.tvBrightness.text=valBright+"%"
                database.child("data").child("brightness").setValue(valBright)
                energyCount(slider)
            }
            override fun onStopTrackingTouch(slider: Slider) {
                val valBright=slider.value.toInt().toString()
                binding.tvBrightness.text=valBright+"%"
                database.child("data").child("brightness").setValue(valBright)
                energyCount(slider)
            }

        })
    }

    private fun energyCount(slider: Slider) {
        val energyData=((slider.value/100)*10)
        binding.tvEnergy.text="%.1f".format(energyData)
    }




}