app.controller("property", function($scope,$state) {
	 $scope.trOnHover=function(event){
		 const propertyIcons=event.target.querySelector(".propertyicons");
		 if(propertyIcons!=null){
			 propertyIcons.style.display = "block";
		 }
	 }
	 $scope.trOffHover=function(event){
		 const propertyIcons=event.target.querySelector(".propertyicons");
		 if(propertyIcons!=null){
			 propertyIcons.style.display = "none";
		 }
	 }
	 $scope.hideAddProperty=true;
	 $scope.addProperty=function(){
		 $state.go('properties.add');
	 }
});
app.controller("addproperty", function($scope) {
	$scope.hideAddProperty=false;
	//$scope.propertytemplate=$scope.CONSTANTS.HOUSETYPE.HOUSE;
	$scope.propertytemplate="APPARTMENT";
	$scope.typeRadioChange = function (event) {
        if(event.target.value===$scope.CONSTANTS.HOUSETYPE.APPARTMENT){
        	$scope.propertytemplate=$scope.CONSTANTS.HOUSETYPE.APPARTMENT;
        }else{
        	$scope.propertytemplate=$scope.CONSTANTS.HOUSETYPE.HOUSE;
        }
    };
});