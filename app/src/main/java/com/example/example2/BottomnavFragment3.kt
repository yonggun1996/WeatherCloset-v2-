package com.example.example2

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.add
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.fragment_bottomnav3.*
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_date1_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_time1_spinner
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_bottomnav3.out1_time2_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out2_date1_spinner
import kotlinx.android.synthetic.main.fragment_bottomnav3.out2_date2_spinner
import java.text.SimpleDateFormat
import java.time.Instant

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
    private var mainactivity : BottomnavMain? = null


    private var param1: String? = null
    private var param2: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onAttach(context: Context) {
        date_Arrayadapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,datelist)
        time_Arrayadapter = ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,timelist)
        super.onAttach(context)
        mainactivity = (activity as BottomnavMain)//BottomnavMain를 할당 이렇게 하지 않으면 예외가 발생한다
    }

    override fun onDetach() {
        mainactivity = null//더이상 참조를 하지 않으니 null로 초기화
        super.onDetach()
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

        //자식 프래그먼트 설정
        //childFragmentManager.beginTransaction().add(R.id.bottomnav3child,BottomnavFragment3_1()).commit()
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
                out1_date1 = out1_date1_spinner.selectedItem as String
                Log.d(TAG, "out1_date1 스피너의 데이터 : ${out1_date1}")

                if(p2 == 0){
                    out1_date2_spinner.setSelection(0)
                    out2_date1_spinner.setSelection(0)
                    out2_date2_spinner.setSelection(0)
                }else if(p2 == 1){
                    out1_date2_spinner.setSelection(1)
                    out2_date1_spinner.setSelection(1)
                    out2_date2_spinner.setSelection(1)
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
                out1_time2 = out1_time2_spinner.selectedItem as String
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

        confilm.setOnClickListener {
            if(out1_time1.equals("시간 선택") || out1_time2.equals("시간 선택")){
                Toast.makeText(context,"시간을 채워주세요", Toast.LENGTH_SHORT).show()
            }else{
                var now_time = Instant.now().epochSecond
                out1_time1_str = out1_date1 + " " + out1_time1
                Log.d(TAG,"out1_time1_str : ${out1_time1_str}")
                var out1_time1_second = set_unixtime(out1_time1_str)//시간1의 유닉스 타임
                out1_time2_str = out1_date2 + " " + out1_time2
                var out1_time2_second = set_unixtime(out1_time2_str)//시간2의 유닉스 타임

                date_time_List.add(out1_time1_second)
                date_time_List.add(out1_time2_second)

                if(now_time >= out1_time1_second || now_time >= out1_time2_second){
                    Toast.makeText(context,"현재 시간보다 앞선 시간을 설정해주세요.",Toast.LENGTH_SHORT).show()
                    date_time_List.removeAll(date_time_List)
                }else if(out1_time1_second > out1_time2_second){
                    Toast.makeText(context,"시간 설정을 알맞게 해주세요",Toast.LENGTH_SHORT).show()
                    date_time_List.removeAll(date_time_List)
                }else{
                    if(out2_date1.equals("날짜 선택") || out2_time1.equals("시간 선택") || out2_date2.equals("날짜 선택") || out2_time2.equals("시간 선택")){
                        //설정시간 2가 비어있다면 대화상자를 띄우는 코드
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("주의사항")
                        builder.setMessage("퇴근(하교)시간이 비어있부분이 있습니다." + "\n" + "출근(등교)시간을 바탕으로 확인하시겠습니까?")

                        builder.setPositiveButton("네",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                moveActivity()
                            }
                        )
                        builder.setNegativeButton("아니오",
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                Toast.makeText(context,"퇴근(하교)시간을 설정해 주세요",Toast.LENGTH_SHORT).show()
                                date_time_List.removeAll(date_time_List)
                            }
                        )

                        val alertDialog = builder.create()
                        alertDialog.show()
                    }else{
                        out2_time1_str = out2_date1 +" " + out2_time1
                        var out2_time1_second = set_unixtime(out2_time1_str)//퇴근시간 1의 유닉스타임을 구한다
                        out2_time2_str = out2_date2 + " " + out2_time2
                        var out2_time2_second = set_unixtime(out2_time2_str)//퇴근시간 2의 유닉스타임을 구한다

                        if(out1_time2_second >= out2_time1_second || out1_time2_second >= out2_time2_second){
                            Toast.makeText(context,"출근(등교)시간보다 뒤의 시간으로 설정해주세요",Toast.LENGTH_SHORT).show()
                            date_time_List.removeAll(date_time_List)
                        }else if(out2_time1_second > out2_time2_second){
                            Toast.makeText(context,"시간 설정을 알맞게 해주세요",Toast.LENGTH_SHORT).show()
                            date_time_List.removeAll(date_time_List)
                        }else{
                            Log.d(TAG,"다른 액티비티로 넘깁니다")
                            Log.d(TAG,"경도 : ${latitude} / 위도 : ${longitude}")

                            //유닉스 타임을 담은 리스트에 구한 유닉스타임을 담는다
                            date_time_List.add(out2_time1_second)
                            date_time_List.add(out2_time2_second)
                            moveActivity()
                        }
                    }
                }
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    private fun set_unixtime(date : String) : Long {
        //유닉스타임을 구해주는 함수
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시")
        val change_date = simpleDateFormat.parse(date)
        var unixtime = change_date.time / 1000
        return unixtime
    }

    private fun moveActivity(){
        if(latitude != null && longitude != null){
            mainactivity?.viewConfilmWeather(date_time_List)
        }else{
            Toast.makeText(context,"위치권한을 설정해 주세요",Toast.LENGTH_SHORT).show()
        }
    }
}