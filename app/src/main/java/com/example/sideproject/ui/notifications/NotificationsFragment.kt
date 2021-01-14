package com.example.sideproject.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sideproject.R
import com.example.sideproject.ui.dashboard.DashboardFragment
import com.example.sideproject.ui.dashboard.DashboardViewModel

class NotificationsFragment : Fragment() {

    lateinit var notificationsViewModel: NotificationsViewModel

    companion object{
        const val  TAG : String = "LOG"

        //자기 자신의 인스턴스를 가져오기 위한 생성자
        fun newInstance() : NotificationsFragment {
            return NotificationsFragment()
        }
    }

    //프래그먼트가 메모리에 올라갔을 떄
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"노티피캐이션 프레그먼트 - onCreate")
    }

    //프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(TAG,"노티피캐이션 프레그먼트 - onAttach")
    }

    //뷰가 생성됐을 때 화면과 연결
    //프래그먼트와 레이아웃을 연결해주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //notificationsViewModel =
        //ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        Log.d(TAG,"노티피캐이션 프레그먼트 - onCreateView")
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        //val textView: TextView = root.findViewById(R.id.sex_textview)
        //notificationsViewModel.text.observe(this, Observer {
            //textView.text = it
        //})
        return root
    }
}