var AppDispatcher = require('../dispatcher/AppDispatcher');
var MovieConstants = require('../constants/MovieConstants');
var HttpMovie = require('../http/HttpMovie');

var MovieActions = {
    search: function(text) {
        HttpMovie.searchMovies(text).done(function(movies) {
            AppDispatcher.dispatch({
                actionType: MovieConstants.MOVIE_SEARCH,
                movies: movies
            })
        });
    }
};

module.exports = MovieActions;