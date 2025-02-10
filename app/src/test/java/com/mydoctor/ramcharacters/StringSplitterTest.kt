package com.mydoctor.ramcharacters

import com.mydoctor.ramcharacters.utils.splitStringByLength
import org.junit.Assert.assertEquals
import org.junit.Test

class StringSplitterTest {

    private val length = 5

    @Test
    fun testLongString() {
        val input = "HelloWorld"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("Hello", first)
        assertEquals("World", second)
    }

    @Test
    fun testShortString() {
        val input = "Short"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("Short", first)
        assertEquals("", second)
    }

    @Test
    fun testExactlyFiveCharacters() {
        val input = "12345"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("12345", first)
        assertEquals("", second)
    }

    @Test
    fun testEmptyString() {
        val input = ""
        val (first, second) = input.splitStringByLength(length)
        assertEquals("", first)
        assertEquals("", second)
    }

    @Test
    fun testStringWithNumbers() {
        val input = "1234567890"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("12345", first)
        assertEquals("67890", second)
    }

    @Test
    fun testStringWithNumbersWithSpaces() {
        val input = "1234 5 67890"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("1234 ", first)
        assertEquals("5 67890", second)
    }

    @Test
    fun testStringWithSpecialCharacters() {
        val input = "!@#$%%^&*()"
        val (first, second) = input.splitStringByLength(length)
        assertEquals("!@#$%", first)
        assertEquals("%^&*()", second)
    }
}