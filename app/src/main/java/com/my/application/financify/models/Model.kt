package com.my.application.financify.models

interface Model {
    suspend fun startRequest(url:String):String?
    fun checkInternet():Boolean
}