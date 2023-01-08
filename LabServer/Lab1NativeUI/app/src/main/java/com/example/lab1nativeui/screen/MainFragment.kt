package com.example.lab1nativeui.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1nativeui.R
import com.example.lab1nativeui.api.ProductAPI
import com.example.lab1nativeui.data.ProductRepository
import com.example.lab1nativeui.database.AppDatabase
import com.example.lab1nativeui.database.ProductDao
import com.example.lab1nativeui.databinding.FragmentMainBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.model.ProductEntity
import com.example.lab1nativeui.productlist.ProductListAdapter
import com.example.lab1nativeui.util.APIUtils
import com.example.lab1nativeui.util.AppConstants
import com.example.lab1nativeui.viewmodel.MainViewModelWithDao
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModelWithDao
    private lateinit var binding: FragmentMainBinding
    private lateinit var dataReceiver: BroadcastReceiver
    private lateinit var adapter: ProductListAdapter
    private var productList = arrayListOf<Product>()
    private lateinit var dao: ProductDao
    private lateinit var repository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModelWithDao::class.java)
        dao = AppDatabase.getInstance(requireContext()).productDao()
        repository = ProductRepository(dao)
        mainViewModel.setDao(dao)
        mainViewModel.setRepository(repository)
        dataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    AppConstants.CREATE_OBJECT -> {
                        mainViewModel.insert(intent.getParcelableExtra(AppConstants.OBJECT)!!, APIUtils.checkNetworkConnectivity(requireContext()))
                    }
                    AppConstants.UPDATE_OBJECT -> {
                        mainViewModel.update(mainViewModel.convertToEntity(intent.getParcelableExtra(AppConstants.OBJECT)!!), APIUtils.checkNetworkConnectivity(requireContext()))
                    }
                    AppConstants.DELETE_OBJECT -> {
                        mainViewModel.delete(mainViewModel.convertToEntity(intent.getParcelableExtra(AppConstants.OBJECT)!!), APIUtils.checkNetworkConnectivity(requireContext()))
                    }
                }
            }
        }

        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch {
                    ProductAPI.retrofitService.getProducts()
                        .enqueue(object : Callback<List<ProductEntity>?> {
                            override fun onResponse(
                                call: Call<List<ProductEntity>?>,
                                response: Response<List<ProductEntity>?>
                            ) {

                                val serverProduct = response.body()!!
                                Log.d("Server data", serverProduct.toString())
                                val localProducts = repository.productList.value!!
                                Log.d("Local data", localProducts.toString())

                                for (s1: ProductEntity in localProducts) {
                                    var exists = false
                                    for (s2: ProductEntity in serverProduct) {
                                        if (s1.id == s2.id) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        ProductAPI.retrofitService.addProductWithId(s1)
                                            .enqueue(object : Callback<ProductEntity?> {
                                                override fun onResponse(
                                                    call: Call<ProductEntity?>,
                                                    response: Response<ProductEntity?>
                                                ) {
                                                    Log.d(
                                                        "Server",
                                                        "Added item from local db Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<ProductEntity?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to add item from local db!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Server",
                                                        "Added item from local db Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }
                                }

                                for (s2: ProductEntity in serverProduct) {
                                    var exists = false
                                    for (s1: ProductEntity in localProducts) {
                                        if (s1.id == s2.id) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        ProductAPI.retrofitService.deleteProduct(s2.id)
                                            .enqueue(object : Callback<ProductEntity?> {
                                                override fun onResponse(
                                                    call: Call<ProductEntity?>,
                                                    response: Response<ProductEntity?>
                                                ) {

                                                    Log.d(
                                                        "Server",
                                                        "Deleted item from server Success!"
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<ProductEntity?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to remove item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Server",
                                                        "Deleted item from server Failed! " + t.message
                                                    )
                                                }
                                            })
                                    }
                                }

                                for (s1: ProductEntity in localProducts) {
                                    var sync = false
                                    for (s2: ProductEntity in serverProduct) {
                                        if (s1.id == s2.id && s1.name == s2.name && s1.price == s2.price && s2.description == s1.description && s1.quantity == s2.quantity)
                                            sync = true
                                        }
                                    Log.d("YESS", serverProduct.toString())
                                    if (!sync && serverProduct.isNotEmpty()) {
                                        ProductAPI.retrofitService.updateProduct(s1.id, s1)
                                            .enqueue(object : Callback<ProductEntity?> {
                                                override fun onResponse(
                                                    call: Call<ProductEntity?>,
                                                    response: Response<ProductEntity?>
                                                ) {
                                                    Log.d(
                                                        "Server ",
                                                        "Updated item from the server Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<ProductEntity?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Server",
                                                        "Updated item from the server Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }
                            }

                            override fun onFailure(call: Call<List<ProductEntity>?>, t: Throwable) {
                                lifecycleScope.launch {
                                    Toast.makeText(
                                        context,
                                        "Failed to check differences between local db and server!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(
                                        "Server",
                                        "Check for differences between local db and server Failed: " + t.message
                                    )
                                }
                            }
                        })
                }
            }
        }
        )

        val dataIntent = mainViewModel.setupReceiverIntentFilter()
        requireActivity().registerReceiver(dataReceiver, dataIntent)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductListAdapter(productList, requireActivity().supportFragmentManager)
        binding.productList.adapter = adapter

        binding.addProductButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.fragment_container, AddFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        mainViewModel.productList.observe(viewLifecycleOwner) {
            when(mainViewModel.action.value) {
                AppConstants.ACTION_INIT -> {
                    productList.addAll(mainViewModel.convertListToProduct())
                    adapter.notifyDataSetChanged()
                }
                AppConstants.ACTION_CREATE -> {
                    productList.add(mainViewModel.convertToProduct(mainViewModel.productList.value!![mainViewModel.productList.value!!.size-1]))
                    adapter.notifyItemInserted(adapter.itemCount)
                }
                AppConstants.ACTION_UPDATE -> {
                    productList[mainViewModel.position] = mainViewModel.convertToProduct(mainViewModel.productList.value?.get(mainViewModel.position)!!)
                    adapter.notifyItemChanged(mainViewModel.position)
                }
                AppConstants.ACTION_DELETE -> {
                    if(mainViewModel.position != -1) {
                        productList.removeAt(mainViewModel.position)
                        adapter.notifyItemRemoved(mainViewModel.position)
                    }
                }
            }
        }

        mainViewModel.successful.observe(viewLifecycleOwner) {
            if(mainViewModel.successful.value != null) {
                if(mainViewModel.successful.value!! <= 0)
                    when(mainViewModel.action.value) {
                        AppConstants.ACTION_CREATE ->
                            Toast.makeText(requireContext(), "An object already exists with this name!", Toast.LENGTH_SHORT).show()
                        AppConstants.ACTION_UPDATE ->
                            Toast.makeText(requireContext(), "An object already exists with this name!", Toast.LENGTH_SHORT).show()
                        AppConstants.ACTION_DELETE ->
                            Toast.makeText(requireContext(), "There is no such element!", Toast.LENGTH_SHORT).show()
                    }
                if(mainViewModel.successful.value!! > 0)
                    when(mainViewModel.action.value) {
                        AppConstants.ACTION_CREATE ->
                            Toast.makeText(requireContext(), "Item added successfully!", Toast.LENGTH_SHORT).show()
                        AppConstants.ACTION_UPDATE ->
                            Toast.makeText(requireContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show()
                        AppConstants.ACTION_DELETE ->
                            Toast.makeText(requireContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        mainViewModel.successfulOnline.observe(viewLifecycleOwner) {
            if(mainViewModel.successfulOnline.value!! < 0)
                Toast.makeText(requireContext(), "Operation done only locally", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(dataReceiver)
    }
}