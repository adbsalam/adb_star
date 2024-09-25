package com.adbsalam.star.api.data.popular

import com.adbsalam.star.ui.uiutil.recycleritems.MovieGenre


enum class MovieGenres(val id: Int) {
    Action(28),
    Adventure(12),
    Animation(16),
    Comedy(35),
    Crime(80),
    Documentary(99),
    Drama(18),
    Family(10751),
    Fantasy(14),
    History(36),
    Horror(27),
    Music(10402),
    Mystery(9648),
    Romance(10749),
    ScienceFiction(878),
    TVMovie(10770),
    Thriller(53),
    War(10752),
    Western(37),
}

data class MovieByGenre(
    var movieList: ArrayList<PopularMoviesResponse.PopularMoviesList> = arrayListOf(),
    var genreTitle: String = ""
)