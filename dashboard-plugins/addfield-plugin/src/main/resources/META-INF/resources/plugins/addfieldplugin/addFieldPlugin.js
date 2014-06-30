(function () {
    "use strict";
    angular.module('hippo.essentials')
            .controller('addfieldpluginCtrl', function ($scope, $sce, $log, $rootScope, $http) {
                var endpointDocuments = $rootScope.REST.documents;
                var endpointAddField = $scope.endpoint = $rootScope.REST.dynamic + 'add-field/';
                $scope.fieldName = null;
                $scope.selectedDocumentType = null;


                $scope.addField = function () {

                var payload = Essentials.addPayloadData("fieldName", $scope.fieldName, null);
                Essentials.addPayloadData("selectedDocumentType", $scope.selectedDocumentType.name, payload);
                $http.post(endpointAddField, payload).success(function (data) {

                });
            };

            //############################################
            // INITIALIZE APP:
            //############################################
            $http.get(endpointDocuments).success(function (data) {
                $scope.documentTypes = data;
            });

            })
})();