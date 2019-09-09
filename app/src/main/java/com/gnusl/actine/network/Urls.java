package com.gnusl.actine.network;

public enum Urls {

    Schema("http://"),
    Host(Schema.getLink() + "atcine.com/"),
    API(Host.getLink() + "api/"),
    Auth(API.getLink() + "auth/"),
    Login(Auth.getLink() + "login"),
    Movies(API.getLink() + "movies/"),
    Movie(API.getLink() + "movie/"),
    MoviesGroups(Movies.getLink() + "groups"),
    MovieSuggest(Movie.getLink() + "%id%/suggest"),
    MovieFavorite(Movie.getLink() + "%id%/favourite"),
    MovieRemind(Movie.getLink() + "%id%/remind"),
    MovieDownload(Movie.getLink() + "%id%/download"),
    MoviesMyList(Movies.getLink() + "favourite"),
    MoviesSoon(Movies.getLink() + "soon"),
    MoviesDownloaded(Movies.getLink() + "download"),
    MovieComments(Movie.getLink() + "%id%/comment"),
    MovieLike(Movie.getLink() + "%id%/like"),
    MovieComment(Movie.getLink() + "comment/%id%/delete"),

    Series(API.getLink() + "series/"),
    Serie(API.getLink() + "serie/"),
    SeriesGroups(Series.getLink() + "groups"),
    SerieSuggest(Series.getLink() + "%id%/suggest"),
    SerieFavorite(Series.getLink() + "%id%/favourite"),
    SeriesMyList(Series.getLink() + "favourite"),
    SeriesDownloaded(Series.getLink() + "favourite"),

    Categories(API.getLink() + "categories"),


    Profiles(API.getLink() + "profiles"),
    Profile(API.getLink() + "profile"),
    UpdateProfile(Profile.getLink() + "/update"),
    CreateProfile(Profile.getLink() + "/create"),

     Help(API.getLink() + "help"),

    ;

    private String link;

    Urls(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
