(function(){
	"use strict";
	var app = angular.module("rentpal",["ui.router","pascalprecht.translate","ngResource","ngAnimate", "ngMaterial", "chart.js"]);
	app.config(['$stateProvider', '$locationProvider','$translateProvider','$httpProvider', function($stateProvider, $locationProvider, $translateProvider, $httpProvider){
		$stateProvider
		.state("dashboard",{
			url:"/dashboard",
			views:{
				"main-view":{
					templateUrl : "/resources/html/home.html",
					controller: "HomeCtrl",
					controllerAs: "home"
				}
			},
			resolve:{
				resolvedStats:['PropertyDataServiceFactory', function(PropertyDataServiceFactory){
					return PropertyDataServiceFactory.query({"type":"statistics"});
				}]
			}
		})
		.state("properties",{
			url:"/properties",
			views:{
				"main-view":{
					templateUrl : "/resources/html/property.html",
					controller: "PropertyCtrl",
					controllerAs: "property"
				}
			},
			params: {
				"searchQuery":'',
				"countRequired":true,
				"pageNo":1
			},
			resolve:{
				resolvedProperties:['PropertyDataServiceFactory','$stateParams', function(PropertyDataServiceFactory, $stateParams){
					var queryParams={};
					if($stateParams.searchQuery.trim().length>0){
						queryParams.searchQuery=$stateParams.searchQuery;
						
					}
					queryParams.countRequired=$stateParams.countRequired;
					queryParams.pageNo=$stateParams.pageNo;
					return PropertyDataServiceFactory.query(queryParams);
				}]
			}
		})
		.state("properties.add",{
			url:"/add",
			views:{
				"property-view":{
					templateUrl : "/resources/html/addproperty.html",
					controller: "AddPropertyCtrl",
					controllerAs:"addproperty"
				}
			}
		})
		.state("properties.edit",{
			url:"/edit/{id}",
			params: {
				"type":'',
				"id":''
			},
			views:{
				"property-view":{
					templateUrl : "/resources/html/editproperty.html",
					controller: "EditPropertyCtrl",
					controllerAs:"editproperty"
				}
			},
			resolve:{
				resolvedProperty:['PropertyDataServiceFactory','$stateParams', function(PropertyDataServiceFactory, $stateParams){
					var data={};
					data.type=$stateParams.type;
					data.id=$stateParams.id;
					return PropertyDataServiceFactory.get(data);
				}]
			}
		})
		.state("tenants", {
			url:"/tenants",
			params: {
				"searchQuery":'',
				"countRequired":true,
				"pageNo":1
			},
			views:{
				"main-view":{
					templateUrl : "/resources/html/tenants.html",
					controller: "TenantsCtrl",
					controllerAs: "tenant"
				}
			},
			resolve:{
				resolvedTenants:['TenantDataServiceFactory','$stateParams', 'constants', function(TenantDataServiceFactory, $stateParams, constants){
					var queryParams={};
					var searchQuery=$stateParams.searchQuery.replace(constants.NOTALLOWEDCHARS+'g', '').trim();
					if(searchQuery.length>0){
						queryParams.searchQuery=searchQuery;
						
					}
					queryParams.countRequired=$stateParams.countRequired;
					queryParams.pageNo=$stateParams.pageNo;
					return TenantDataServiceFactory.query(queryParams);
				}],
				countries:[ "$translate", "$http", function($translate, $http){
					return $http.get("/resources/countries/"+$translate.use()+".json",{"cache": true});
				}]
			}
		})
		.state("tenants.add",{
			url:"/add",
			views:{
				"tenant-view":{
					templateUrl : "/resources/html/addtenant.html",
					controller: "AddTenantCtrl",
					controllerAs:"addtenant"
				}
			}
		})
		.state("tenants.edit",{
			url:"/edit/{id}",
			params: {
				"id":''
			},
			views:{
				"tenant-view":{
					templateUrl : "/resources/html/edittenant.html",
					controller: "EditTenantCtrl",
					controllerAs:"edittenant"
				}
			},
			resolve:{
				resolvedTenant:['TenantDataServiceFactory','$stateParams', function(TenantDataServiceFactory, $stateParams){
					return TenantDataServiceFactory.get({"id":$stateParams.id});
				}]
			}
		}).
		state("tenants.addlease",{
			url:"/add/lease",
			params:{
				"tenantid":''
			},
			views:{
				"tenant-view":{
					templateUrl : "/resources/html/addlease.html",
					controller: "AddLeaseCtrl",
					controllerAs:"addlease"
				}
			},
			resolve:{
				resolvedProperties:['PropertyDataServiceFactory', function(PropertyDataServiceFactory){
					return PropertyDataServiceFactory.queryWithMeta({"allPropwithMeta":true});
				}]
			}
		}).
		state("lease",{
			url:"/leases",
			params:{
				"entry":{}
			},
			views:{
				"main-view":{
					templateUrl : "/resources/html/leases.html",
					controller: "LeasesCtrl",
					controllerAs:"leases"
				}
			},
			resolve:{
				resolvedLeases:['TenantDataServiceFactory','$stateParams', function(TenantDataServiceFactory, $stateParams){
					return TenantDataServiceFactory.get({"lease":"lease","id":$stateParams.entry.tenantid});
				}]
			}
		}).
		state("lease.editlease",{
			url:"/edit",
			params:{
				"lease":{}
			},
			views:{
				"lease-view":{
					templateUrl : "/resources/html/editlease.html",
					controller: "EditLeaseCtrl",
					controllerAs:"editlease"
				}
			},
			resolve:{
				resolvedProperties:['PropertyDataServiceFactory', function(PropertyDataServiceFactory){
					return PropertyDataServiceFactory.queryWithMeta({"allPropwithMeta":true});
				}]
			}
		}).
		state("settings",{
			url:"/settings",
			views:{
				"main-view":{
					templateUrl : "/resources/html/settings.html",
					controller: "SettingsCtrl",
					controllerAs:"settings"
				}
			}
		});
		$locationProvider.html5Mode({enabled: true, requireBase: false});
		$translateProvider.useStaticFilesLoader({
			prefix: '/resources/i18n/',
			suffix: '.json'
		});
		$translateProvider.preferredLanguage('en');
		$translateProvider.useSanitizeValueStrategy('escape');
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		$httpProvider.interceptors.push(function($q, $window, $rootScope, $translate){
			return {
				'request': function(config) {
		       		$rootScope.$broadcast('showLoading');
		        	return config;
		      	},
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
	app.filter('truncate', function () {
	    return function (value, wordwise, max, tail) {
	        if (!value) return '';
	        max = parseInt(max, 10);
	        if (!max) return value;
	        if (value.length <= max) return value;
	
	        value = value.substr(0, max);
	        if (wordwise) {
	            var lastspace = value.lastIndexOf(' ');
	            if (lastspace != -1) {
	                value = value.substr(0, lastspace);
	            }
	        }
	        return value + (tail || ' …');
	    };
	});
	app.controller("MainController", ["$scope", "$translate", "$timeout",'$transitions', '$state', 'constants','$window', function($scope, $translate, $timeout, $transitions, $state, constants, $window) {
		var vm=this;
		vm.constants = constants;
		vm.currentLanguage = $translate.proposedLanguage();
		vm.selectedTab='dashboard';
		$state.go('dashboard');
		vm.showMessage=false;
		vm.loading = false;
		vm.toggleClass = function(tab) {
			vm.setSelectedTab(tab);
			var searchParams={
				"pageNo":1
			};
			$state.go(tab, searchParams, {reload: true});
		};
		$scope.$on('showLoading', function(event){
			vm.showLoading();
		});
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
			vm.hideLoading();
        });
		vm.setSelectedTab=function(tab){
			vm.selectedTab=tab;
		}
		vm.search = function(){
			if(vm.selectedTab === 'dashboard' || vm.selectedTab === 'settings'){
				vm.setSelectedTab("properties");
				$state.go("properties", {"searchQuery":vm.searchQuery,"countRequired":true, "pageNo":1}, {reload: true});
			}else{
				$state.go(vm.selectedTab, {"searchQuery":vm.searchQuery,"countRequired":true, "pageNo":1}, {reload: true});
			}
			
		}
		vm.showLoading=function(){
			vm.loading = true;
		}
		vm.hideLoading=function(){
			vm.loading = false;
		}
		$transitions.onStart({}, function($transition){
			vm.showLoading();
		});
		$transitions.onSuccess({}, function($transition){
			vm.hideLoading();
		});
		vm.logout=function(){
			$window.location="/logout";
		}
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
				DIGITAL:/^[1-9][0-9]$/,
				EMAIL:/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
				ALPHAWITHSPACE:/^[a-zA-Z]+(?:[\s]{1}[a-zA-Z]+)*$/,
				PASSWORD:/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,64}$/
			})
		})
	})());
	
	//Isolation level not possible because of multiple directives
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
						if(newValue!=value.toString()){
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
	            element.on('submit', function (e) {
					try{
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
					}catch(e){
						console.log(e);
					}
					
	            });
	        }
	    };
	});
	app.directive('rpConfirmClick', function($mdDialog){
		return {
			restrict: 'A',
			//scope: {confirmFunction: "&rpConfirmClick" },
			link: function( scope, element, attrs ){
				element.on( 'click', function(e){
					var confirm = $mdDialog.confirm()
						.title(attrs.rpConfirmTitle)
						.textContent(attrs.rpConfirmMessage)
						.clickOutsideToClose(true)
						.ok(attrs.rpConfirmOk)
						.cancel(attrs.rpConfirmCancel);
				    $mdDialog.show(confirm).then(function() {
				    	 scope.$eval(attrs.rpConfirmClick)
				    }, function() {
				    });
				});
			}
		};
	});
	app.directive('rpCompareTo', function(){
		return {
			restrict: 'A',
			require: "ngModel",
			scope: {
		        otherModelValue: "=rpCompareTo"
			},
			link: function(scope, element, attributes, ngModel) {
			    ngModel.$validators.rpCompareTo = function(modelValue) {
					return modelValue === '' || scope.otherModelValue===undefined || modelValue == scope.otherModelValue;
			    };
			    scope.$watch("otherModelValue", function() {
			      ngModel.$validate();
			    });
		  	}
		}
	});
})();