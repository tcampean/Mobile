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
import com.example.lab1nativeui.databinding.FragmentMainBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.productlist.ProductListAdapter
import com.example.lab1nativeui.util.AppConstants
import com.example.lab1nativeui.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var dataReceiver: BroadcastReceiver
    private lateinit var adapter: ProductListAdapter
    private var productList = arrayListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        dataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action){
                    AppConstants.CREATE_OBJECT -> {
                        if(!mainViewModel.addProduct(intent.getParcelableExtra(AppConstants.OBJECT)!!))
                            Toast.makeText(requireContext(), "An object already exists with this name!", Toast.LENGTH_LONG).show()
                        else  Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_LONG).show()
                    }
                    AppConstants.UPDATE_OBJECT -> {
                        if(!mainViewModel.updateProduct(intent.getParcelableExtra(AppConstants.OBJECT)!!))
                            Toast.makeText(requireContext(), "An object already exists with this name!", Toast.LENGTH_LONG).show()
                        else Toast.makeText(requireContext(), "Item updated successfully!", Toast.LENGTH_LONG).show()
                    }
                    AppConstants.DELETE_OBJECT -> {
                        if(!mainViewModel.deleteProduct(intent.getStringExtra((AppConstants.OBJECT))!!))
                            Toast.makeText(requireContext(), "Item not found!", Toast.LENGTH_LONG).show()
                        else Toast.makeText(requireContext(), "Item deleted successfully!", Toast.LENGTH_LONG).show()
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
                    productList.addAll(mainViewModel.productList.value!!)
                    adapter.notifyDataSetChanged()
                }
                AppConstants.ACTION_CREATE -> {
                    productList.add(mainViewModel.productList.value!![mainViewModel.productList.value!!.size-1])
                    adapter.notifyItemInserted(adapter.itemCount)
                }

                AppConstants.ACTION_UPDATE -> {
                    productList[mainViewModel.position] = mainViewModel.productList.value?.get(mainViewModel.position)!!
                    adapter.notifyItemChanged(mainViewModel.position)
                }
                AppConstants.ACTION_DELETE -> {
                    productList.removeAt(mainViewModel.position)
                    adapter.notifyItemRemoved(mainViewModel.position)
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