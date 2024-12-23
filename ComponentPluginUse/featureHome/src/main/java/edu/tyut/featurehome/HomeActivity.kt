package edu.tyut.featurehome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import edu.tyut.featurehome.databinding.ActivityHomeBinding

private const val TAG: String = "HomeActivity"
private const val NAME_KEY: String = "name"

class HomeActivity : AppCompatActivity() {

    companion object{
        /**
         * interface
         */
        fun startActivity(context: Context, name: String){
            context.startActivity(Intent(context, HomeActivity::class.java).apply {
                putExtras(bundleOf(
                    NAME_KEY to name
                ))
            })
        }
    }

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name: String = intent?.getStringExtra(NAME_KEY) ?: ""
        binding.tvName.append(name)
    }
}