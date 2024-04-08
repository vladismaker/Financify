package com.my.application.financify.views

interface HomeView {
    fun setOnClickForConverter()
    fun showToast(text:String)
    fun setRecyclerView(arrayAllData:MutableList<Array<String>>)
}