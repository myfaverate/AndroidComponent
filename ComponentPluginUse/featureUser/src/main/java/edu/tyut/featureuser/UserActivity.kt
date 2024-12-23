package edu.tyut.featureuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import edu.tyut.featureuser.databinding.ActivityUserBinding

private const val TAG: String = "UserActivity"
private const val NAME_KEY: String = "name"

class UserActivity : AppCompatActivity() {
    companion object{
        /**
         * interface
         */
        fun startActivity(context: Context, name: String){
            context.startActivity(Intent(context, UserActivity::class.java).apply {
                putExtras(bundleOf(
                    NAME_KEY to name
                ))
            })
        }
    }

    private val binding: ActivityUserBinding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name: String = intent?.getStringExtra(NAME_KEY) ?: ""
        binding.tvName.append(name)
    }
}