(function(){
	"use strict";
	var app=angular.module('rentpal');
	app.factory('TenantDataServiceFactory', ["$resource", function($resource) {
		return $resource('/api/tenant/:id', {id: "@id" }, {
	        save: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        },
			query:{
				method:'GET', 
				isArray:false
			},
			update: {
	            method: "PUT",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        }
	    });
	}]);
})();