package com.my.application.financify.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.my.application.financify.R
import com.my.application.financify.adapters.RecyclerViewAdapterTwo
import com.my.application.financify.databinding.FragmentConvertBinding
import com.my.application.financify.interactors.straings_interactor.StringsInteractorImp
import com.my.application.financify.models.ModelImp
import com.my.application.financify.presenters.ConvertPresenter
import com.my.application.financify.views.ConvertView

class ConvertFragment : Fragment(), ConvertView {
    private lateinit var presenter:ConvertPresenter
    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertBinding.inflate(inflater, container, false)
        presenter = ConvertPresenter(this, StringsInteractorImp(requireActivity()), ModelImp())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.startConvertPresenter()
    }

    override fun showToast(text: String)  {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    override fun getEditTextContent(): String = with(binding) {
        return idEditText.text.toString()
    }

    override fun setOnClickForHome() = with(binding){
        linearClickHome.setOnClickListener{
            navigateToSecondFragment(HomeFragment())
        }
    }

    override fun showAllTextViews(doubleVal:String, doubleVal2:String, doubleVal3:String, doubleVal4:String, doubleVal5:String, doubleVal6:String) = with(binding) {
        idTv1.text = doubleVal
        idTv2.text = doubleVal2
        idTv3.text = doubleVal3
        idTv4.text = doubleVal4
        idTv5.text = doubleVal5
        idTv6.text = doubleVal6
    }

    override fun showRecyclerView(internet:Boolean) = with(binding) {
        if (internet){
            recyclerView2.visibility = View.VISIBLE
        }else{
            recyclerView2.visibility = View.GONE
        }
    }

    override fun setOnClickForAll() = with(binding) {
        idAll.setOnClickListener {
            presenter.showAll()
        }
    }

    override fun setOnClickForButton() = with(binding) {
        clickCalculate.setOnClickListener {
            presenter.calculate()
        }
    }

    private fun navigateToSecondFragment(fragment:Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun setRecyclerView(namesArrayList: ArrayList<String>) = with(binding){
        val adapter = RecyclerViewAdapterTwo(namesArrayList)
        recyclerView2.isNestedScrollingEnabled = false
        recyclerView2.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView2.adapter = adapter

        adapter.setListener(object: RecyclerViewAdapterTwo.Listener{
            override fun onClick(position: Int) {
                presenter.setNumber(position)
                recyclerView2.visibility = View.GONE
                idName1.text = namesArrayList[position]
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        _binding = null
    }
}