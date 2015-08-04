var React = require('react');
var Movie = require('./Movie.react');

var MovieList = React.createClass({
    render: function() {
        var movieNodes = this.props.data.map(function(movie) {
            return (
                <Movie key={movie.id} movie={movie}></Movie>
            );
        });

        return (
            <div className="movieList">
                {movieNodes}
            </div>
        );
    }
});

module.exports = MovieList;