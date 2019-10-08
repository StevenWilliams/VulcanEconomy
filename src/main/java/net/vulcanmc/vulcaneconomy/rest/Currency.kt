package net.vulcanmc.vulcaneconomy.rest

import net.vulcanmc.vulcaneconomy.VulcanEconomy

import java.util.UUID

/**
 * Created by steven on 23/12/14.
 */
class Currency @JvmOverloads constructor(val id: UUID = defaultID, val name: String? = "NoName", val symbol :String? = "nosymbol", val symbloc :String? = "after") {
    val nameSingle: String
        get() = "Dollar"
    val namePlural: String
        get() = "Dollars"
    var key : String? = null


    fun format(value : Long) : String {
        return if(symbloc =="after") {
            "${value} ${symbol}";
        } else {
            "${symbol}${value}"
        }
    }
    companion object {
        val defaultID: UUID
            get() = UUID.fromString("f0aa6d41-1931-4c3a-8576-4d175779cb51")
    }
}
