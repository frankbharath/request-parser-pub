(function() {
	"use strict";
	var app=angular.module('rentpal');
	app.controller("SettingsCtrl",["constants", "SettingDataServiceFactory", "$state",  function(constants, SettingDataServiceFactory, $state){
		var vm=this;
		vm.constants=constants;
		vm.password='';
		vm.confirmpassword='';
		vm.showPassword=false;
		vm.showConfirmPassword=false;
		vm.changepasswordfun=function(){
			var payload = new FormData();
			payload.append("password", vm.password);
			payload.append("confirmpassword", vm.confirmpassword);
			SettingDataServiceFactory.update({}, payload).$promise
			.then(function(res) {
				$state.go("settings", {}, {reload:true});
			}).catch(angular.noop);
		}
		vm.showPasswordFun=function(){
			if(vm.showPassword){
				vm.showPassword=false;
			}else{
				vm.showPassword=true;
			}
		}
		vm.showConfirmPasswordFun=function(){
			if(vm.showConfirmPassword){
				vm.showConfirmPassword=false;
			}else{
				vm.showConfirmPassword=true;
			}
		}
	}]);
})();