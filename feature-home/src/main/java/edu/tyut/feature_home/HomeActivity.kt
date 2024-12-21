package edu.tyut.feature_home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val TAG: String = "HomeActivity"

class HomeActivity : AppCompatActivity(){

    companion object{
        /**
         * 启动 Activity 标准接口
         */
        fun startActivity(context: Context){
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}