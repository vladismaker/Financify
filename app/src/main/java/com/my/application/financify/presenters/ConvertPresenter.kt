package com.my.application.financify.presenters

import com.my.application.financify.interactors.straings_interactor.StringsInteractor
import com.my.application.financify.models.Model
import com.my.application.financify.views.ConvertView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ConvertPresenter(private val view: ConvertView, private val stringsInteractor: StringsInteractor, private val model: Model) {
    private var mapPrices: MutableMap<Int, Map<Int, String>> = mutableMapOf()
    private var namesArrayList: ArrayList<String> = ArrayList()
    private var number:Int = 0
    private var update: Boolean = false
    private var stringSum: String = "0"
    private lateinit var coroutine: Job
    private var initCoroutine:Boolean = false
    private val mapTitles: Map<String, String> = mapOf(
        "bitcoin" to "BTC",
        "ethereum" to "ETH",
        "ripple" to "XRP",
        "cardano" to "ADA",
        "litecoin" to "LTC",
        "polkadot" to "DOT",
        "chainlink" to "LINK",
        "stellar" to "XLM",
        "vechain" to "VET",
        "binancecoin" to "BNB",
        "cosmos" to "ATOM"
    )

    fun startConvertPresenter(){
        view.setOnClickForHome()
        view.setOnClickForAll()
        view.setOnClickForButton()

        workForRequest(false)
    }

    private fun workForRequest(calculate:Boolean){
        coroutine = CoroutineScope(Dispatchers.IO).launch {
            try {
                initCoroutine = true
                val internet = model.checkInternet()

                if(!internet) {
                    withContext(Dispatchers.Main) {
                        view.showToast(stringsInteractor.textInternet)
                    }
                }else{

                    val apiUrl = "https://api.coingecko.com/api/v3/simple/price"
                    val listCoins = "bitcoin,ethereum,ripple,cardano,litecoin,polkadot,chainlink,stellar,vechain,binancecoin,cosmos"
                    val currency = "usd,eur,gbp,try,chf,jpy"

                    val data  = model.startRequest("$apiUrl?ids=$listCoins&vs_currencies=$currency").toString()

                    val jsonObjNew = JSONObject(data)

                    val keysMap = jsonObjNew.keys()
                    var count = 0
                    while (keysMap.hasNext()) {
                        val key = keysMap.next() as String
                        val valueObjNew = jsonObjNew.getJSONObject(key)

                        val usd = valueObjNew.getString("usd")
                        val eur = valueObjNew.getString("eur")
                        val gbp = valueObjNew.getString("gbp")
                        val cTry = valueObjNew.getString("try")
                        val chf = valueObjNew.getString("chf")
                        val jpy = valueObjNew.getString("jpy")

                        mapPrices[count] = createInnerMap(usd, eur, gbp, cTry, chf, jpy, mapTitles[key].toString())
                        namesArrayList.add(mapTitles[key].toString())
                        count++

                        withContext(Dispatchers.Main){
                            view.setRecyclerView(namesArrayList)
                        }
                    }
                    update=true

                    if (calculate){
                        withContext(Dispatchers.Main){
                            calc()
                        }
                    }
                }
            }catch (e:Throwable){
                withContext(Dispatchers.Main){
                    view.showToast(stringsInteractor.textInternet)
                }
            }
        }
    }

    private fun calc(){
        val text = view.getEditTextContent()

        if (text.isNotEmpty()) {
            stringSum = text
            calculateFormulas(stringSum)
        } else {
            view.showToast(stringsInteractor.textError)
        }
    }

    private fun calculateFormulas(strCount:String){
        val doubleVal: Double = strCount.toDouble() * mapPrices[number]?.get(0)?.toDouble()!!
        val doubleVal2: Double = strCount.toDouble() * mapPrices[number]?.get(1)?.toDouble()!!
        val doubleVal3: Double = strCount.toDouble() * mapPrices[number]?.get(2)?.toDouble()!!
        val doubleVal4: Double = strCount.toDouble() * mapPrices[number]?.get(3)?.toDouble()!!
        val doubleVal5: Double = strCount.toDouble() * mapPrices[number]?.get(4)?.toDouble()!!
        val doubleVal6: Double = strCount.toDouble() * mapPrices[number]?.get(5)?.toDouble()!!

        view.showAllTextViews("%.3f".format(doubleVal), "%.3f".format(doubleVal2), "%.3f".format(doubleVal3), "%.3f".format(doubleVal4), "%.3f".format(doubleVal5), "%.3f".format(doubleVal6))
    }

    fun calculate(){
        if (update){
            calc()
        }else{
            workForRequest(true)
        }
    }

    fun showAll(){
        coroutine = CoroutineScope(Dispatchers.IO).launch {
            try {
                initCoroutine = true
                val internet = model.checkInternet()
                withContext(Dispatchers.Main){
                    if (internet) {
                        view.showRecyclerView(true)
                    }else{
                        view.showRecyclerView(false)
                    }
                }
            }catch (e:Throwable){

                withContext(Dispatchers.Main){
                    view.showToast(stringsInteractor.textInternet)
                }
            }
        }
    }

    fun setNumber(position:Int){
        number = position
    }

    private fun createInnerMap(usdString: String, eurString: String, gbpString: String, tryyString: String, chfString: String, jpyString: String, nameString: String): MutableMap<Int, String> {
        return mutableMapOf(
            0 to usdString,
            1 to eurString,
            2 to gbpString,
            3 to tryyString,
            4 to chfString,
            5 to jpyString,
            6 to nameString
        )
    }

    fun onDestroy(){
        if(initCoroutine){
            coroutine.cancel()
        }
    }
}