var Config = require('../utils/Config');

module.exports = {

    getRecommendations: function (user) {
        return $.ajax({
            type: 'get',
            url: Config.getUrl() + '/recommendations',
            data: {
                user: user
            },
            dataType: 'json',
            cache: false
        });
    }

};