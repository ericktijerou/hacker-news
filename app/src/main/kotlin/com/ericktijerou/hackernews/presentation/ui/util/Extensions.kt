package com.ericktijerou.hackernews.presentation.ui.util

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlin.reflect.KClass

fun <T> Activity.startNewActivity(
    target: KClass<T>,
    sharedViews: Array<Pair<View, String>>,
    extras: Intent.() -> Unit = {  }
) where T : Activity {
    val intent = Intent(this, target.java)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedViews).toBundle()
    intent.apply { extras() }
    this.startActivity(intent, options)
}

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, body: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer { body(it) })
}