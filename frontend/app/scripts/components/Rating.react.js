var React = require('react');

var Rating = React.createClass({
    render: function() {
        return (
            <div className="rating">
                <h3>{this.props.rating.title} <small>{this.props.rating.mark}/5</small></h3>
            </div>
        );
    }
});

module.exports = Rating;