var React = require('react');

var Recommendation = React.createClass({
    render: function() {
        return (
            <div className="recommendation">
                <h2>{this.props.title} <small>{this.props.mark}/5</small></h2>
            </div>
        );
    }
});

module.exports = Recommendation;