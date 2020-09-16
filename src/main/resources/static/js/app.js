(function(){
	"use strict";
	var app = angular.module("rentpal",["ui.router","pascalprecht.translate","ngResource","ngAnimate"]);
	app.config(['$stateProvider', '$locationProvider','$translateProvider','$httpProvider', function($stateProvider, $locationProvider, $translateProvider, $httpProvider){
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
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		$httpProvider.interceptors.push(function($q, $window, $rootScope, $translate){
			return {
				'response': function (response) {
					$rootScope.$broadcast('showGeneralMessage',{message:response.data.message,error:false});
					return response;
				},
				'responseError': function(response) {
      				if (response.status === 401 || response.status === 403) {
						$window.location.reload();
      				}else{
						if(response.data.message==undefined){
							$translate('error.unabletoprocessrequest').then(function (translatedValue) {
					   			$rootScope.$broadcast('showGeneralMessage',{message:translatedValue,error:true});
							});
						}else{
							$rootScope.$broadcast('showGeneralMessage',{message:response.data.message,error:true});
						}
					}
      				return $q.reject(response);
    			}
			};
		});
	}]);

	app.controller("MainController", ["$scope", "$translate", "$timeout",'$transitions', function($scope, $translate, $timeout, $transitions) {
		var vm=this;
		vm.currentLanguage = $translate.use();
		vm.selectedTab='home';
		vm.showMessage=false;
		vm.loading = false;
		vm.toggleClass = function(tab) {
			vm.selectedTab=tab;
		};
		
		$scope.$on('showGeneralMessage', function(event, data){
			if(data.message){
				if(vm.timeout){
					vm.showMessage=false;
					$timeout.cancel(vm.timeout);
				}
				vm.timeout = $timeout(function() {
					 vm.showMessage=false;
				}, 3000);
	            vm.showMessage=true;
				vm.isErrorMessage=data.error;
				vm.errorMessage=data.message;
			}
        });
		
		$transitions.onStart({}, function($transition){
			vm.loading = true;
		});
		$transitions.onSuccess({}, function($transition){
			vm.loading = false;
		});
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
				DIGITAL:/^[1-9][0-9]$/
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
						newValue=newValue.replace(/^[0]+/g,'');
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
	
	app.directive('rpWholeNumber', () => {
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
	
	app.directive('rpAlphaNumber', () => {
		return {
			restrict: "A",
			require: 'ngModel',
			link: function (scope, element, attrs, ngModelCtrl) {
				attrs.$set("ngTrim", "false");
				ngModelCtrl.$parsers.push(function (value) {
					if (value!=undefined){
						let newValue=value.toString().replace(/[^0-9A-Za-z]/g, '');
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
	
	app.directive('rpFormSubmit', function ($timeout, $animate) {
	    return {
	        restrict: 'A',
			scope:{},
			require: '^form',
	        link: function (scope, element, attrs, form) {
	            element.on('submit', function () {
					if (!form.$valid){
		                var firstInvalid = element[0].querySelector('.ng-invalid');
		                if (firstInvalid) {
		                    firstInvalid.focus();
		                }
						$animate.addClass(element, 'shake').then(function() {
							$timeout(function(){
								$animate.removeClass(element, 'shake');
							},200);
							
						});
					}
	            });
	        }
	    };
	});
})();