package com.lappenfashion.`interface`

import com.lappenfashion.data.model.ResponseMainHome

interface CategoriesInterface {
    fun goToSubCategories(get: ResponseMainHome.Payload.Category?)
    fun dealsOfTheDay(get: ResponseMainHome.Payload.DealsOfTheDay?)
}