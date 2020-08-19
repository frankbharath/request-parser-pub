var app = angular.module("rentpal",["ui.router","pascalprecht.translate"]);
app.config(['$stateProvider', '$locationProvider','$translateProvider', function($stateProvider, $locationProvider, $translateProvider){
	$stateProvider
	.state("properties",{
		url:"/properties",
		views:{
			"main-view":{
				templateUrl : "/resources/html/property.html",
			    controller: "property"
			}
		}
	})
	.state("properties.add",{
		url:"/add",
		views:{
			"add-property-view":{
				templateUrl : "/resources/html/addproperty.html",
				controller: "addproperty"
			}
		}
	});
    $locationProvider.html5Mode({enabled: true, requireBase: false});
    $translateProvider.useStaticFilesLoader({
	    prefix: '/resources/i18N/',
	    suffix: '.json'
	});
	$translateProvider.preferredLanguage('en');
	$translateProvider.useSanitizeValueStrategy('escape');
}]);

app.controller("main-ctrl", function($scope, $translate,CONSTANTS) {
	this.currentLanguage = $translate.use();
	$scope.selectedTab='home';
	$scope.toggleClass = function(tab) {
		$scope.selectedTab=tab;
	};
	$scope.CONSTANTS=CONSTANTS;
});

app.constant('CONSTANTS', (function() {
	  return Object.freeze({
		  "HOUSETYPE":Object.freeze({
			  HOUSE:"HOUSE",
			  APPARTMENT:"APPARTMENT"
		  })
	  })
})());