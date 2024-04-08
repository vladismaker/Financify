package com.my.application.financify.presenters

import com.my.application.financify.interactors.straings_interactor.StringsInteractor
import com.my.application.financify.models.Model
import com.my.application.financify.views.HomeView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HomePresenter(private val view:HomeView, private val stringsInteractor: StringsInteractor, private val model: Model) {
    private var dataFromMapInternet: MutableMap<String, Array<String>> = mutableMapOf()
    private var arrayAllData:MutableList<Array<String>> = mutableListOf()
    private var dataBoolean:Boolean = false
    private lateinit var coroutine: Job
    private var initCoroutine:Boolean = false
    private val dataFromMap: Map<String, Array<String>> = mapOf(
        "usd" to arrayOf("USD", "241.59", "1.71811"),
        "eur" to arrayOf("EUR", "1.100", "0.67"),
        "gbp" to arrayOf("GBP", "1.284", "0.68"),
        "huf" to arrayOf("HUF", "0.003", "1.39"),
        "jpy" to arrayOf("JPY", "0.007", "2.12"),
        "try" to arrayOf("TRY", "0.037", "0.95"),
        "chf" to arrayOf("CHF", "1.154", "0.75"),
        "cny" to arrayOf("CNY", "0.139", "0.80"),
        "pln" to arrayOf("PLN", "0.249", "0.77"),
        "aud" to arrayOf("AUD", "0.673", "0.82"),
        "nzd" to arrayOf("NZD", "0.620", "0.93"),
        "cad" to arrayOf("CAD", "0.757", "1.08"),
        "aed" to arrayOf("AED", "0.272", "1.00")
    )

    fun startPresenter(){
        view.setOnClickForConverter()

        if (dataBoolean){
            mapToArray(true)
            view.setRecyclerView(arrayAllData)
        }else{
            coroutine = CoroutineScope(Dispatchers.IO).launch {
                try {
                    initCoroutine=true
                    val internet = model.checkInternet()

                    if(!internet){
                        withContext(Dispatchers.Main){
                            view.showToast(stringsInteractor.textInternet)
                        }

                        mapToArray(false)
                        withContext(Dispatchers.Main){
                            view.setRecyclerView(arrayAllData)
                        }

                    }else{
                        val apiUrl = "https://api.coingecko.com/api/v3/simple/price"
                        val listPairs = "binancecoin"
                        val cur = "USD,EUR,GBP,HUF,JPY,TRY,CHF,CNY,PLN,AUD,NZD,CAD,AED"

                        val data  = model.startRequest("$apiUrl?ids=$listPairs&vs_currencies=$cur&include_24hr_change=true")
                        dataFromNewAppJson(data.toString())
                        mapToArray(true)
                        withContext(Dispatchers.Main){
                            view.setRecyclerView(arrayAllData)
                        }
                    }
                }catch (e: Throwable) {
                    withContext(Dispatchers.Main){
                        mapToArray(false)
                        view.setRecyclerView(arrayAllData)
                    }
                }
            }
        }
    }

    private fun dataFromNewAppJson(response: String){
        dataFromMapInternet.clear()

        val jObj = JSONObject(response)

        val cObject = jObj.getJSONObject("binancecoin")

        val currencies = listOf("eur", "gbp", "huf", "jpy", "try", "chf", "cny", "pln", "aud", "nzd", "cad", "aed")

        for (currency in currencies) {
            val priceUsd = cObject.getDouble("usd")
            val price = cObject.getDouble(currency)
            val changeUsd = cObject.getDouble("usd_24h_change")
            val change = cObject.getDouble("${currency}_24h_change")

            val newArray = arrayOf(currency.uppercase(), (priceUsd/price).toString(), (changeUsd/change).toString())
            dataFromMapInternet[currency] = newArray
        }

        dataBoolean=true
    }

    private fun mapToArray(map:Boolean){
        arrayAllData.clear()
        if (!map){
            dataFromMap.forEach { (cur, array) ->
                if (cur != "usd"){
                    arrayAllData.add(array)
                }
            }
        }else{
            dataFromMapInternet.forEach { (_, array) ->
                arrayAllData.add(array)
            }
        }
        arrayAllData.sortByDescending { it[2].toDouble() }
    }

    fun onDestroy(){
        if(initCoroutine){
            coroutine.cancel()
        }
    }
}