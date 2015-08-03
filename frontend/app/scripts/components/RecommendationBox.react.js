var React = require('react');

var RecommendationList = require('./RecommendationList.react');
var RecommendationStore = require('../stores/RecommendationStore');

function getRecommendationState() {
    return {
        data: RecommendationStore.getAll()
    };
}

var RecommendationBox = React.createClass({
    getInitialState: function () {
        return getRecommendationState();
    },

    componentDidMount: function() {
        RecommendationStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function() {
        RecommendationStore.removeChangeListener(this._onChange);
    },

    render: function() {
        return (
            <div className="recommendationBox">

                <RecommendationList data={this.state.data}/>

            </div>
        );
    },

    _onChange: function() {
        this.setState(getRecommendationState());
    }
});

module.exports = RecommendationBox;