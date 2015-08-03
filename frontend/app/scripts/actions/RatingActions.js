var AppDispatcher = require('../dispatcher/AppDispatcher');
var RatingConstants = require('../constants/RatingConstants');
var HttpRating = require('../http/HttpRating');

var RatingActions = {
    getRatings: function (user) {
        HttpRating.getRatings(user).done(function (ratings) {
            AppDispatcher.dispatch({
                actionType: RatingConstants.RATING_GET_BY_USER,
                ratings: ratings
            })
        });
    },

    createRating: function (rating) {
        HttpRating.createRating(rating).done(function () {
            AppDispatcher.dispatch({
                actionType: RatingConstants.RATING_CREATE,
                rating: rating
            })
        });
    }
};

module.exports = RatingActions;