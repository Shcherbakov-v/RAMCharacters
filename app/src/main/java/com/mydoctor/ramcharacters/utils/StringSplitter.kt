package com.mydoctor.ramcharacters.utils

fun String.splitStringByLength(length: Int): Pair<String, String> {
    val input = this
    val firstPart = input.substring(0, minOf(length, input.length))
    val secondPart = input.substring(minOf(length, input.length))
    return Pair(firstPart, secondPart)
}