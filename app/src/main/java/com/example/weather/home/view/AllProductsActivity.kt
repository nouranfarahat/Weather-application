package com.example.weather.home.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.mvvm.allproducts.viewmodel.AllProductsViewModel
import com.example.mvvm.allproducts.viewmodel.AllProductsViewModelFactory
import com.example.weather.model.Repository
import com.example.weather.network.ProductClient

class AllProductsActivity : AppCompatActivity() {

    lateinit var viewModel:AllProductsViewModel
    lateinit var productFactory:AllProductsViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)

        productFactory=AllProductsViewModelFactory(Repository.getInstance(ProductClient.getInstance()))
        viewModel=ViewModelProvider(this,productFactory).get(AllProductsViewModel::class.java)

        viewModel.productList.observe(this){
            products-> if(products!=null)
            Toast.makeText(this, "Product is Added", Toast.LENGTH_LONG).show()


        }
    }

}