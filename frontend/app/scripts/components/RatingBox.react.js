var React = require('react');

var RatingList = require('./RatingList.react');
var RatingStore = require('../stores/RatingStore');

function getRatingState() {
    return {
        data: RatingStore.getAll()
    };
}

var RatingBox = React.createClass({
    getInitialState: function () {
        return getRatingState();
    },

    componentDidMount: function() {
        RatingStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        RatingStore.removeChangeListener(this._onChange);
    },

    render: function() {
        return (
            <div className="ratingBox">
                <RatingList data={this.state.data}/>
            </div>
        );
    },

    _onChange: function() {
        this.setState(getRatingState());
    }
});

module.exports = RatingBox;