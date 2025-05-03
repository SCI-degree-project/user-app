package com.userapp.viewmodel

import com.userapp.viewmodel.ProductDetailsViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProductDetailsViewModelFactoryProvider {
    fun productDetailsViewModelFactory(): ProductDetailsViewModel.Factory
}
