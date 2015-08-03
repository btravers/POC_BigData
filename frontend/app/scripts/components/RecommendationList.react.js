var React = require('react');

var Recommendation = require('./Recommendation.react');

var RecommendationList = React.createClass({
    render: function() {
        var recommendationNodes = this.props.data.map(function(recommendation) {
            return (
                <Recommendation title={recommendation.title} mark={recommendation.mark}></Recommendation>
            )
        });

        return (
            <div className="ratingList">
                {recommendationNodes}
            </div>
        );
    }
});

module.exports = RecommendationList;