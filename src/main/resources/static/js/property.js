(function(){
	"use strict";
	var app=angular.module('rentpal');
	app.controller("PropertyCtrl", ["$state", function($state) {
		var vm=this;
		vm.hideAddProperty=true;
		vm.addProperty=function(){
			$state.go('properties.add');
		}
	}]);
	
	app.controller("AddPropertyCtrl", ["$state", "$injector", "constants", function($state, $injector, constants) {
		var vm=this;
		vm.constants=constants;
		vm.type=constants.HOUSETYPE.HOUSE;
		setTimeout(()=>{
			vm.propertyname="Maison Alfort";
			vm.address_1="10 Bis";
			vm.address_2="Place Des Martrys De La Deportation";
			vm.postalcode="94400";
			vm.city="Vitry Sur Seine";
			vm.area="10";
			vm.rent="20";
			vm.capacity="30";
		},500);
		vm.units=[];
		vm.typeRadioChange = function (event) {
	        if(event.target.value===constants.HOUSETYPE.APPARTMENT){
	        	vm.type=constants.HOUSETYPE.APPARTMENT;
	        }else{
	        	vm.type=constants.HOUSETYPE.HOUSE;
	        }
	    };
		vm.addUnit=function(){
	    	let entry={};
	    	vm.units.push(entry);
	    }
		vm.removeUnit=(index)=>{
			vm.units.splice(index,1);
		}
		vm.closeTemplate=function(){
			 $state.go('properties');
		}
		vm.submit=function(){
			if(vm.form.$valid){
				if(vm.type===constants.HOUSETYPE.APPARTMENT){
					
				}else{
					var HouseFactory=$injector.get("HouseFactory");
					var house=new HouseFactory();
					house.name=vm.propertyname;
					house.address1=vm.address_1;
					house.address2=vm.address_2;
					house.postalcode=vm.postalcode;
					house.city=vm.city;
					house.type=vm.type;
					var PropertyDetailsFactory=$injector.get("PropertyDetailsFactory");
					var propertyDetails=new PropertyDetailsFactory();
					propertyDetails.area=vm.area;
					propertyDetails.rent=vm.rent;
					propertyDetails.capacity=vm.capacity;
					house.propertydetails=propertyDetails;
				}
			}
		}
	}]);
})();
