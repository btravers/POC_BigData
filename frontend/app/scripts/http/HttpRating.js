var Config = require('../utils/Config');

module.exports = {

    createRating: function (rating) {
        return $.ajax({
            type: 'POST',
            url: Config.getUrl() + '/rating',
            contentType: "application/json",
            data: JSON.stringify(rating)
        });
    },

    getRating: function (user, movie) {
        return $.ajax({
            type: 'GET',
            url: Config.getUrl() + '/rating',
            data: {
                user: user,
                movie: movie
            },
            dataType: 'json',
            cache: false
        });
    },

    getRatings: function (user) {
        return $.ajax({
            type: 'GET',
            url: Config.getUrl() + '/ratings',
            data: {
                user: user
            },
            dataType: 'json',
            cache: false
        });
    }

};