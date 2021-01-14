package com.example.sideproject.ui.dashboard

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sideproject.R
import com.example.sideproject.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {
    //private lateinit var dashboardViewModel: DashboardViewModel

    var out1_date1 : String? = null
    var out1_time1 : String? = null
    var out1_date2 : String? = null
    var out1_time2 : String? = null
    var out2_date1 : String? = null
    var out2_time1 : String? = null
    var out2_date2 : String? = null
    var out2_time2 : String? = null
    private val REQUEST_CODE_LOCATION = 2
    var latitude : Double? = null
    var longitude : Double? = null
    var flag = false

    companion object{
        const val  TAG : String = "LOG"

        //자기 자신의 인스턴스를 가져오기 위한 생성자
        fun newInstance() : DashboardFragment{
            return DashboardFragment()
        }
    }

    //프래그먼트가 메모리에 올라갔을 떄
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"대시보드 프레그먼트 - onCreate")


    }

    //프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(TAG,"대시보드 프레그먼트 - onAttach")
    }

    //뷰가 생성됐을 때 화면과 연결
    //프래그먼트와 레이아웃을 연결해주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel =
            //ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        //val textView: TextView = root.findViewById(R.id.text_dashboard)
        //dashboardViewModel.text.observe(this, Observer {
            //textView.text = it
        //})

        Log.d(TAG,"대시보드 프레그먼트 - onCreateView")

        var out1_datespiner1: Spinner
        out1_datespiner1 = root.findViewById(R.id.out1_datespiner1)

        out1_datespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date1 = out1_datespiner1.selectedItem as String
            }

        }

        var out1_timespiner1: Spinner
        out1_timespiner1 = root.findViewById(R.id.out1_datespiner1)
        out1_timespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time1 = out1_timespiner1.selectedItem as String
            }
        }

        var out1_datespiner2: Spinner
        out1_datespiner2 = root.findViewById(R.id.out1_datespiner2)
        out1_datespiner2.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date2 = out1_datespiner2.selectedItem as String
            }
        }

        var out1_timespiner2: Spinner
        out1_timespiner2 = root.findViewById(R.id.out1_timespiner2)
        out1_timespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time2 = out1_timespiner2.selectedItem as String
            }
        }

        var out2_datespiner1: Spinner
        out2_datespiner1 = root.findViewById(R.id.out2_datespiner1)
        out2_datespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date1 = out2_datespiner1.selectedItem as String
            }
        }

        var out2_timespiner1: Spinner
        out2_timespiner1 = root.findViewById(R.id.out2_timespiner1)

        out2_timespiner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time1 = out2_timespiner1.selectedItem as String
            }
        }

        var out2_datespiner2: Spinner
        out2_datespiner2 = root.findViewById(R.id.out2_datespiner2)

        out2_datespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date2 = out2_datespiner2.selectedItem as String
            }
        }

        var out2_timespiner2: Spinner
        out2_timespiner2 = root.findViewById(R.id.out2_timespiner2)
        out2_timespiner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time2 = out2_timespiner2.selectedItem as String
            }
        }

        var confilm: Button
        confilm = root.findViewById(R.id.confilm)
        confilm.setOnClickListener{
            if(out1_date1.equals("선택") || out1_time1.equals("선택") || out1_date2.equals("선택") || out1_time2.equals("선택")){
                Log.d(TAG,"선택이 안된 스피너가 있습니다.")
                layout_overcoat.visibility = View.INVISIBLE
                layout_shirt.visibility = View.INVISIBLE
                layout_pants.visibility = View.INVISIBLE
            }else{
                Log.d(TAG,"완료")
                layout_overcoat.visibility = View.VISIBLE
                layout_shirt.visibility = View.VISIBLE
                layout_pants.visibility = View.VISIBLE
            }
        }
        return root
    }


}