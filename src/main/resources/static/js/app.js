(function(){
	"use strict";
	var app = angular.module("rentpal",["ui.router","pascalprecht.translate"]);
	app.config(['$stateProvider', '$locationProvider','$translateProvider', function($stateProvider, $locationProvider, $translateProvider){
		$stateProvider
		.state("properties",{
			url:"/properties",
			views:{
				"main-view":{
					templateUrl : "/resources/html/property.html",
					controller: "PropertyCtrl",
					controllerAs: "property"
				}
			}
		})
		.state("properties.add",{
			url:"/add",
			views:{
				"add-property-view":{
					templateUrl : "/resources/html/addproperty.html",
					controller: "AddPropertyCtrl",
					controllerAs:"addproperty"
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

	app.controller("MainController", ["$translate", function($translate) {
		var vm=this;
		vm.currentLanguage = $translate.use();
		vm.selectedTab='home';
		vm.toggleClass = function(tab) {
			vm.selectedTab=tab;
		};
	}]);

	app.constant('constants', (() => {
		return Object.freeze({
			"HOUSETYPE":Object.freeze({
				HOUSE:"HOUSE",
				APPARTMENT:"APPARTMENT"
			}),
			"REGEX":Object.freeze({
				NOTALLOWEDCHARS:/^[^`~.<>:"{}=+]+$/,
				NOTALLOWEDCHARSANDNUMS:/^[^`~.<>:"{}=+0-9]+$/,
				POSTALCODE:/^[0-9]{5}$/,
				DIGITAL:/^[0-9]+$/
			})
		})
	})());

	app.directive('rpLimitLength', () => {
		return {
			restrict: "A",
			require: 'ngModel',
			link: function (scope, element, attrs, ngModelCtrl) {
				let limitLength = parseInt(attrs.rpLimitLength, 10);
				ngModelCtrl.$parsers.push(function (value) {
					if (value!=undefined && value.length>limitLength){
						let newValue=value.substring(0, limitLength);
						ngModelCtrl.$setViewValue(newValue);
						ngModelCtrl.$render();
						return newValue;
					}
					return value;
				});
			}
		};
	});

	app.directive('rpNumber', () => {
		return {
			restrict: "A",
			require: 'ngModel',
			link: function (scope, element, attrs, ngModelCtrl) {
				attrs.$set("ngTrim", "false");
				ngModelCtrl.$parsers.push(function (value) {
					if (value!=undefined){
						let newValue=value.toString().replace(/[^0-9]/g, '');
						if(newValue!=value){
							ngModelCtrl.$setViewValue(newValue);
							ngModelCtrl.$render();
						}
						return newValue;
					}
				});
			}
		}
	});
	
	app.directive('rpFormSubmit', function () {
	    return {
	        restrict: 'A',
	        link: function (scope, elem) {
	            elem.on('submit', function () {
	                var firstInvalid = elem[0].querySelector('.ng-invalid');
	                if (firstInvalid) {
	                    firstInvalid.focus();
	                }
	            });
	        }
	    };
	});
})();