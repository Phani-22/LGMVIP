package com.phani.covidapp


data class Model(
    val name: String,
    val activeCases: Int,
    val confirmedCases: Int,
    val deceasedCases: Int,
    val recoveredCases: Int,
    val curStateDetailsJSON: String
)
