package com.userapp.viewmodel

import com.userapp.viewmodel.carousel.ProductsByMaterialCarouselViewModel
import com.userapp.viewmodel.carousel.ProductsByStyleCarouselViewModel
import com.userapp.viewmodel.product.ProductDetailsViewModel
import com.userapp.viewmodel.profile.StoreProfileViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProductDetailsViewModelFactoryProvider {
    fun productDetailsViewModelFactory(): ProductDetailsViewModel.Factory
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StoreViewModelFactoryProvider {
    fun storeViewModelFactory(): StoreProfileViewModel.Factory
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MaterialCarouselViewModelFactoryProvider {
    fun materialCarouselViewModelFactoryProvider(): ProductsByMaterialCarouselViewModel.Factory
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StyleCarouselViewModelFactoryProvider {
    fun styleCarouselViewModelFactoryProvider(): ProductsByStyleCarouselViewModel.Factory
}