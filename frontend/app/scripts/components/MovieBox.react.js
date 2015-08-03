var React = require('react');

var MovieSearch = require('./MovieSearch.react');
var MovieList = require('./MovieList.react');
var MovieStore = require('../stores/MovieStore');

function getMovieState() {
    return {
        data: MovieStore.getAll()
    };
}

var MovieBox = React.createClass({
    getInitialState: function () {
        return getMovieState();
    },

    componentDidMount: function() {
        MovieStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        MovieStore.removeChangeListener(this._onChange);
    },

    render: function () {
        return (
            <div className="movieBox">
                <div className="jumbotron">
                    <h1>Movies</h1>
                    <MovieSearch/>
                </div>
                <MovieList data={this.state.data}/>
            </div>
        );
    },

    _onChange: function() {
        this.setState(getMovieState());
    }
});

module.exports = MovieBox;