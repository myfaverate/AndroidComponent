package edu.tyut.feature_user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.tyut.feature_user.databinding.ActivityUserBinding

private const val USERNAME_KEY: String = "username"

class UserActivity : AppCompatActivity() {

    private val binding: ActivityUserBinding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }

    companion object{
        /**
         * 启动 Activity 的标准接口
         */
        fun startActivity(context: Context, username: String){
            context.startActivity(Intent(context, UserActivity::class.java).apply {
                putExtras(bundleOf(
                    USERNAME_KEY to username
                ))
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }
    private fun initView(){
        binding.tvName.text = intent?.getStringExtra(USERNAME_KEY) ?: "default"
    }
}