package com.vallem.componentlibrary.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.booleanResource
import com.vallem.componentlibrary.R

@Composable
fun isSmallScreen() = booleanResource(id = R.bool.is_small_screen)
