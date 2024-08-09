package com.example.model.utils

fun <T> List<T>.add(element: T) {
    this.toMutableList().apply {
        add(element)
    }
}

fun <T> List<T>.add(index: Int, element: T) {
    this.toMutableList().apply {
        add(index, element)
    }
}

fun <T> List<T>.remove(element: T) {
    this.toMutableList().apply {
        remove(element)
    }
}

fun <T> List<T>.removeAt(index: Int) {
    this.toMutableList().apply {
        removeAt(index)
    }
}