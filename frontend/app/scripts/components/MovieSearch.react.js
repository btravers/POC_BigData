var React = require('react');

var MovieActions = require('../actions/MovieActions');

var MovieSearch = React.createClass({
    handleChange: function () {
        MovieActions.search(this.refs.searchTextInput.getDOMNode().value);
    },

    render: function () {
        return (
            <form className="movieSearch">
                <input
                    className="form-control"
                    type="text"
                    placeholder="Search..."
                    ref="searchTextInput"
                    onChange={this.handleChange}/>
            </form>
        );
    }
});

module.exports = MovieSearch;