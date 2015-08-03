var AppDispatcher = require('../dispatcher/AppDispatcher');
var RecommendationConstants = require('../constants/RecommendationConstants');
var HttpRecommendation = require('../http/HttpRecommendation');

var RecommendationActions = {
    getByUser: function (user) {
        HttpRecommendation.getRecommendations(user).done(function (recommendations) {
            AppDispatcher.dispatch({
                actionType: RecommendationConstants.RECOMMENDATION_GET_BY_USER,
                recommendations: recommendations
            })
        });
    }
};

module.exports = RecommendationActions;