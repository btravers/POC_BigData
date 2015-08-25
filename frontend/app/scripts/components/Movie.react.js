var React = require('react');

var RatingActions = require('../actions/RatingActions');
var RatingStore = require('../stores/RatingStore');
var UserStore = require('../stores/UserStore');

function getRating(movie) {
    return {
        rating: RatingStore.get(movie)
    };
}

var Movie = React.createClass({
    getInitialState: function () {
        return getRating(this.props.movie.id);
    },

    handleChange: function () {
        RatingActions.createRating({
            mark: this.refs.ratingInput.getDOMNode().value,
            movie: this.props.movie.id,
            user: UserStore.get()
        });
    },

    componentDidMount: function () {
        RatingStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function () {
        RatingStore.removeChangeListener(this._onChange);
    },

    render: function () {
        return (
            <div className="movie">
                <hr/>

                <h2>
                    {this.props.movie.title}
                    <small className="text-muted">
                        {
                            this.props.movie.genres.map(function (genre) {
                                return (
                                    <span key={genre}> {genre} </span>
                                );
                            })
                        }
                    </small>
                </h2>

                <p className="text-muted">Mark: {this.props.movie.mark}/5 ({this.props.movie.nb} votes)</p>


                <select className="form-control" value={this.state.rating} onChange={this.handleChange}
                        ref={"ratingInput"}>
                    <option value="">-</option>
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                    <option>4</option>
                    <option>5</option>
                </select>
            </div>
        );
    },

    _onChange: function () {
        this.setState(getRating(this.props.movie.id));
    }
});

module.exports = Movie;
