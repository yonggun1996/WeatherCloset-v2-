package com.example.example2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_bottomnav3.*
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_date1_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_time1_spinner
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_time2_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out2_date1_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out2_date2_spinner

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BottomnavFragment3 : Fragment() {

    private val TAG = "LoginActivity"

    var now = LocalDate.now()
    var date1 = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
    var date2 = now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

    val datelist : List<String> = listOf(date1,date2)
    val timelist : List<String> = listOf("시간 선택","0시","1시","2시","3시","4시","5시","6시","7시","8시","9시","10시","11시","12시",
        "13시","14시","15시","16시","17시","18시","19시","20시","21시","22시","23시")

    lateinit var out1_date1 : String
    lateinit var out1_time1 : String
    lateinit var out1_date2 : String
    lateinit var out1_time2 : String
    lateinit var out2_date1 : String
    lateinit var out2_time1 : String
    lateinit var out2_date2 : String
    lateinit var out2_time2 : String
    lateinit var out1_time1_str : String
    lateinit var out1_time2_str : String
    lateinit var out2_time1_str : String
    lateinit var out2_time2_str : String
    private val REQUEST_CODE_LOCATION = 2
    lateinit var date_Arrayadapter : ArrayAdapter<String>
    lateinit var time_Arrayadapter : ArrayAdapter<String>
    var flag = false
    var date_time_List : ArrayList<Long> = ArrayList<Long>()


    private var param1: String? = null
    private var param2: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onAttach(context: Context) {
        date_Arrayadapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,datelist)
        time_Arrayadapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,timelist)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        out1_date1_spinner.adapter = date_Arrayadapter
        out1_date1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date1 = out1_time1_spinner.selectedItem as String
                Log.d(TAG, "out1_date1 스피너의 데이터 : ${out1_date1}")

                if(p2 == 0){
                    out1_date2_spinner.setSelection(0)
                    out2_time1_spinner.setSelection(0)
                    out2_time2_spinner.setSelection(0)
                }else if(p2 == 1){
                    out1_date2_spinner.setSelection(1)
                    out2_time1_spinner.setSelection(1)
                    out2_time2_spinner.setSelection(1)
                }
            }

        }

        out1_time1_spinner.adapter = time_Arrayadapter
        out1_time1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time1 = out1_time1_spinner.selectedItem as String
                Log.d(TAG, "out1_time1 스피너의 데이터 : ${out1_time1}")
            }
        }

        out1_date2_spinner.adapter = date_Arrayadapter
        out1_date2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date2 = out1_date2_spinner.selectedItem as String
                Log.d(TAG, "out1_date2 스피너의 데이터 : ${out1_date2}")
            }
        }

        out1_time2_spinner.adapter = time_Arrayadapter
        out1_time2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time2 = out1_date2_spinner.selectedItem as String
                Log.d(TAG, "out1_time2 스피너의 데이터 : ${out1_time2}")
            }
        }

        out2_date1_spinner.adapter = date_Arrayadapter
        out2_date1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date1 = out2_date1_spinner.selectedItem as String
                Log.d(TAG, "out2_date1 스피너의 데이터 : ${out2_date1}")

                if(p2 == 0){
                    out2_date2_spinner.setSelection(0)
                }else if(p2 == 1){
                    out2_date2_spinner.setSelection(1)
                }
            }
        }

        out2_time1_spinner.adapter = time_Arrayadapter
        out2_time1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time1 = out2_time1_spinner.selectedItem as String
                Log.d(TAG, "out2_time1 스피너의 데이터 : ${out2_time1}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        out2_date2_spinner.adapter = date_Arrayadapter
        out2_date2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date2 = out2_date2_spinner.selectedItem as String
                Log.d(TAG, "out2_date2 스피너의 데이터 : ${out2_date2}")
            }
        }

        out2_time2_spinner.adapter = time_Arrayadapter
        out2_time2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time2 = out2_time2_spinner.selectedItem as String
                Log.d(TAG, "out2_time2 스피너의 데이터 : ${out2_time2}")
            }
        }

        super.onActivityCreated(savedInstanceState)
    }
}