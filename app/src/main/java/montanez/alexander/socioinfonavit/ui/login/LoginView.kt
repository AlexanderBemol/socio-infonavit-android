package montanez.alexander.socioinfonavit.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login_view.*
import montanez.alexander.socioinfonavit.R
import montanez.alexander.socioinfonavit.model.entity.UserResponse
import org.koin.android.viewmodel.ext.android.viewModel

class LoginView: Fragment() {
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_login_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    private fun initListeners() {
        ETMail.doOnTextChanged { _, _, _, _ -> onTextChanged() }
        ETPassword.doOnTextChanged { _, _, _, _ -> onTextChanged() }
        ETPassword.setOnEditorActionListener{ _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE){
                if(BTEnter.isEnabled) BTEnter.performClick()
            }
            false
        }

        BTEnter.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            BTEnter.isEnabled = false
            viewModel.loginWithMail(ETMail.text.toString(),ETPassword.text.toString())
        }
    }

    private fun onTextChanged(){
        BTEnter.isEnabled = ETMail.text.isNotEmpty() && ETPassword.text.isNotEmpty() && progressBar.visibility == View.INVISIBLE
    }

    private fun onLoadFinished(){
        progressBar.visibility = View.INVISIBLE
        onTextChanged()
    }

    private fun onSuccess(userResponse: UserResponse){
        this.activity?.getSharedPreferences("user",Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean("logged",true)
            ?.putString("token",userResponse.token)
            ?.putString("mail",userResponse.email)
            ?.apply()

        val navController : NavController = findNavController()
        navController.navigate(R.id.action_loginView_to_homeView)
    }

    private fun observeLiveData(){
        val context = this.context
        if (context != null){
            viewModel.error.observe(
                viewLifecycleOwner,
                {
                    onLoadFinished()
                    Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
            )
            viewModel.success.observe(
                viewLifecycleOwner,
                {
                    onLoadFinished()
                    onSuccess(it)
                }
            )

        }
    }
}