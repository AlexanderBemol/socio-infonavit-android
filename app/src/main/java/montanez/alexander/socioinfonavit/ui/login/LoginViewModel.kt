package montanez.alexander.socioinfonavit.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import montanez.alexander.socioinfonavit.model.Generics
import montanez.alexander.socioinfonavit.model.entity.EmptyInputException
import montanez.alexander.socioinfonavit.model.entity.LoginUser
import montanez.alexander.socioinfonavit.model.entity.TaskResult
import montanez.alexander.socioinfonavit.model.entity.UserResponse
import montanez.alexander.socioinfonavit.model.repository.User

class LoginViewModel(private val userRepository : User) : ViewModel() {
    val error = MutableLiveData<Exception>()
    val success = MutableLiveData<UserResponse>()

    fun loginWithMail(mail:String, password:String){
        if(mail.isBlank()||password.isBlank()){
            error.value = EmptyInputException()
        }else{
            viewModelScope.launch {
                try {
                    withTimeout(Generics.TIME_OUT){
                        when(val result = userRepository.login(LoginUser(mail,password))){
                            is TaskResult.Success -> success.value = result.data
                            is TaskResult.Error -> error.value = result.e
                        }
                    }
                } catch (e : Exception){
                    error.value = e
                }
            }
        }
    }

}