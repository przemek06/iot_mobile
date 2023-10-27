package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import retrofit2.Retrofit

class DashboardRemoteDataSourceImpl(retrofit: Retrofit) :
    DashboardRemoteDataSource by retrofit.create(DashboardRemoteDataSource::class.java)