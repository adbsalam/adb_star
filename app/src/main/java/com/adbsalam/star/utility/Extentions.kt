package com.adbsalam.star.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import com.adbsalam.star.api.data.popular.MovieByGenre
import com.adbsalam.star.api.data.popular.MovieGenres
import com.adbsalam.star.api.data.popular.PopularMoviesResponse

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

fun Activity.launchActivity(
    packageName: String,
    className: String,
    flags: Int = -1,
    bundle: Bundle? = null
) {
    val intent = Intent(Intent.ACTION_VIEW).setClassName(packageName, className)
    if (flags != -1) {
        intent.flags = flags
    }
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

fun CreateComposeList(list: List<@Composable()() -> Unit>): List<@Composable()() -> Unit> {
    return list
}

inline fun <reified T : Any> Any.cast(): T {
    return this as T
}

fun List<PopularMoviesResponse.PopularMoviesList>.filterByGenre(): ArrayList<MovieByGenre> {
    val listByGenre = ArrayList<MovieByGenre>()

    MovieGenres.values().forEach { genre ->
        val currentGenre = MovieByGenre()
        currentGenre.genreTitle = genre.name
        this.forEach { movie ->
            if (movie.genre_ids[0] == genre.id) {
                currentGenre.movieList.add(movie)
            }
        }
        listByGenre.add(currentGenre)
    }

    return listByGenre
}

@Composable
fun UIComponent(comp : @Composable () -> Unit){
    comp()
}

@Retention(AnnotationRetention.SOURCE)
@Target(
    // function declarations
    // @Composable fun Foo() { ... }
    // lambda expressions
    // val foo = @Composable { ... }
    AnnotationTarget.FUNCTION,

    // type declarations
    // var foo: @Composable () -> Unit = { ... }
    // parameter types
    // foo: @Composable () -> Unit
    AnnotationTarget.TYPE,

    // composable types inside of type signatures
    // foo: (@Composable () -> Unit) -> Unit
    AnnotationTarget.TYPE_PARAMETER,

    AnnotationTarget.EXPRESSION
)
annotation class MainUiForComposable()

