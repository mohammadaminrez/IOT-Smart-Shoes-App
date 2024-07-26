package com.mobiledevelopment.smartshoesapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("devices/668011d8f3fa49000bd01b74/variables/?token=BBUS-TOdaX8dAWkomX6XBkwHIjnd0b12qY7")
    fun getData(): Call<ResponseData>
}