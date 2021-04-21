package com.example.example2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BottomnavFragment3 : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            var latitude = it.getDouble("latitude")
            var longitude = it.getDouble("longitude")
            Log.d("Bottomnav2","받아온 결과 -> 위도 : ${latitude} / 경도 : ${longitude}")

            this.latitude = latitude
            this.longitude = longitude
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("BottomnavFragment3", "위도 : ${latitude}/ 경도 : ${longitude}")
        return inflater.inflate(R.layout.fragment_bottomnav3, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment3{
            return BottomnavFragment3()
        }
    }
}