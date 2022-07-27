package bd.smartpost.tracker.data.source.remote

import bd.smartpost.tracker.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UpuServices {
    @FormUrlEncoded
    @POST("Search.aspx")
    suspend fun getUpu(
        @Field("txtItemID") tracking_id: String,
        @Field("__VIEWSTATE") viewState:String = Constants.__VIEWSTATE,
        @Field("__EVENTVALIDATION") eventValidation:String = Constants.__EVENTVALIDATION,
        @Field("__VIEWSTATEGENERATOR") viewStateGenerator:String = Constants.__VIEWSTATEGENERATOR,
        @Field("btnSearch") btnSearch:String = Constants.btnSearch
    ):Response<String>

}