package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.TopicRemoteDataSource
import retrofit2.Retrofit

class TopicRemoteDataSourceImpl(retrofit: Retrofit) :
    TopicRemoteDataSource by retrofit.create(TopicRemoteDataSource::class.java)