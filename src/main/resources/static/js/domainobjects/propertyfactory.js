(function(){
	"use strict";
	var app=angular.module('rentpal');
	app.factory('PropertyFactory', ["constants", function(constants) {
		class Property{
			#name;
			#address1;
			#address2;
			#postalcode;
			#city;
			#type;
			set name(name){
				if(!constants.REGEX.NOTALLOWEDCHARS.test(name) || name.trim().length==0){
					throw new Error("Invalid property name.");
				}
				this.#name=name;
			}
			get name(){
				return this.#name;
			}
			set address1(address1){
				if(!constants.REGEX.NOTALLOWEDCHARS.test(address1) || address1.trim().length==0){
					throw new Error("Invalid address.");
				}
				this.#address1=address1;
			}
			get address1(){
				return this.#address1;
			}
			set address2(address2){
				if(!constants.REGEX.NOTALLOWEDCHARS.test(address2) || address2.trim().length==0){
					throw new Error("Invalid address.");
				}
				this.#address2=address2;
			}
			get address2(){
				return this.#address2;
			}
			set postalcode(postalcode){
				if(!constants.REGEX.POSTALCODE.test(postalcode) || postalcode.trim().length==0){
					throw new Error("Invalid postal code.");
				}
				this.#postalcode=postalcode;
			}
			get postalcode(){
				return this.#postalcode;
			}
			set city(city){
				if(!constants.REGEX.NOTALLOWEDCHARSANDNUMS.test(city) || city.trim().length==0){
					throw new Error("Invalid city.");
				}
				this.#city=city;
			}
			get city(){
				return this.#city;
			}
			set type(type){
				if(type!=constants.HOUSETYPE.HOUSE && type!=constants.HOUSETYPE.APPARTMENT){
					throw new Error("Invalid property type");
				}
				this.#type=type;
			}
			get type(){
				return this.#type;
			}
			/*toJSON = function() {
				
			}*/
		}
		return Property;
	}]);
	app.factory('PropertyDetailsFactory', ["constants", function(constants) {
		class PropertyDetails{
			#area;
			#rent;
			#capacity;
			set area(area){
				if(!constants.REGEX.DIGITAL.test(area) || area.length==0){
					throw new Error("Invalid area.");
				}
				this.#area=area;
			}
			get area(){
				return this.#area;
			}
			set rent(rent){
				if(!constants.REGEX.DIGITAL.test(rent) || rent.length==0){
					throw new Error("Invalid rent.");
				}
				this.#rent=rent;
			}
			get rent(){
				return this.#rent;
			}
			set capacity(capacity){
				if(!constants.REGEX.DIGITAL.test(capacity) || capacity.length==0){
					throw new Error("Invalid capacity.");
				}
				this.#capacity=capacity;
			}
			get capacity(){
				return this.#capacity;
			}
		}
		return PropertyDetails;
	}]);
	app.factory('AppartmentDetailsFactory', ["PropertyDetailsFactory", function() {
		class AppartmentDetails extends PropertyDetailsFactory{
			#doorno;
			#floorno;
			set doorno(doorno){
				if(!constants.REGEX.DIGITAL.test(doorno) || doorno.length==0){
					throw new Error("Invalid door number.");
				}
				this.#doorno=doorno;
			}
			get doorno(){
				return this.#doorno;
			}
			set floorno(rent){
				if(!constants.REGEX.DIGITAL.test(floorno) || floorno.length==0){
					throw new Error("Invalid floor number.");
				}
				this.#floorno=floorno;
			}
			get floorno(){
				return this.#floorno;
			}
		}
		return AppartmentDetails;
	}]);
	app.factory('HouseFactory', ["PropertyFactory", "PropertyDetailsFactory", function(PropertyFactory, PropertyDetailsFactory) {
		class House extends PropertyFactory{
			#propertydetails;
			set propertydetails(propertydetails){
				if(!(propertydetails instanceof PropertyDetailsFactory)){
					throw new Error("Property details should be an instance of PropertyDetailsFactory");
				}
				this.#propertydetails=propertydetails;
			}
			get propertydetails(){
				return this.#propertydetails;
			}
		}
		return House;
	}]);
	app.factory('AppartmentFactory', ["PropertyFactory", "AppartmentDetailsFactory", function(PropertyFactory, AppartmentDetailsFactory) {
		class Appartment extends PropertyFactory{
			#appartmentdetails=[];
			set appartmentdetails(appartmentdetails){
				if(!(appartmentdetails instanceof AppartmentDetailsFactory)){
					throw new Error("Appartment Property details should be an instance of AppartmentDetailsFactory");
				}
				this.#appartmentdetails.push(appartmentdetails);
			}
		}
		return Appartment;
	}]);
})();