package com.example.gagalmuluyaallah


// Sealed class is used to represent restricted class hierarchies,
// when a value can have one of the types from a limited set, but cannot have any other type.
sealed class ResultSealed <out R> private constructor(){


    data class Success<out T>(val data: T) : ResultSealed<T>()
    data class Error(val exception: String) : ResultSealed<Nothing>()
    //add loading process
    object Loading : ResultSealed<Nothing>()
}