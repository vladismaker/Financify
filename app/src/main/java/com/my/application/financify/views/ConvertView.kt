package com.my.application.financify.views

interface ConvertView {
    fun showToast(text:String)
    fun setOnClickForHome()
    fun setOnClickForAll()
    fun setOnClickForButton()
    fun getEditTextContent(): String
    fun showRecyclerView(internet:Boolean)
    fun setRecyclerView(namesArrayList: ArrayList<String>)
    fun showAllTextViews(doubleVal:String, doubleVal2:String, doubleVal3:String, doubleVal4:String, doubleVal5:String, doubleVal6:String)
}