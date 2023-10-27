package edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl

import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ProjectRemoteDataSource
import retrofit2.Retrofit

class ProjectRemoteDataSourceImpl(retrofit: Retrofit) :
    ProjectRemoteDataSource by retrofit.create(ProjectRemoteDataSource::class.java)