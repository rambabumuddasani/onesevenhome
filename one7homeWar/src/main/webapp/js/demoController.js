app.controller("demoController", function ($scope, $http) {
    //1. Used to list all selected files
    $scope.files = [];

    //2. a simple model that want to pass to Web API along with selected files
    $scope.jsonData = {
        email: "email@gmail.com",
        password: "secret",
        confirmPassword: "secret",
        vendorName: "vendorName1",				
        vendorMobile: "98989888",
        termsAndConditions: true,
        activationURL: "someURL"
    };
    //3. listen for the file selected event which is raised from directive
    $scope.$on("seletedFile", function (event, args) {
        $scope.$apply(function () {
            //add the file object to the scope's files collection
            $scope.files.push(args.file);
        });
    });

    //4. Post data and selected files.
    $scope.save = function () {
    	newURL = location.origin;
        $http({
            method: 'POST',
            //url: newURL+"/vendor/register",
            url: newURL+"/vendor/update",
            headers: { 'Content-Type': undefined },
           
            transformRequest: function (data) {
                var formData = new FormData();
                formData.append("vendorRequest", JSON.stringify(data.fileInfo));
                // if file length is 0, set empty file 
                if(data.file.length == 0){
                	formData.append("file",new File([""], "emptyFile.jpg", {type: "impage/jpeg"}));
                }else {
                	for (var i = 0; i < data.file.length; i++) {
                		// formData.append("file", data.file);
                		formData.append("file", data.file[i]);
                	}
                }
                return formData;
            },
            data: { fileInfo: $scope.jsonData, file: $scope.files }
        }).
        success(function (data, status, headers, config) {
			if (status === 200) {
            alert("success!");
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