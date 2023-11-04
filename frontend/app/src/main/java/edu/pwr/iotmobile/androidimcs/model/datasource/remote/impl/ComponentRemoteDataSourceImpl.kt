package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ComponentRemoteDataSource
import retrofit2.Retrofit

class ComponentRemoteDataSourceImpl(retrofit: Retrofit) :
    ComponentRemoteDataSource by retrofit.create(ComponentRemoteDataSource::class.java)