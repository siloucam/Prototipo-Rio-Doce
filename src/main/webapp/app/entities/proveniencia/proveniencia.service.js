(function() {
    'use strict';
    angular
        .module('prototipoRioDoceApp')
        .factory('Proveniencia', Proveniencia);

    Proveniencia.$inject = ['$resource'];

    function Proveniencia ($resource) {
        var resourceUrl =  'api/proveniencias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
