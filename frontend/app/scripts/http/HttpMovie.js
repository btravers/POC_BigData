var Config = require('../utils/Config');

module.exports = {

    getMovie: function (id) {
        return $.ajax({
            type: 'GET',
            url: Config.getUrl() + '/movie',
            data: {
                id: id
            },
            dataType: 'json',
            cache: false
        });
    },

    searchMovies: function (request) {
        return $.ajax({
            type: 'GET',
            url: Config.getUrl() + '/movies',
            data: {
                request: request
            },
            dataType: 'json',
            cache: false
        });
    }

};