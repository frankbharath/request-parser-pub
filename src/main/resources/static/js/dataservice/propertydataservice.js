(function(){
	"use strict";
	var app=angular.module('rentpal');
	app.factory('PropertyDataServiceFactory', ["$resource", function($resource) {
		return $resource('/api/property/:type/:id', {type:"@type", id: "@id" }, {
	        create: {
	            method: "POST",
	            transformRequest: angular.identity,
	            headers: { 'Content-Type': undefined }
	        }
	    });
	}]);
})();