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

    getRating: function(user, movie) {
        HttpRating.getRating(user, movie).done(function (rating) {
            if (rating) {
                return rating.mark;
            }
            return null;
        })
    },

    createRating: function (rating) {
        HttpRating.createRating(rating).done(function () {
            this.getRatings(rating.user);
        });
    }
};

module.exports = RatingActions;