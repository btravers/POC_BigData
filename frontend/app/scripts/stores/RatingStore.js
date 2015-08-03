var assign = require('object-assign');
var EventEmitter = require('events').EventEmitter;

var AppDispatcher = require('../dispatcher/AppDispatcher');
var RatingConstants = require('../constants/RatingConstants');

var CHANGE_EVENT = 'change';

var __ratings = [];

function update(ratings) {
    __ratings = ratings;
}

function create(rating) {
    __ratings.push(rating);
}

var RatingStore = assign({}, EventEmitter.prototype, {
    getAll: function () {
        return __ratings;
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
        case RatingConstants.RATING_GET_BY_USER:
            update(action.ratings);
            RatingStore.emitChange();
            break;
        case RatingConstants.RATING_CREATE:
            create(action.rating);
            RatingStore.emitChange();
            break;
    }

});

module.exports = RatingStore;