var React = require('react');

var Rating = require('./Rating.react');

var RatingList = React.createClass({
    render: function() {
        var ratingNodes = this.props.data.map(function(rating) {
            return (
                <Rating title={rating.title} mark={rating.mark}></Rating>
            )
        });

        return (
            <div className="ratingList">
                {ratingNodes}
            </div>
        );
    }
});

module.exports = RatingList;