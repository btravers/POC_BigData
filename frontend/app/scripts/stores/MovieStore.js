var assign = require('object-assign');
var EventEmitter = require('events').EventEmitter;

var AppDispatcher = require('../dispatcher/AppDispatcher');
var MovieConstants = require('../constants/MovieConstants');

var CHANGE_EVENT = 'change';

var __movies = [];

function update(movies) {
    __movies = movies;
}


var MovieStore = assign({}, EventEmitter.prototype, {
    getAll: function () {
        return __movies;
    },

    emitChange: function() {
        this.emit(CHANGE_EVENT);
    },

    addChangeListener: function(callback) {
        this.on(CHANGE_EVENT, callback);
    },

    removeChangeListener: function(callback) {
        this.removeListener(CHANGE_EVENT, callback);
    }
});

AppDispatcher.register(function (action) {

    switch (action.actionType) {
        case MovieConstants.MOVIE_SEARCH:
            update(action.movies);
            MovieStore.emitChange();
            break;
    }

});

module.exports = MovieStore;