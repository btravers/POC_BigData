var Config = require('../utils/Config');

module.exports = {

    createRating: function (rating) {
        return $.ajax({
            type: 'post',
            url: Config.getUrl() + '/rating',
            data: rating,
            dataType: 'json',
            cache: false
        });
    },

    getRating: function (user, movie) {
        return $.ajax({
            type: 'get',
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
            type: 'get',
            url: Config.getUrl() + '/ratings',
            data: {
                user: user
            },
            dataType: 'json',
            cache: false
        });
    }

};