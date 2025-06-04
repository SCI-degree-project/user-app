package com.userapp.viewmodel.ar

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface Product3DModelViewModelFactoryProvider {
    fun product3DModelViewModelFactory(): Product3DModelViewModel.Factory
}
