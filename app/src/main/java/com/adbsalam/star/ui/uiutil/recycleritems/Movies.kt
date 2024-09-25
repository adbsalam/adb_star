package com.adbsalam.star.ui.uiutil.recycleritems

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.adbsalam.star.BuildConfig
import com.adbsalam.star.R
import com.adbsalam.star.api.data.popular.MovieGenres
import com.adbsalam.star.api.data.popular.PopularMoviesResponse
import com.adbsalam.star.ui.uiutil.AppButton
import com.adbsalam.star.ui.uiutil.ScrollState
import com.adbsalam.star.ui.uiutil.TabLayout
import com.adbsalam.star.ui.uiutil.uidatamodels.ButtonModel
import com.adbsalam.star.ui.uiutil.uidatamodels.PagerModel
import com.adbsalam.star.utility.filterByGenre
import com.google.accompanist.pager.*
import com.google.android.material.animation.AnimationUtils.lerp
import kotlin.math.absoluteValue


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppCompactPager(pagerState: PagerState, imagesList: List<PopularMoviesResponse.PopularMoviesList>){
    Box(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 20.dp)
            .fillMaxWidth()
            .height(700.dp)
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            count = imagesList.size,

        ) { position ->

            Column(modifier = Modifier.fillMaxSize()) {
                Card(
                    shape = RoundedCornerShape(0),
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(position).absoluteValue

                            lerp(
                                0.60f, 1f, 1f - pageOffset.coerceIn(0f, 1f)).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            alpha = lerp(0.2f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        },
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 15.dp)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter("${BuildConfig.API_IMAGE_BASE_URL}${imagesList[position].poster_path}"),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }

            }
        }
    }
}

@Composable
fun MovieGenre(genre: String){
    Text(modifier = Modifier
        .padding(horizontal = 10.dp),
        text = genre,
        color = Color.White,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.W500
        )
    )
}


@Composable
fun MovieDescription(modifier: Modifier, movie: PopularMoviesResponse.PopularMoviesList, animatedVisibility: Boolean){
    Column(
        modifier = modifier
    ) {
        AnimatedVisibility(visible = animatedVisibility) {
            Column() {
                var genreText = ""
                movie.genre_ids.forEach{ genre ->
                    val isExist = MovieGenres.values().firstOrNull{
                        it.id == genre
                    }?.name
                    genreText = "$genreText $isExist - "
                }
                if(genreText.isNotEmpty()){
                    MovieImageTailingView(genreText.substring(0, genreText.length -2))
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoadMoviesListView(moviesList: List<PopularMoviesResponse.PopularMoviesList>) {
    var pagerImages = listOf<PopularMoviesResponse.PopularMoviesList>()
    val listByGenre = moviesList.filterByGenre()
    val pagerState = rememberPagerState(1)

    if (moviesList.size > 5) {
        pagerImages = listOf(moviesList[0], moviesList[1], moviesList[2])
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                if (pagerImages.isNotEmpty()) {
                    Box(){
                        AppCompactPager(pagerState, pagerImages)
                        MovieDescription(modifier = Modifier.align(Alignment.BottomStart), movie = moviesList[pagerState.currentPage], !pagerState.isScrollInProgress )
                    }
                }

                for (movieByGenre in listByGenre) {
                    if (movieByGenre.movieList.isNotEmpty()) {
                        MovieGenre(genre = movieByGenre.genreTitle)
                        LazyRow {
                            item {
                                movieByGenre.movieList.forEach { movie ->
                                    MovieItem(movie = movie)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: PopularMoviesResponse.PopularMoviesList){
    Card(modifier = Modifier
        .padding(horizontal = 5.dp, vertical = 20.dp)
        .width(160.dp)
        .height(250.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(Color.Black)
    ) {
        Column(){
            Image(
                modifier = Modifier
                    .fillMaxSize() ,
                painter = rememberAsyncImagePainter("${BuildConfig.API_IMAGE_BASE_URL}${movie.poster_path}"),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun MovieImageTailingView(text: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text =text, color = Color.White, style = TextStyle(fontWeight = FontWeight.W500, fontSize = 16.sp))
        AppButton(buttonModel = ButtonModel("See More", alignment = Alignment.Center))
    }
}

@Composable
fun MoviesTopTabbedBar(modifier: Modifier, pagerModel: PagerModel, scrollDirection: MutableState<ScrollState>){
    LazyColumn(modifier = modifier){
        item {
            AnimatedVisibility(
                modifier= Modifier.height(130.dp),
                visible =  scrollDirection.value == ScrollState.SCROLL_DOWN || scrollDirection.value == ScrollState.NO_SCROLL,
                enter =  slideInVertically() + fadeIn(),
                exit =  slideOutVertically() + fadeOut(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Transparent
                                )
                            )
                        ),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(40.dp)
                    ){
                        Image(painter = painterResource(id = R.drawable.ic_outline_home_24), contentDescription = "" , modifier = Modifier.size(100.dp, 40.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                                Icon(Icons.Default.Person, contentDescription = "",
                                    modifier = Modifier.size(30.dp),
                                    tint = Color.White
                                )
                            }

                    }
                    TabLayout(pagerModel)
                }
            }
        }
    }
}

@Composable
fun movieSearchItem(){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)) {
        Icon(
            Icons.Default.AccountBox,
            contentDescription = "",
            tint = Color.White
        )
        Text(text = "this is movie item", style = TextStyle(color = Color.White))
    }
}



@Preview
@Composable
fun preview(){
    MovieImageTailingView("Something . Something . Something")
}


