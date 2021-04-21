package com.example.example2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.SharedPreferences
import android.util.Log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomnavFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomnavFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //앞서 설정했던 위도와 경도를 Argument를 확인해 받아옵니다.
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //번들로 넘겼던 객체를 가져오는 방법
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
        //위치 권한 설정

        Log.d("BottomnavFragment2", "위도 : ${latitude}/ 경도 : ${longitude}")
        return inflater.inflate(R.layout.fragment_bottomnav2, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment2{
            return BottomnavFragment2()
        }

    }


}