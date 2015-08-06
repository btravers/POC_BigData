var React = require('react');

var Router = require('react-router');
var ClassNames = require('classnames');

var User = require('./components/User.react');
var MovieBox = require('./components/MovieBox.react');
var RatingBox = require('./components/RatingBox.react');
var RecommendationBox = require('./components/RecommendationBox.react');
var Footer = require('./components/Footer.react');

var UserStore = require('./stores/UserStore');
var RecommendationActions = require('./actions/RecommendationActions');
var RatingActions = require('./actions/RatingActions');

var DefaultRoute = Router.DefaultRoute;
var Link = Router.Link;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var App = React.createClass({
    getInitialState: function () {
        RecommendationActions.getByUser(UserStore.get());
        RatingActions.getRatings(UserStore.get());

        return {};
    },

    render: function () {
        var movies = ClassNames({
            'active': (this.props.path == '/')
        });

        var ratings = ClassNames({
            'active': (this.props.path == '/ratings')
        });

        var recommendations = ClassNames({
            'active': (this.props.path == '/recommendations')
        });

        return (
            <div className="movieApp container">
                <div className="header">
                    <ul className="nav nav-pills pull-right">
                        <li className={movies}><Link to="movies">Home</Link></li>
                        <li className={ratings}><Link to="ratings">Ratings</Link></li>
                        <li className={recommendations}><Link to="recommendations">Recommendations</Link></li>
                        <li> <User /></li>
                    </ul>
                    <h3 className="text-muted">Movie library</h3>

                </div>

                <RouteHandler/>

                <Footer />
            </div>
        );
    }
});

var routes = (
    <Route name="movies" path="/" handler={App}>
        <Route name="ratings" handler={RatingBox}/>
        <Route name="recommendations" handler={RecommendationBox}/>
        <DefaultRoute handler={MovieBox}/>
    </Route>
);

Router.run(routes, function (Handler, state) {
    React.render(<Handler path={state.pathname}/>, document.getElementById('movieapp'));
});
