package montanez.alexander.socioinfonavit.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_splash_view.*
import montanez.alexander.socioinfonavit.R

class SplashView : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        val navController : NavController = findNavController()
        val sharedPreferences = this.activity?.getSharedPreferences("user", Context.MODE_PRIVATE)

        splash_icon.startAnimation(
            AnimationUtils.loadAnimation(this.context,R.anim.fade_in)
        )

        Handler().postDelayed({
            if(sharedPreferences != null){
                val logged = sharedPreferences.getBoolean("logged",false)
                val token = sharedPreferences.getString("token","").toString()
                if(logged && token.isNotEmpty()) navController.navigate(R.id.action_splashView_to_homeView)
                else navController.navigate(R.id.action_splashView_to_loginView)
            } else navController.navigate(R.id.action_splashView_to_loginView)
        }, 2000)
    }
}