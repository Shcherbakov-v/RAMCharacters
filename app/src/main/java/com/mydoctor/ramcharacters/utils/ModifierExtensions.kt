package com.mydoctor.ramcharacters.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mydoctor.ramcharacters.BuildConfig

fun Modifier.testTagDebug(tag: String): Modifier {
    return if (BuildConfig.DEBUG) this.testTag(tag) else this
}