var Config = require('../utils/Config');

module.exports = {

    getRecommendations: function (user) {
        return $.ajax({
            type: 'GET',
            url: Config.getUrl() + '/recommendations',
            data: {
                user: user
            },
            dataType: 'json',
            cache: false
        });
    }

};