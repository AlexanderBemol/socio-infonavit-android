package montanez.alexander.socioinfonavit.ui.home

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home_view.*
import kotlinx.android.synthetic.main.item_wallet.*
import kotlinx.android.synthetic.main.side_nav_header.view.*
import montanez.alexander.socioinfonavit.R
import montanez.alexander.socioinfonavit.model.entity.SessionExpiredException
import montanez.alexander.socioinfonavit.model.entity.Wallet
import org.koin.android.viewmodel.ext.android.viewModel


class HomeView : Fragment() {
    private val viewModel by viewModel<HomeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_home_view, container, false)
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = this.activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        var token = ""

        shrimmerLayout.startShimmer()

        if(sharedPreferences != null){
            val mail = sharedPreferences.getString("mail", "").toString()
            token = sharedPreferences.getString("token", "").toString()
            NAV_Menu.getHeaderView(0).side_nav_header_user_text.text = mail
        }

        toolbar_menu.setOnClickListener {
            toolbar_menu.startAnimation(
                AnimationUtils.loadAnimation(this.context, R.anim.click)
            )
            DrawerLayout.openDrawer(Gravity.LEFT)
        }
        btn_logout.setOnClickListener {
            btn_logout.startAnimation(
                AnimationUtils.loadAnimation(this.context, R.anim.click)
            )
            viewModel.logout(token)
        }

        viewModel.getData(token)

    }

    private fun observeLiveData() {
        val context = this.context
        if (context != null) {
            viewModel.error.observe(
                viewLifecycleOwner,
                {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                    if (it is SessionExpiredException) onLogOut() //si expira token se borran datos de sesión
                }
            )
            viewModel.logoutSuccess.observe(
                viewLifecycleOwner,
                { onLogOut() }
            )
            viewModel.walletList.observe(
                viewLifecycleOwner,
                {
                    onDataLoaded(it)
                }
            )
        }
    }

    private fun onLogOut(){
        this.activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
            ?.edit()
            ?.remove("logged")
            ?.remove("token")
            ?.remove("mail")
            ?.apply()

        val navController : NavController = findNavController()
        navController.navigate(R.id.action_homeView_to_loginView)
    }

    private fun onDataLoaded(data: MutableList<Wallet>){
        shrimmerLayout.hideShimmer()
        shrimmerLayout.visibility = View.GONE
        home_rv.setHasFixedSize(true)
        val context = this.context
        if(context != null){
            val layoutManager = LinearLayoutManager(context)
            val adapter = RVWalletAdapter(data as ArrayList<Wallet>, context)
            home_rv.layoutManager = layoutManager
            home_rv.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }
}