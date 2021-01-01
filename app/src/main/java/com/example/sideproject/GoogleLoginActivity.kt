package com.example.sideproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.security.MessageDigest

class GoogleLoginActivity : AppCompatActivity(), View.OnClickListener{

    override fun onClick(p0: View?) {//버튼에 대한 리스너
        //R.id.loginG -> signIn()
    }

    private val TAG = "GoogleLoginActivity"
    //private lateinit var loginG : SignInButton//버튼
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginG.setOnClickListener{
            signIn()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        /*loginG.setOnClickListener{
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }*/

    }

    //유저가 이미 로그인 되어 있으면 MainActivity로 이동
    public override fun onStart(){
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!==null){
            toMainActivity(auth.currentUser)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //firebase에 사용자 정보를 전달하는 코드
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    //firebase에 구글 계정을 전달하는 코드
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "로그인성공")
                    toMainActivity(auth?.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "로그인에 실패했습니다.",Toast.LENGTH_SHORT).show()
                    // ...
                    //Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                }

                // ...
            }
    }

    //메인엑티비티로 이동하는 메서드
    fun toMainActivity(user: FirebaseUser?){
        if(user !== null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

}