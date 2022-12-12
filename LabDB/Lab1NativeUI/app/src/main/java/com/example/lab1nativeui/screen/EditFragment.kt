package com.example.lab1nativeui.screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab1nativeui.R
import com.example.lab1nativeui.data.ProductData
import com.example.lab1nativeui.databinding.FragmentEditBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.util.AppConstants
import com.example.lab1nativeui.viewmodel.EditViewModel


class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var editViewModel: EditViewModel
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editViewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        editViewModel.setupFields(arguments?.getParcelable(AppConstants.OBJECT)!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        binding.viewModel = editViewModel
        binding.lifecycleOwner = this

        adapter = ArrayAdapter<String>(requireContext(), R.layout.type_item_layout, ProductData.getTypeList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.typeSpinner.adapter = adapter

        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                editViewModel.setSelectedType(p0?.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.typeSpinner.setSelection(ProductData.getPosType(editViewModel.product.type))

        initFormListeners()

        binding.editButton.setOnClickListener {
            onEditClicked()
        }

        return binding.root
    }

    private fun initFormListeners() {
        binding.nameEditText.doAfterTextChanged {
            editViewModel.isProductNameValid()
        }

        binding.descrptionEditText.doAfterTextChanged {
            editViewModel.isDescriptionValid()
        }

        binding.quantityEditText.doAfterTextChanged {
            editViewModel.isQuantityValid()
        }

        binding.priceEditText.doAfterTextChanged {
            editViewModel.isPriceValid()
        }
    }

    private fun onEditClicked() {
        if(editViewModel.isAllCorrect()) {
            val intent = Intent(AppConstants.UPDATE_OBJECT)
            intent.putExtra(AppConstants.OBJECT, Product(id = editViewModel.product.id, name = editViewModel.productName.value!!,
                type = editViewModel.selectedType.value!!,
                description = editViewModel.description.value!!,
                quantity = editViewModel.quantity.value!!.toInt(),
                price = editViewModel.price.value!!.toDouble()
                )
            )
            requireContext().sendBroadcast(intent)
            requireActivity().supportFragmentManager.popBackStack()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}