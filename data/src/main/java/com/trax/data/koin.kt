package com.trax.data

import androidx.room.Room
import com.trax.data.local.MoviesDatabase
import com.trax.data.local.MoviesLocalDataSource
import com.trax.data.local.MoviesLocalDataSourceImpl
import com.trax.data.remote.MoviesRemoteDataSource
import com.trax.data.remote.MoviesRemoteDataSourceImpl
import com.trax.data.remote.testEpgCertificate
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<MoviesLocalDataSource> {
        MoviesLocalDataSourceImpl(get())
    }

    single {
        Room.databaseBuilder(get(), MoviesDatabase::class.java, "movies").build()
    }

    single<OkHttpClient> {
        val clientCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(testEpgCertificate)
            .addPlatformTrustedCertificates()
            .build()

        OkHttpClient.Builder()
            .sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://testepg.r0ro.fr/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<MoviesRemoteDataSource> {
        MoviesRemoteDataSourceImpl(get())
    }

    factory<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }
}