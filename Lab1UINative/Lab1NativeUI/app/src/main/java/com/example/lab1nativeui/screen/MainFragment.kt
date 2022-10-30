package com.example.lab1nativeui.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1nativeui.R
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.databinding.FragmentMainBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.productlist.ProductListAdapter
import com.example.lab1nativeui.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var dataReceiver: BroadcastReceiver
    private lateinit var adapter: ProductListAdapter
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        dataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //add element to list
                System.out.println(intent?.getParcelableExtra("object"))
                mainViewModel.addProduct(intent?.getParcelableExtra("object")!!)
            }
        }

        val dataIntent = mainViewModel.setupReceiverIntentFilter()
        requireActivity().registerReceiver(dataReceiver, dataIntent)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        productList = ProductData().loadProducts()
        binding.productList.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductListAdapter(productList)
        binding.productList.adapter = adapter

        binding.addProductButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, AddFragment())
                .addToBackStack(null)
                .commit()
        }

        mainViewModel.productList.observe(viewLifecycleOwner) {
            //fix observer strat
            productList.add(mainViewModel.productList.value!![productList.size-1])
            adapter.notifyItemInserted(adapter.itemCount - 1)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(dataReceiver)
    }


}