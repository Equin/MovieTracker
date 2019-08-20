# Movie Tracker

It is a test task application with 5 screens:
1. Main screen with list of aveliable genres, filter and search toolbar.
2. Movies list sreen with movie cover, short info and "Add to favorite" button.
3. Movie info screen with movie cover, full info and 4 tabs (Info, Cast, Review, Video).
4. Favorite movies screen.
5. Movie trailers screen.

- API calls made with Retrofit+OkHttp+GSON.
- Ability to work offline made by Room (saving/getting information from data base).
- For easy image loading used Glide library.
- To make asynchronous API calls and DB transactions and returning result in proper thread used RxJava2
- Made some custom views like GenreView on main screen, PinCodeView for Password reset dialog and CustomToolbarSearchView.
- Made sync with server for update all movies that saved in DB every 4 hours (by WorkManager).
- Loging to sync saved movies in DB with TMDB server. 
- Parental control to avoid showing 18+ movies, protected by password.
- Adding movies to favorites.
- Filterig and sorting movies.
- Searching movies.
- Saving covers to disk when cover is long pressed. 
- ZoomIn/ZoomOut on cover press.
- Support Horizontal/Vertical orientation.
- Ability to wotk offline.

## Screenshots

<div align="center">
<img src="/img/main.jpeg?raw=true" height="300" alt="Main" title="Main screen"/>
<img src="/img/movie_list.jpeg?raw=true" height="300" alt="movie_list info" title="List of movies"/> 
<img src="/img/menu.jpeg?raw=true" height="300" alt="menu" title="Menu"/> 
</div>

<br/>

<div align="center">
<img src="/img/favorite_list.jpeg?raw=true" height="300" alt="favorite_list" title="List of favorites"/>
<img src="/img/info.jpeg?raw=true" height="300" alt="info" title="Movie info"/> 
<img src="/img/cast.jpeg?raw=true" height="300" alt="cast" title="Movie cast"/> 
</div>

<br/>

<div align="center">
<img src="/img/review.jpeg?raw=true" height="300" alt="review" title="Movie review"/>
<img src="/img/video.jpeg?raw=true" height="300" alt="video" title="Movie video"/> 
<img src="/img/youtube.jpeg?raw=true" height="300" alt="trailer" title="Watch thrailers"/> 
</div>

<br/>

<div align="center">
<img src="/img/search.jpeg?raw=true" height="300" alt="search" title="Movies serach toolbar"/>
<img src="/img/reset_password.jpg?raw=true" height="300" alt="reset_password" title="Reset password dialog"/> 
</div>

<br/>

<div align="center">
<img src="/img/hor_movies_list.jpeg?raw=false" width="300" alt="hor_movies_list" title="Horizontal list of movies"/>
<img src="/img/hor_casts.jpeg?raw=false" width="300" alt="hor_casts" title="Horizontal movie casts"/> 
</div>

<br/>

<div align="center">
<img src="/img/hor_main.jpeg?raw=false" width="300" alt="hor_main" title="Horizontal main screen"/> 
  <img src="/img/hor_sort.jpeg?raw=true" height="300" alt="hor_sort" title="Horizontal Sort By dialog"/> 
</div>


## Libraries and technologies used

- [RxJava 2](https://github.com/ReactiveX/RxJava)
- [Retrofit 2](https://square.github.io/retrofit/)
- [OkHttp](https://github.com/square/okhttp)
- [Glide](https://github.com/bumptech/glide)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ButterKnife](https://github.com/JakeWharton/butterknife)
- [GSON](https://github.com/google/gson) 
- [RxBinding](https://github.com/JakeWharton/RxBinding) 
