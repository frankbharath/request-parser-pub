(function() {
	"use strict";
	var app=angular.module('rentpal');
	var stateParams={
		totalProperties:0
	}
	app.controller("PropertyCtrl", ["$state", "resolvedProperties", "$stateParams", "PropertyDataServiceFactory","constants",'$cacheFactory', function($state, resolvedProperties, $stateParams, PropertyDataServiceFactory, constants, $cacheFactory) {
		var vm = this;
		vm.properties=[];
		vm.hideAddProperty = true;
		vm.constants=constants;
		vm.loaded=false;
		vm.minPage=1;
		vm.currentPage=$stateParams.pageNo;
		vm.maxPage=1;
		vm.limit=50;
		vm.showMoreDetails=false;
		vm.selectedProperties=0;
		vm.selectProperty={};
		vm.addProperty = function() {
			$state.go('properties.add');
		}
		vm.processResult=function(res){
			if(res.data.count!=undefined){
				vm.totalproperties=res.data.count;
				stateParams.totalProperties=vm.totalproperties;
			}else{
				vm.totalproperties=stateParams.totalProperties;
			}
			vm.maxPage=parseInt(vm.totalproperties/vm.limit);
			if(vm.maxPage*vm.limit<vm.totalproperties){
				vm.maxPage=vm.maxPage+1;
			}
			vm.properties=res.data.propertylist;
			if(vm.properties.length>0){
				vm.selectedProperty=vm.properties[0];
				vm.selectedIndex=0;
			}else{
				vm.finished();
			}
		}
		resolvedProperties.$promise
		.then(function(res) {
			vm.processResult(res);
		}).catch(angular.noop);
		
		vm.finished=function(){
			vm.loaded=true;
		}
		vm.showDetails=function(index){
			if(index!==vm.selectedIndex){
				vm.selectedProperty=vm.properties[index];
				vm.selectedIndex=index;
				vm.moreDetails=undefined;
				vm.hidePropertyDetails();
			}
		}
		vm.pagenation=function(bool){
			if(bool){
				if(vm.currentPage+1<=vm.maxPage){
					vm.currentPage++;
					$state.go("properties", {"countRequired":false, "pageNo":vm.currentPage}, {reload: true});
				}
			}else{
				if(vm.currentPage-1>=vm.minPage){
					vm.currentPage--;
					$state.go("properties", {"countRequired":false, "pageNo":vm.currentPage}, {reload: true});
				}
			}
		}
		vm.getPropertyDetails=function(){
			if(vm.moreDetails==undefined){
				PropertyDataServiceFactory.get({type: vm.selectedProperty.propertytype.toLowerCase(), id:vm.selectedProperty.propertyid}).$promise
				.then(function(res) {
					if(vm.selectedProperty.propertytype===vm.constants.HOUSETYPE.HOUSE){
						vm.moreDetails=res.data.detailsDTO;
					}else{
						vm.moreDetails=res.data.units;
					}
					vm.showMoreDetails=true;
				}).catch(angular.noop);
			}else{
				vm.showMoreDetails=true;
			}	
		}
		vm.hidePropertyDetails=function(){
			vm.showMoreDetails=false;
		}	
		vm.edit=function(index){
			$state.go("properties.edit", {"type":vm.properties[index].propertytype.toLowerCase(), "id":vm.properties[index].propertyid});
		}
		vm.deleteProperty=function(index){
			PropertyDataServiceFactory.delete({"propertyIds":vm.properties[index].propertyid}).$promise
			.then(function(res) {
				$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
				$state.go("properties", {"countRequired":true}, {reload: true});
			}).catch(angular.noop);
		}
		vm.deleteProperties=function(){
			var propertyIds=[];
			for(var index in vm.selectProperty){
				if(vm.selectProperty[index]){
					propertyIds.push(index);
				}
			}
			PropertyDataServiceFactory.delete({"propertyIds":propertyIds}).$promise
			.then(function(res) {
				if(propertyIds.length===vm.properties.length){
					if(vm.currentPage>1){
						vm.currentPage--;
					}
				}
				$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
				$state.go("properties", {"countRequired":true, "pageNo":vm.currentPage}, {reload: true});
			}).catch(angular.noop);
		}
		vm.checkBoxChange=function(index){
			if(vm.selectProperty[index]){
				vm.selectedProperties++;
			}else{
				vm.selectedProperties--;
				if(vm.selectedProperties===0){
					vm.selectAll=false;
				}
			}
		}
		vm.selectAllChange=function(e){
			if(!vm.selectAll){
				for(var index in vm.properties){
					vm.selectProperty[vm.properties[index].propertyid]=false;
				}
				vm.selectedProperties=0;
			}else{
				for(var index in vm.properties){
					vm.selectProperty[vm.properties[index].propertyid]=true;
				}
				vm.selectedProperties=vm.properties.length;
			}
		}
	}]);

	app.controller("AddPropertyCtrl", ["$state", "constants", "PropertyDataServiceFactory",'$cacheFactory', function($state, constants, PropertyDataServiceFactory, $cacheFactory) {
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
			if (vm.form.$valid) {
				var payload = new FormData();
				payload.append("name", vm.propertyname);
				var address={};
				address.line_1=vm.address_1;
				address.line_2=vm.address_2;
				address.postalcode=vm.postalcode;
				address.city=vm.city;
				payload.append("address", JSON.stringify(address));
				payload.append("propertytype", vm.type);
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
						$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
						$state.go("properties", {"countRequired":true}, {reload: true});
					}).catch(angular.noop);
				} else {
					var propertyDetails = {};
					propertyDetails.area = vm.area;
					propertyDetails.rent = vm.rent;
					propertyDetails.capacity = vm.capacity;
					payload.append("propertydetails", JSON.stringify(propertyDetails));
					PropertyDataServiceFactory.create({ type: 'house' }, payload).$promise
					.then(function(res) {
						$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
						$state.go("properties", {"countRequired":true}, {reload: true});
					}).catch(angular.noop);
				}
			}
		}
	}]);
	app.controller("EditPropertyCtrl", ["$state","resolvedProperty", "constants", "PropertyDataServiceFactory",'$cacheFactory', function($state, resolvedProperty, constants, PropertyDataServiceFactory, $cacheFactory) {
		var vm=this;
		vm.constants = constants;
		vm.property={};
		vm.showForm=false;
		vm.deletedIds=[];
		resolvedProperty.$promise
		.then(function(res) {
			vm.property=res.data;
		}).catch(angular.noop);
		vm.closeTemplate = function() {
			$state.go('properties');
		}
		vm.finished=function(){
			vm.showForm=true;
		}
		vm.addUnit = function() {
			let entry = {};
			vm.property.units.push(entry);
		}
		vm.removeUnit = (index) => {
			vm.deletedIds.push(vm.property.units[index].propertydetailsid);
			vm.property.units.splice(index, 1);
		}
		vm.submit = function() {
			var payload = new FormData();
			payload.append("name", vm.property.propertyname);
			var address={};
			address.line_1=vm.property.address.addressline_1;
			address.line_2=vm.property.address.addressline_2;
			address.postalcode=vm.property.address.postal;
			address.city=vm.property.address.city;
			payload.append("propertyid", vm.property.propertyid);
			payload.append("address", JSON.stringify(address));
			payload.append("propertytype", vm.property.propertytype);
			if (vm.form.$valid) {
				if (vm.property.propertytype === constants.HOUSETYPE.APPARTMENT) {
					var units = [];
					vm.property.units.forEach(function(value) {
						var unit = {};
						unit.area = value.area;
						unit.capacity = value.capacity;
						unit.doorno = value.doorno;
						unit.floorno = value.floorno;
						unit.rent = value.rent;
						unit.propertydetailsid=value.propertydetailsid;
						units.push(unit);
					});
					payload.append("units", JSON.stringify(units));
					for(var id in vm.deletedIds){
						payload.append("deleteIds", vm.deletedIds[id]);
					}
					PropertyDataServiceFactory.update({ type: 'appartment' }, payload).$promise
					.then(function(res) {
						$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
						$state.go("properties", {"countRequired":true}, {reload: true});
					}).catch(angular.noop);
				}else{
					var propertyDetails = {};
					propertyDetails.area = vm.property.detailsDTO.area;
					propertyDetails.rent = vm.property.detailsDTO.rent;
					propertyDetails.capacity = vm.property.detailsDTO.capacity;
					propertyDetails.propertydetailsid = vm.property.detailsDTO.propertydetailsid;
					payload.append("propertydetails", JSON.stringify(propertyDetails));
					PropertyDataServiceFactory.update({ type: 'house' }, payload).$promise
					.then(function(res) {
						$cacheFactory.get('property').remove('/api/property?allPropwithMeta=true');
						$state.go("properties", {"countRequired":true}, {reload: true});
					}).catch(angular.noop);
				}
			}
		}
	}]);
})();
