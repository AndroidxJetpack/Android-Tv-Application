package com.ncgtelevision.net.retrofit_clients

import com.ncgtelevision.net.account.model.AccountModel
import com.ncgtelevision.net.account.model.Datum
import com.ncgtelevision.net.account.model.DeleteChannelRequest
import com.ncgtelevision.net.account.model.DeleteChannelResponse
import com.ncgtelevision.net.channel.model.ChannelModel
import com.ncgtelevision.net.forgot_password.ForgotPasswordModel
import com.ncgtelevision.net.forgot_password.ForgotPasswordRQBody
import com.ncgtelevision.net.home_screen.model.HomePageModel
import com.ncgtelevision.net.home_screen.model.HomePageRequest
import com.ncgtelevision.net.playback.model.PlaybackModel
import com.ncgtelevision.net.playback.model.PlaybackRequest
import com.ncgtelevision.net.search_screen.SearchRequestBody
import com.ncgtelevision.net.search_screen.search_model.SearchModel
import com.ncgtelevision.net.signin.SendSignInBody
import com.ncgtelevision.net.signin.SignInModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("Accept: application/json")
    @POST("users/login")
    fun postSignInCall(@Body  signInJSONBody: SendSignInBody?): Call<SignInModel?>?

    @Headers("Accept: application/json")
    @POST("forgot_password")
    fun postForgotPasswordCall(@Body  forgotPasswordRQBody: ForgotPasswordRQBody?): Call<ForgotPasswordModel?>?

    @Headers("Accept: application/json")
    @POST("home_page")
    fun getHomePage(@Body homePageRequest: HomePageRequest?): Call<HomePageModel?>?

    @Headers("Accept: application/json")
    @GET("user/account_info")
    fun getAccount(): Call<AccountModel?>?

    @Headers("Accept: application/json")
    @POST("user/update_account_info")
    fun updateAccount(@Body data:Datum?): Call<AccountModel?>?

    @Headers("Accept: application/json")
    @GET("user/membership_details")
    fun getMembership(): Call<AccountModel?>?

    @Headers("Accept: application/json")
    @POST("get_category_by_channel")
    fun getChannelPage(@Body channelModel: ChannelModel?): Call<HomePageModel?>?

    @Headers("Accept: application/json")
    @HTTP(method = "DELETE", path = "user/delete_membership", hasBody = true)
    fun deleteChannel(@Body deleteChannelRequest: DeleteChannelRequest?): Call<DeleteChannelResponse?>?

    @Headers("Accept: application/json")
    @POST("search_videos")
    fun postSearchVideos(@Body seachRequest: SearchRequestBody? ) : Call<SearchModel>?

    @Headers("Accept: application/json")
    @POST("video_play")
    fun getVideo(@Body plabackRequest: PlaybackRequest? ) : Call<PlaybackModel>?

    @Headers("Accept: application/json")
    @POST("my_list/add")
    fun addVideoInList(@Body plabackRequest: PlaybackRequest? ) : Call<PlaybackModel>?

    @Headers("Accept: application/json")
    @POST("my_list/remove")
    fun removeVideoInList(@Body plabackRequest: PlaybackRequest? ) : Call<PlaybackModel>?

}