var React = require('react');

var Rating = require('./Rating.react');

var RatingList = React.createClass({
    render: function() {
        var ratingNodes = Object.keys(this.props.data).map(function(index) {
            return (
                <Rating key={index} rating={this.props.data[index]} mark={this.props.data[index]}></Rating>
            )
        }, this);

        return (
            <div className="ratingList">
                {ratingNodes}
            </div>
        );
    }
});

module.exports = RatingList;