package com.example.example2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    var now = LocalDate.now()
    var date1 = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
    var date2 = now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

    val datelist : List<String> = listOf(date1,date2)
    val timelist : List<String> = listOf("시간 선택","0시","1시","2시","3시","4시","5시","6시","7시","8시","9시","10시","11시","12시",
                                            "13시","14시","15시","16시","17시","18시","19시","20시","21시","22시","23시")

    var sex_code = 0
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
    var flag = false
    var latitude : Double = 1.0
    var longitude : Double = 1.0
    var date_time_List : ArrayList<Long> = ArrayList<Long>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.titlebar_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_reset ->{
                out1_date1_spinner.setSelection(0)
                out1_time1_spinner.setSelection(0)
                out1_date2_spinner.setSelection(0)
                out1_time2_spinner.setSelection(0)
                out2_date1_spinner.setSelection(0)
                out2_time1_spinner.setSelection(0)
                out2_date2_spinner.setSelection(0)
                out2_time2_spinner.setSelection(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Weather & Closet")
        Log.d(TAG, "main으로 넘어왔습니다.")
        supportActionBar?.setLogo(R.drawable.appicon)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//세로 고정

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }

        out1_date1_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datelist)
        out1_date1_spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date1 = out1_date1_spinner.selectedItem as String

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

        out1_time1_spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,timelist)
        out1_time1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time1 = out1_time1_spinner.selectedItem as String
            }
        }

        out1_date2_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datelist)
        out1_date2_spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_date2 = out1_date2_spinner.selectedItem as String
            }
        }

        out1_time2_spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,timelist)
        out1_time2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out1_time2 = out1_time2_spinner.selectedItem as String
            }
        }


        out2_date1_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datelist)
        out2_date1_spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date1 = out2_date1_spinner.selectedItem as String

                if(p2 == 0){
                    out2_date2_spinner.setSelection(0)
                }else if(p2 == 1){
                    out2_date2_spinner.setSelection(1)
                }
            }
        }

        out2_time1_spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,timelist)
        out2_time1_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time1 = out2_time1_spinner.selectedItem as String
            }
        }

        out2_date2_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datelist)
        out2_date2_spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_date2 = out2_date2_spinner.selectedItem as String
            }
        }

        out2_time2_spinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,timelist)
        out2_time2_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                out2_time2 = out2_time2_spinner.selectedItem as String
            }
        }

        //위치권한 설정
        if(isLocationPermissionGranted()){
            getCurrentLocation()
        }

        sex_Radio.setOnCheckedChangeListener{radioGroup, i ->
            when(i){
                R.id.male -> sex_code = 1
                R.id.female -> sex_code = 2
            }

        }//성별 설정

        //현재시간 설정으로 했을 경우
        nowtime_confirm.setOnClickListener {
            if(sex_code != 0){
                if(flag){//위치를 수락한 경우
                    Log.d(TAG,"다른 액티비티로 넘깁니다")
                    Log.d(TAG,"경도 : ${latitude} / 위도 : ${longitude}")

                    val nextIntent = Intent(this,NowWeatherActivity::class.java)
                    nextIntent.putExtra("latitude",latitude)
                    nextIntent.putExtra("longitude",longitude)
                    nextIntent.putExtra("sex_code",sex_code)
                    nextIntent.putExtra("now_month",now.monthValue)

                    startActivity(nextIntent)
                }else{
                    Toast.makeText(this,"위치권한을 설정해 주세요", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"성별을 선택해 주세요", Toast.LENGTH_SHORT).show()
            }

        }

        //설정시간으로 선택한 경우
        confilm.setOnClickListener{

            Log.d(TAG,"성별코드 : ${sex_code}")

            if(sex_code != 0){//성별이 선택된 경우
                if(out1_date1.equals("날짜 선택") || out1_time1.equals("시간 선택") || out1_date2.equals("날짜 선택") || out1_time2.equals("시간 선택")){
                    //설정시간 1의 빈 데이터가 있을 때
                    Toast.makeText(this,"빈 데이터를 채워주세요",Toast.LENGTH_SHORT).show()
                }else{
                    //설정시간 1의 빈 데이터가 없을 때
                    var now_time = Instant.now().epochSecond//현재 unixtime
                    out1_time1_str = out1_date1 + " " + out1_time1
                    var out1_time1_second = set_unixtime(out1_time1_str)//시간1의 유닉스 타임
                    out1_time2_str = out1_date2 + " " + out1_time2
                    var out1_time2_second = set_unixtime(out1_time2_str)//시간2의 유닉스 타임
                    Log.d(TAG,"시간 2 : ${out1_time2_second}")

                    //유닉스타임을 담은 리스트에 담는다
                    date_time_List.add(out1_time1_second)
                    date_time_List.add(out1_time2_second)
                    if(now_time >= out1_time1_second || now_time >= out1_time2_second){
                        Toast.makeText(this,"현재 시간보다 앞선 시간을 설정해주세요.",Toast.LENGTH_SHORT).show()
                        date_time_List.removeAll(date_time_List)
                    }else if(out1_time1_second > out1_time2_second){
                        Toast.makeText(this,"시간 설정을 알맞게 해주세요",Toast.LENGTH_SHORT).show()
                        date_time_List.removeAll(date_time_List)
                    }else{
                        if(out2_date1.equals("날짜 선택") || out2_time1.equals("시간 선택") || out2_date2.equals("날짜 선택") || out2_time2.equals("시간 선택")){
                            //설정시간 2가 비어있다면 대화상자를 띄우는 코드
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("주의사항")
                            builder.setMessage("퇴근(하교)시간이 비어있부분이 있습니다." + "\n" + "출근(등교)시간을 바탕으로 확인하시겠습니까?")

                            builder.setPositiveButton("네",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    moveActivity()
                                }
                            )
                            builder.setNegativeButton("아니오",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    Toast.makeText(this,"퇴근(하교)시간을 설정해 주세요",Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(this,"출근(등교)시간보다 뒤의 시간으로 설정해주세요",Toast.LENGTH_SHORT).show()
                                date_time_List.removeAll(date_time_List)
                            }else if(out2_time1_second > out2_time2_second){
                                Toast.makeText(this,"시간 설정을 알맞게 해주세요",Toast.LENGTH_SHORT).show()
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
            }else{//성별이 선택이 안된 경우
                Toast.makeText(this,"성별을 선택해 주세요", Toast.LENGTH_SHORT).show()
            }

        }//confilm 클릭 리스너
    }

    private fun moveActivity(){
        //다른 액티비티로 넘기는 함수
        if (flag){//위치를 수락한 경우
            Log.d(TAG,"다른 액티비티로 넘깁니다")
            Log.d(TAG,"경도 : ${latitude} / 위도 : ${longitude}")
            val nextIntent = Intent(this,WeatherActivity::class.java)
            nextIntent.putExtra("latitude",latitude)
            nextIntent.putExtra("longitude",longitude)
            nextIntent.putExtra("date_List",date_time_List)
            nextIntent.putExtra("sex_code",sex_code)
            nextIntent.putExtra("now_month",now.monthValue)
            startActivity(nextIntent)
            //finish()
            date_time_List.removeAll(date_time_List)
        }else{
            Toast.makeText(this,"위치권한을 설정해 주세요.",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(this,"권한이 없어 해당 기능을 실행할 수 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCurrentLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location !== null){//위치가 파악된 경우
                Log.d(TAG,"위치를 찾았습니다.")
                flag = true
                latitude = location.latitude
                longitude = location.longitude
            }else{
                Toast.makeText(this,"위치를 알 수 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean{
        val preference = getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck",true)
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //거부를 했을 때 사용자에게 왜 필요한지 이유를 설명
                Toast.makeText(this,"날씨를 알기 위해선 위치권한이 필요합니다.",Toast.LENGTH_SHORT).show()
            }else{
                if(isFirstCheck){
                    //앱을 처음 실행한 경우
                    preference.edit().putBoolean("isFirstPermissionCheck",false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE_LOCATION)
                }else{
                    Toast.makeText(this,"위치가 파악이 안돼 날씨 데이터를 가져올 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }else{
            return true
        }

    }

    private fun set_unixtime(date : String) : Long {
        //유닉스타임을 구해주는 함수
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시")
        val change_date = simpleDateFormat.parse(date)
        var unixtime = change_date.time / 1000
        return unixtime
    }

}