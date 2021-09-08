package com.tashev.gbweatherfromya.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tashev.gbweatherfromya.R
import com.tashev.gbweatherfromya.viewModel.MainViewModel
import com.tashev.gbweatherfromya.databinding.SomeFragmentBinding

class SomeFragment : Fragment() {

    private var _binding : SomeFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SomeFragment = SomeFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return inflater.inflate(R.layout.some_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<Any>{renderData(it)}
        viewModel.getData().observe(viewLifecycleOwner, observer)
    }

    private fun renderData(data:Any){
        Toast.makeText(context, "SomeData", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}