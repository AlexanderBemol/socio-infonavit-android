package montanez.alexander.socioinfonavit.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import montanez.alexander.socioinfonavit.model.Generics
import montanez.alexander.socioinfonavit.model.entity.LoginUser
import montanez.alexander.socioinfonavit.model.entity.TaskResult
import montanez.alexander.socioinfonavit.model.entity.Wallet
import montanez.alexander.socioinfonavit.model.repository.User

class HomeViewModel(
    private val userRepository : User
) : ViewModel() {
    val error = MutableLiveData<Exception>()
    val logoutSuccess = MutableLiveData<Boolean>()
    val walletList = MutableLiveData<MutableList<Wallet>>()

    fun logout(token: String){
        viewModelScope.launch {
            try {
                withTimeout(Generics.TIME_OUT){
                    when(val result = userRepository.logout(token)){
                        is TaskResult.Success -> logoutSuccess.value = true
                        is TaskResult.Error -> error.value = result.e
                    }
                }
            } catch (e : Exception){
                error.value = e
            }
        }
    }

    fun getData(token: String){
        viewModelScope.launch {
            try {
                withTimeout(Generics.TIME_OUT){
                    when(val result = userRepository.getUserData(token)){
                        is TaskResult.Success -> walletList.value = result.data
                        is TaskResult.Error -> error.value = result.e
                    }
                }
            } catch (e : Exception){
                error.value = e
            }
        }
    }
}