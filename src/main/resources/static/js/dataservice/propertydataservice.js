(function(){
	"use strict";
	var app=angular.module('rentpal');
	app.factory('PropertyDataServiceFactory', ["$resource","$cacheFactory", function($resource, $cacheFactory) {
		var cache = $cacheFactory('property');
		return $resource('/api/property/:type/:id', {type:"@type", id: "@id" }, {
	        create: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        },
			query:{
				method:'GET', 
				isArray:false
			},
			queryWithMeta:{
				method:'GET', 
				isArray:false,
				cache : cache
			},
			update: {
	            method: "PUT",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        }
	    });
	}]);
})();