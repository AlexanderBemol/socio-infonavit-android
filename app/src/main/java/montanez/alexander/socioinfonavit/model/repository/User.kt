package montanez.alexander.socioinfonavit.model.repository

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import montanez.alexander.socioinfonavit.model.Generics
import montanez.alexander.socioinfonavit.model.entity.*
import montanez.alexander.socioinfonavit.model.source.IUser
import montanez.alexander.socioinfonavit.model.source.IUserData
import retrofit2.HttpException
import kotlin.collections.*

class User(
    private val userSource: IUser,
    private val userDataSource: IUserData,
) {

    suspend fun login (user: LoginUser) : TaskResult<UserResponse>{
        val userObject = LoginObject(user)
        return try {
            withContext(Dispatchers.IO){
                withTimeout(Generics.TIME_OUT){
                    val result = userSource.login(userObject)
                    val userResponse = result.body()
                    if(result.code() == 200 && userResponse != null){
                        val token = result.headers().get("Authorization").toString()
                        val error = userResponse.error
                        userResponse.token = token
                        if (error == null) TaskResult.Success(userResponse)
                        else TaskResult.Error(LoginError(error))
                    } else {
                        val response = Gson().fromJson(result.errorBody()?.string(),UserResponse::class.java)
                        TaskResult.Error(
                            LoginError(response?.error!!)
                        )
                    }
                }
            }
        } catch (e : Exception){
            if(e is HttpException){
                if(e.code() == 401) {
                    val response = Gson().fromJson(e.response()?.errorBody()!!.string(),UserResponse::class.java)
                    TaskResult.Error(
                        LoginError(response?.error!!)
                    )
                }
                else TaskResult.Error(e)
            }else TaskResult.Error(e)
        }
    }

    suspend fun logout(token: String) : TaskResult<Unit>{
        return try {
            withContext(Dispatchers.IO){
                withTimeout(Generics.TIME_OUT){
                    val result = userSource.logout(token)
                    if(result.code() == 200 || result.code() == 401){ //401 porque significa que expiró
                         TaskResult.Success(Unit)
                    } else {
                        val response = Gson().fromJson(result.errorBody()?.string(),UserResponse::class.java)
                        TaskResult.Error(
                            LogoutError(response?.error!!)
                        )
                    }
                }
            }
        } catch (e : Exception){
            if(e is HttpException){
                if(e.code() == 401) {
                    //exitoso porque le expiró el token
                    TaskResult.Success(Unit)
                }
                else TaskResult.Error(e)
            }else TaskResult.Error(e)
        }
    }

    suspend fun getUserData(token: String) : TaskResult<MutableList<Wallet>>{
        return try {
            withContext(Dispatchers.IO){
                withTimeout(Generics.TIME_OUT){
                    val walletResponse = userDataSource.getWallets(token)
                    if(walletResponse.code() == 401) TaskResult.Error(SessionExpiredException())
                    else{
                        val benevitsResponse = userDataSource.getBenevits(token)
                        val wallets = walletResponse.body()
                        val benevits = benevitsResponse.body()
                        if(benevitsResponse.code() == 401) TaskResult.Error(SessionExpiredException())
                        else if (walletResponse.code() != 200 || benevitsResponse.code() != 200) TaskResult.Error(UnknownException("Error Desconocido"))
                        else if (wallets != null && benevits != null){
                            val fixedData = mutableListOf<Wallet>()
                            for (w in wallets){
                                val lockedOnes = benevits.locked.filter { it.wallet.id == w.id }
                                val unlockedOnes= benevits.unlocked.filter { it.wallet.id == w.id }
                                lockedOnes.map { it.locked = true }
                                unlockedOnes.map { it.locked = false }
                                val allBens = lockedOnes + unlockedOnes
                                w.benevits = allBens
                                fixedData.add(w)
                            }
                            TaskResult.Success(fixedData)
                        }else TaskResult.Error(UnknownException("Error Desconocido"))
                    }
                }
            }
        } catch (e : Exception){
            if(e is HttpException){
                if(e.code() == 401) {
                    TaskResult.Error(SessionExpiredException())
                }
                else TaskResult.Error(e)
            }else TaskResult.Error(e)
        }
    }
}