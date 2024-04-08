package com.my.application.financify.framework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.my.application.financify.AppContext
import com.my.application.financify.R
import com.my.application.financify.adapters.RecyclerViewMyAppAdapter
import com.my.application.financify.databinding.FragmentHomeBinding
import com.my.application.financify.interactors.straings_interactor.StringsInteractorImp
import com.my.application.financify.models.ModelImp
import com.my.application.financify.presenters.HomePresenter
import com.my.application.financify.views.HomeView

class HomeFragment : Fragment(), HomeView {
    private lateinit var presenter:HomePresenter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        presenter = HomePresenter(this, StringsInteractorImp(AppContext.context), ModelImp())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.startPresenter()

        binding.recyclerView.visibility = View.GONE
        binding.idLoaderGo.visibility = View.VISIBLE
        binding.idLoaderGo.startAnimation(AnimationUtils.loadAnimation(AppContext.context,
            R.anim.anim_new
        ))
    }

    override fun setOnClickForConverter() = with(binding) {
        linearClickTwo.setOnClickListener{
            navigateToSecondFragment(ConvertFragment())
        }
    }
    private fun navigateToSecondFragment(fragment:Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun setRecyclerView(arrayAllData:MutableList<Array<String>>) = with(binding){
        recyclerView.visibility = View.VISIBLE
        idLoaderGo.visibility = View.GONE
        idLoaderGo.clearAnimation()

        val adapterNewApp = RecyclerViewMyAppAdapter(arrayAllData)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapterNewApp
    }

    override fun showToast(text: String) {
        Toast.makeText(AppContext.context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.onDestroy()
        _binding = null
    }
}