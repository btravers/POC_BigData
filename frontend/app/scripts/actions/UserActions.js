var AppDispatcher = require('../dispatcher/AppDispatcher');
var UserConstants = require('../constants/UserConstants');

var UserActions = {
    update: function (user) {
        AppDispatcher.dispatch({
            actionType: UserConstants.USER_UPDATE,
            user: user
        })
    }
};

module.exports = UserActions;