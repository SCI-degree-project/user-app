package com.user_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CatalogFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private var productList = listOf<Product>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        fetchProducts()
        return view
    }

    private fun fetchProducts() {
        productList = listOf(
            Product("1", "Silla Moderna", "Cómoda y elegante", "https://via.placeholder.com/150", 49.99),
            Product("2", "Mesa de Centro", "Estilo escandinavo", "https://via.placeholder.com/150", 89.99),
            Product("3", "Estante Minimalista", "Perfecto para libros", "https://via.placeholder.com/150", 69.99)
        )

        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter
    }
}
