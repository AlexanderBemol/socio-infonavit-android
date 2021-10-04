package montanez.alexander.socioinfonavit.model.source

import montanez.alexander.socioinfonavit.model.entity.LoginObject
import montanez.alexander.socioinfonavit.model.entity.LoginUser
import montanez.alexander.socioinfonavit.model.entity.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface IUser {
    @POST("login")
    suspend fun login(@Body loginObject: LoginObject) : Response<UserResponse>

    @DELETE("logout")
    suspend fun logout(@Header("Authorization") authorization : String) : Response<Unit>
}