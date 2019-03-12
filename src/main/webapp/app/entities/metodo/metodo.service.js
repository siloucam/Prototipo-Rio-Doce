(function() {
    'use strict';
    angular
        .module('prototipoRioDoceApp')
        .factory('Metodo', Metodo);

    Metodo.$inject = ['$resource'];

    function Metodo ($resource) {
        var resourceUrl =  'api/metodos/:id';

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
