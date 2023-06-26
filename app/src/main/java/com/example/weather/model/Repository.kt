package com.example.weather.model

import com.example.weather.network.RemoteSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class Repository(
    var remoteSource: RemoteSource,
):RepositoryInterface{

    companion object
    {
        private var instance: Repository?=null
        fun getInstance(
            remoteSource: RemoteSource,
        ): Repository {
            return instance?: synchronized(this){
                val temp= Repository(remoteSource)
                instance=temp
                temp
            }
        }

    }


    override suspend fun getProductsList(): Flow<Gson> { //leh m4 rady 8er b ? ?
        return remoteSource.getAllProductFromNetwork()
    }


}