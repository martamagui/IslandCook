package com.marta.islandcook.usecases.personal

import android.content.ClipData

class Category(val name: String, vararg item: ClipData.Item) {

    val listOfItems: List<ClipData.Item> = item.toList()

}