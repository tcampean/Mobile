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
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab1nativeui.R
import com.example.lab1nativeui.databinding.FragmentAddBinding
import com.example.lab1nativeui.model.Product
import com.example.lab1nativeui.viewmodel.AddViewModel
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorText")
fun errorText(view: TextInputLayout, error: String) {
    view.error = error
}

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var addViewModel: AddViewModel
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addViewModel = ViewModelProvider(this).get(AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.viewModel = addViewModel
        binding.lifecycleOwner = this

        adapter = ArrayAdapter<String>(requireContext(), R.layout.type_item_layout, listOf("Bakery", "Cake", "Cupcake", "Cookie", "Donut"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.typeSpinner.adapter = adapter

        binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                addViewModel.setSelectedType(p0?.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        initFormListeners()

        binding.createButton.setOnClickListener {
            onCreateClicked()
        }

        return binding.root
    }

    private fun initFormListeners() {
        binding.nameEditText.doAfterTextChanged {
            addViewModel.isProductNameValid()
        }

        binding.descrptionEditText.doAfterTextChanged {
            addViewModel.isDescriptionValid()
        }

        binding.quantityEditText.doAfterTextChanged {
            addViewModel.isQuantityValid()
        }

        binding.priceEditText.doAfterTextChanged {
            addViewModel.isPriceValid()
        }
    }

    private fun onCreateClicked() {
        if(addViewModel.isAllCorrect()) {
            val intent = Intent("CreateObject")
            intent.putExtra("object", Product(name = addViewModel.productName.value!!,
                type = addViewModel.selectedType.value!!,
                description = addViewModel.description.value!!,
                quantity = addViewModel.quantity.value!!.toInt(),
                price = addViewModel.price.value!!.toDouble()
            ))
            requireContext().sendBroadcast(intent)
        }
    }

}