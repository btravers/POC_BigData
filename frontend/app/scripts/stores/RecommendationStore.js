var assign = require('object-assign');
var EventEmitter = require('events').EventEmitter;

var AppDispatcher = require('../dispatcher/AppDispatcher');
var RecommendationConstants = require('../constants/RecommendationConstants');

var CHANGE_EVENT = 'change';

var __recommendations = [];

function update(recommendations) {
    __recommendations = recommendations;
}


var RecommendationStore = assign({}, EventEmitter.prototype, {
    getAll: function () {
        return __recommendations;
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
        case RecommendationConstants.RECOMMENDATION_GET_BY_USER:
            update(action.recommendations);
            RecommendationStore.emitChange();
            break;
    }

});

module.exports = RecommendationStore;