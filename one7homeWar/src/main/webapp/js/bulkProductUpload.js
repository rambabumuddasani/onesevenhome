app.controller("demoController", function ($scope, $http) {
	//1. Used to list all selected files
	$scope.files = [];
	//2. a simple model that want to pass to Web API along with selected files
	/*$scope.jsonData = {
        email: "email@gmail.com",
        password: "secret",
        confirmPassword: "secret",
        vendorName: "vendorName1",				
        vendorMobile: "98989888",
        termsAndConditions: true,
        activationURL: "someURL"
    };*/
	//3. listen for the file selected event which is raised from directive
	$scope.$on("seletedFile", function (event, args) {
		$scope.$apply(function () {
			$scope.files.push(args.file);
		});
	});

	//4. Post data and selected files.
	/**
	 * I assume, we always get only two MultipartFile
	 * 1st object always represent user profile picture
	 * 2nd object always represent vendor certificate.
	 * 
	 */

	$scope.save = function () {
		newURL = location.origin;
		$http({
			method: 'POST',
			//url: newURL+"/vendor/register",
			url: newURL+"/shop/bulkProductInsertion",
			headers: { 'Content-Type': undefined },

			transformRequest: function (data) {
				var formData = new FormData();
				for (var i = 0; i < data.file.length; i++) {
					formData.append("file", data.file[i]);
				}
				return formData;
			//}
			},
		data: {file: $scope.files }
	}).
	success(function (data, status, headers, config) {
		if (status === 200) {
			$scope.responseData = data;
			console.log(data);
			//alert("success!");
		} else {
			alert("success with some error !");
			console.log("error")
		}
	}).
	error(function (data, status, headers, config) {
		alert("failed!");
	});
};
});