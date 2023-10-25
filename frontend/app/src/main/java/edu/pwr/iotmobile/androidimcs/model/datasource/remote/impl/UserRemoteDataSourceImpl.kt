package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.UserRemoteDataSource
import retrofit2.Retrofit

class UserRemoteDataSourceImpl(retrofit: Retrofit) :
    UserRemoteDataSource by retrofit.create(UserRemoteDataSource::class.java)