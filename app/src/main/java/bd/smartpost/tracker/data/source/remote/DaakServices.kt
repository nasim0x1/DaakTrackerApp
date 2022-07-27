package bd.smartpost.tracker.data.source.remote

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DaakServices {
    @FormUrlEncoded
    @POST("search1.php")
    suspend fun getInternational(@Field("item_id") tracking_id:String): Response<String>

    @FormUrlEncoded
    @POST("search2.php")
    suspend fun getDomestic(@Field("item_id") tracking_id:String): Response<String>
}