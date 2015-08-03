var React = require('react');

var Rating = React.createClass({
    render: function() {
        return (
            <div className="rating">
                <h2>{this.props.title} <small>{this.props.mark}/5</small></h2>
            </div>
        );
    }
});

module.exports = Rating;