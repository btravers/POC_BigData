var assign = require('object-assign');
var EventEmitter = require('events').EventEmitter;

var AppDispatcher = require('../dispatcher/AppDispatcher');
var RatingConstants = require('../constants/RatingConstants');
var RatingActions = require('../actions/RatingActions');
var UserConstants = require('../constants/UserConstants');

var CHANGE_EVENT = 'change';

var __ratings = {};

function update(ratings) {
    __ratings = ratings;
}

var RatingStore = assign({}, EventEmitter.prototype, {
    getAll: function () {
        return __ratings;
    },

    get: function (id) {
        if (__ratings[id]) {
            return __ratings[id].mark;
        }
        return null;
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
        case UserConstants.USER_UPDATE:
            RatingActions.getRatings(action.user);
            break;
    }

});

module.exports = RatingStore;