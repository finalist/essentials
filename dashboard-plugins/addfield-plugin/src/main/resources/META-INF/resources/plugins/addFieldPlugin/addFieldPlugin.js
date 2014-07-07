(function () {
    "use strict";
    angular.module('hippo.essentials')
            .controller('addfieldpluginCtrl', function ($scope, $sce, $log, $rootScope, $http) {
                var endpointDocuments = $rootScope.REST.documents;
                var endpointAddField = $scope.endpoint = $rootScope.REST.dynamic + 'add-field/';
                $scope.pluginId = "addFieldPlugin";
                $scope.fieldName = null;
                $scope.selectedDocumentType = null;


                $scope.addField = function () {
                    var payload = Essentials.addPayloadData("fieldName", $scope.fieldName, null);
                    Essentials.addPayloadData("documentType", $scope.selectedDocumentType.name, payload);
                    Essentials.addPayloadData("fieldPosition", $scope.fieldPosition, payload);
                    Essentials.addPayloadData("namespace", $scope.selectedDocumentType.prefix, payload);
                    $http.post(endpointAddField, payload).success(function (data) {
                    });
                };
                $scope.positionMap = {
                    '${cluster.id}.right.item': 'right',
                    '${cluster.id}.left.item' : 'left'
                };

                // when changing the document type, set the default position and retrieve a fresh list of fields
                $scope.$watch('selectedDocumentType', function (newDocType) {
                    if (newDocType) {
                        $scope.positionMatters = newDocType.fieldLocations.length > 1;
                        $scope.fieldPosition = newDocType.fieldLocations[0];
                        reloadSelectionFields(newDocType);
                    }
                }, true);

                //############################################
                // INITIALIZE APP:
                //############################################
                $http.get(endpointDocuments).success(function (data) {
                    $scope.documentTypes = data;
                });

            })
})();