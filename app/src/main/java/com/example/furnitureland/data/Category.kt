package com.example.furnitureland.data

sealed class Category(val category: String) {

    object Chair: Category("Chair")
    object Cupboard: Category("Cupboard")
    object Table: Category("Table")
    object Sofa: Category("Sofa")
}