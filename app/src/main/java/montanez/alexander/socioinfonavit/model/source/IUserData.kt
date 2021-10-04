package montanez.alexander.socioinfonavit.model.source

import montanez.alexander.socioinfonavit.model.entity.Benevit
import montanez.alexander.socioinfonavit.model.entity.BenevitResponse
import montanez.alexander.socioinfonavit.model.entity.Wallet
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface IUserData {
    @GET("member/wallets")
    suspend fun getWallets(@Header("Authorization") authorization : String) : Response<List<Wallet>>

    @GET("member/landing_benevits")
    suspend fun getBenevits(@Header("Authorization") authorization : String) : Response<BenevitResponse>
}