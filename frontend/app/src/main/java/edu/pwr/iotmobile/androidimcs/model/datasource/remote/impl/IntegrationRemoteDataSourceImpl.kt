package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.IntegrationRemoteDataSource
import retrofit2.Retrofit

class IntegrationRemoteDataSourceImpl(retrofit: Retrofit) :
    IntegrationRemoteDataSource by retrofit.create(IntegrationRemoteDataSource::class.java)