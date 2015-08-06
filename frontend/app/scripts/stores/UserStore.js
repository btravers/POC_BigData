var assign = require('object-assign');
var EventEmitter = require('events').EventEmitter;

var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');

var CHANGE_EVENT = 'change';

var __user = 1;

function update(user) {
    __user = user;
}


var UserStore = assign({}, EventEmitter.prototype, {
    get: function () {
        return __user;
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
        case UserConstants.USER_UPDATE:
            update(action.user);
            UserStore.emitChange();
            break;
    }

});

module.exports = UserStore;