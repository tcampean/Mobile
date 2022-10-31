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
import androidx.lifecycle.ViewModelProvider
import com.example.lab1nativeui.R
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.databinding.FragmentDetailsBinding
import com.example.lab1nativeui.util.AppConstants
import com.example.lab1nativeui.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var dataReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        detailsViewModel.setupFields(arguments?.getParcelable(AppConstants.OBJECT)!!)
        dataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.action.equals(AppConstants.UPDATE_OBJECT)) {
                    detailsViewModel.setupFields(intent?.getParcelableExtra(AppConstants.OBJECT)!!)
                    binding.productImage.setImageResource(ProductData.getAppropriatePicture(detailsViewModel.product.type))
                }
            }
        }

        val dataIntent = detailsViewModel.setupReceiverIntentFilter()
        requireActivity().registerReceiver(dataReceiver, dataIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.viewModel = detailsViewModel
        binding.lifecycleOwner = this

        binding.editButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.OBJECT, detailsViewModel.product)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, EditFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()
        }

        binding.delete.setOnClickListener {
            val intent = Intent(AppConstants.DELETE_OBJECT)
            intent.putExtra(AppConstants.OBJECT, detailsViewModel.product.id.toString())
            requireContext().sendBroadcast(intent)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.productImage.setImageResource(ProductData.getAppropriatePicture(detailsViewModel.product.type))

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(dataReceiver)
    }

}