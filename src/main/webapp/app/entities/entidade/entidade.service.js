(function() {
    'use strict';
    angular
        .module('prototipoRioDoceApp')
        .factory('Entidade', Entidade);

    Entidade.$inject = ['$resource'];

    function Entidade ($resource) {
        var resourceUrl =  'api/entidades/:id';

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
