package com.ericktijerou.hackernews.presentation.ui.util

import androidx.test.espresso.idling.CountingIdlingResource

object CoroutinesIdlingResource {

    private const val idlingResourceName = "COROUTINES_IDLING_RESOURCE"

    val idlingResource = CountingIdlingResource(idlingResourceName)

}