(function() {
	"use strict";
	var app=angular.module('rentpal');
	app.controller("PropertyCtrl", ["$state", function($state) {
		var vm = this;
		vm.hideAddProperty = true;
		vm.addProperty = function() {
			$state.go('properties.add');
		}
	}]);

	app.controller("AddPropertyCtrl", ["$state", "$injector", "constants", "PropertyDataServiceFactory", function($state, $injector, constants, PropertyDataServiceFactory) {
		var vm = this;
		vm.constants = constants;
		vm.type = constants.HOUSETYPE.HOUSE;
		setTimeout(() => {
			vm.propertyname = "Maison Alfort_3";
			vm.address_1 = "10 Bis";
			vm.address_2 = "Place Des Martrys De La Deportation";
			vm.postalcode = "94400";
			vm.city = "Vitry Sur Seine";
			vm.area = "10";
			vm.rent = "20";
			vm.capacity = "30";
		}, 500);
		vm.units = [];
		vm.units.push({
			area:1,
			capacity:1,
			door:1,
			floor:1,
			rent:1
		});
		vm.typeRadioChange = function(event) {
			if (event.target.value === constants.HOUSETYPE.APPARTMENT) {
				vm.type = constants.HOUSETYPE.APPARTMENT;
			} else {
				vm.type = constants.HOUSETYPE.HOUSE;
			}
		};
		vm.addUnit = function() {
			let entry = {};
			entry.area = vm.units.length + 1;
			entry.capacity = vm.units.length + 1;
			entry.door = vm.units.length + 1;
			entry.floor = vm.units.length + 1;
			entry.rent = vm.units.length + 1;
			vm.units.push(entry);
		}
		vm.removeUnit = (index) => {
			vm.units.splice(index, 1);
		}
		vm.closeTemplate = function() {
			$state.go('properties');
		}
		vm.submit = function() {
			var payload = new FormData();
			payload.append("name", vm.propertyname);
			var address={};
			address.line_1=vm.address_1;
			address.line_2=vm.address_2;
			address.postalcode=vm.postalcode;
			address.city=vm.city;
			payload.append("address", JSON.stringify(address));
			payload.append("propertytype", vm.type);
			if (vm.form.$valid) {
				if (vm.type === constants.HOUSETYPE.APPARTMENT) {
					var units = [];
					vm.units.forEach(function(value) {
						var unit = {};
						unit.area = value.area;
						unit.capacity = value.capacity;
						unit.doorno = value.door;
						unit.floorno = value.floor;
						unit.rent = value.rent;
						units.push(unit);
					});
					payload.append("units", JSON.stringify(units));
					PropertyDataServiceFactory.create({ type: 'appartment' }, payload).$promise
					.then(function(res) {
						$state.go("properties", {}, { reload: true });
					}).catch(angular.noop);
				} else {
					var propertyDetails = {};
					propertyDetails.area = vm.area;
					propertyDetails.rent = vm.rent;
					propertyDetails.capacity = vm.capacity;
					payload.append("propertydetails", JSON.stringify(propertyDetails));
					PropertyDataServiceFactory.create({ type: 'house' }, payload).$promise
					.then(function(res) {
						$state.go("properties", {}, { reload: true });
					}).catch(angular.noop);
				}
			}
		}
	}]);
})();
