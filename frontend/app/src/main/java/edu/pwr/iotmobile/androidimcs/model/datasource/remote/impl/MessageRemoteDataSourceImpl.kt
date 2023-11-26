package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.MessageRemoteDataSource
import retrofit2.Retrofit

class MessageRemoteDataSourceImpl(retrofit: Retrofit) :
    MessageRemoteDataSource by retrofit.create(MessageRemoteDataSource::class.java)