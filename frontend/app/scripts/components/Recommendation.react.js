var React = require('react');

var Recommendation = React.createClass({
    render: function() {
        return (
            <div className="recommendation">
                <h3>{this.props.recommendation.title} <small>Score {this.props.recommendation.mark}</small></h3>
            </div>
        );
    }
});

module.exports = Recommendation;