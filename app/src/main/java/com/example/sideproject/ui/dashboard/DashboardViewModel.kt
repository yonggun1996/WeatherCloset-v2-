package com.example.sideproject.ui.dashboard

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class MainActivity : AppCompatActivity() {

    val date : Array<String> = arrayOf("선택","오늘","내일")
    val time = arrayOf("선택","0시","1시","2시","3시","4시","5시","6시","7시","8시","9시","10시","11시","12시",
        "13시","14시","15시","16시","17시","18시","19시","20시","21시","22시","23시")

    lateinit var out1_date1 : String
    lateinit var out1_time1 : String
    lateinit var out1_date2 : String
    lateinit var out1_time2 : String
    lateinit var out2_date1 : String
    lateinit var out2_time1 : String
    lateinit var out2_date2 : String
    lateinit var out2_time2 : String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //각 스피너의 데이터를 입력받는 코드
        out1_datespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date1 = out1_datespiner1.selectedItem as String
            }

        }

        out1_timespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time1 = out1_timespiner1.selectedItem as String
            }

        }

        out1_datespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date2 = out1_datespiner2.selectedItem as String
            }

        }

        out1_timespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time2 = out1_timespiner2.selectedItem as String
            }

        }

        out2_datespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date1 = out2_datespiner1.selectedItem as String
            }

        }

        out2_timespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time1 = out2_timespiner1.selectedItem as String
            }

        }

        out2_datespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date2 = out2_datespiner2.selectedItem as String
            }

        }

        out2_timespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time2 = out2_timespiner2.selectedItem as String
            }

        }

        //버튼 이벤트
        confilm.setOnClickListener{
            if(out1_date1.equals("선택") || out1_time1.equals("선택") || out1_date2.equals("선택") || out1_time2.equals("선택")){
                Toast.makeText(this, "외출기간 1의 빈 데이터를 채워주세요",Toast.LENGTH_SHORT).show()
                layout_overcoat.visibility = View.INVISIBLE
                layout_shirt.visibility = View.INVISIBLE
                layout_pants.visibility = View.INVISIBLE
            }else{
                Toast.makeText(this, "${out1_time1} ~ ${out1_time2}",Toast.LENGTH_SHORT).show()
                layout_overcoat.visibility = View.VISIBLE
                layout_shirt.visibility = View.VISIBLE
                layout_pants.visibility = View.VISIBLE
            }

        }
    }





}