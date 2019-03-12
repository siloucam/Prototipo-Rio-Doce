(function() {
    'use strict';
    angular
        .module('prototipoRioDoceApp')
        .factory('DataSource', DataSource);

    DataSource.$inject = ['$resource', 'DateUtils'];

    function DataSource ($resource, DateUtils) {
        var resourceUrl =  'api/data-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dtinicial = DateUtils.convertLocalDateFromServer(data.dtinicial);
                        data.dtfinal = DateUtils.convertLocalDateFromServer(data.dtfinal);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtinicial = DateUtils.convertLocalDateToServer(copy.dtinicial);
                    copy.dtfinal = DateUtils.convertLocalDateToServer(copy.dtfinal);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtinicial = DateUtils.convertLocalDateToServer(copy.dtinicial);
                    copy.dtfinal = DateUtils.convertLocalDateToServer(copy.dtfinal);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
