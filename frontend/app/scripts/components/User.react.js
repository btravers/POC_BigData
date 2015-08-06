var React = require('react');

var UserStore = require('../stores/UserStore');
var UserActions = require('../actions/UserActions');

function getUser() {
    return {
        user: UserStore.get()
    };
}

var User = React.createClass({
    getInitialState: function () {
        return getUser();
    },

    handleChange: function () {
        if (this.refs.userInput.getDOMNode().value > 0) {
            UserActions.update(this.refs.userInput.getDOMNode().value);
        }
    },

    componentDidMount: function () {
        UserStore.addChangeListener(this._onChange);
    },

    componentWillUnmount: function () {
        UserStore.removeChangeListener(this._onChange);
    },

    render: function () {
        return (
            <form className="user navbar-form navbar-left" role="search">
                <div className="form-group input-group input-group-sm">
                    <span className="input-group-addon">User</span>
                    <input type="text" ref="userInput" className="form-control" value={this.state.user} onChange={this.handleChange} size="6"/>
                </div>
            </form>
        );
    },

    _onChange: function () {
        this.setState(getUser());
    }
});

module.exports = User;