(function() {
    'use strict';
    angular
        .module('prototipoRioDoceApp')
        .factory('Propriedade', Propriedade);

    Propriedade.$inject = ['$resource'];

    function Propriedade ($resource) {
        var resourceUrl =  'api/propriedades/:id';

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
