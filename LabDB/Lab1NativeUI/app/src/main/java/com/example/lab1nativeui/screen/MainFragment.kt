package com.example.lab1nativeui.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1nativeui.R
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.data.ProductRepository
import com.example.lab1nativeui.database.AppDatabase
import com.example.lab1nativeui.database.ProductDao
import com.example.lab1nativeui.databinding.FragmentMainBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.model.ProductEntity
import com.example.lab1nativeui.productlist.ProductListAdapter
import com.example.lab1nativeui.util.AppConstants
import com.example.lab1nativeui.viewmodel.MainViewModel
import com.example.lab1nativeui.viewmodel.MainViewModelWithDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
                        mainViewModel.insert(intent.getParcelableExtra(AppConstants.OBJECT)!!)
                    }
                    AppConstants.UPDATE_OBJECT -> {
                        mainViewModel.update(mainViewModel.convertToEntity(intent.getParcelableExtra(AppConstants.OBJECT)!!))
                    }
                    AppConstants.DELETE_OBJECT -> {
                        mainViewModel.delete(mainViewModel.convertToEntity(intent.getParcelableExtra(AppConstants.OBJECT)!!))
                    }
                }
            }
        }

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(dataReceiver)
    }
}