var React = require('react');

var Movie = React.createClass({
    render: function () {
        return (
            <div className="movie">
                <h2>
                    {this.props.title}
                </h2>
                <p>Mark: {this.props.mark}/5 ({this.props.nb} votes)</p>
                {this.props.children}
            </div>
        );
    }
});

module.exports = Movie;
