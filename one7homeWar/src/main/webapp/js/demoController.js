app.controller("demoController", function ($scope, $http) {
    //1. Used to list all selected files
    $scope.files = [];
    $scope.fileIDs = [];
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
            $scope.fileIDs.push(args.event.currentTarget.id);
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
            url: newURL+"/vendor/update",
            headers: { 'Content-Type': undefined },
           
            transformRequest: function (data) {
            	var fileIDs = data.fileIDs;
                var formData = new FormData();
                formData.append("vendorRequest", JSON.stringify(data.fileInfo));
                // if file length is 0, set empty file 
                if(data.file.length == 0){
                	formData.append("file",new File([""], "emptyProfileFile.jpg", {type: "image/jpeg"}));
                	formData.append("file",new File([""], "emptycertificateFile.jpg", {type: "image/jpeg"}));
                }else if(data.file.length == 1){
                	if(fileIDs[0] == 'profile'){
                		formData.append("file", data.file[0]);
                    	formData.append("file",new File([""], "emptycertificateFile.jpg", {type: "image/jpeg"}));
                	}else{
                    	formData.append("file",new File([""], "emptyProfileFile.jpg", {type: "image/jpeg"}));
                		formData.append("file", data.file[0]);
                	}
                }else {
                	for (var i = 0; i < data.file.length; i++) {
                		// formData.append("file", data.file);
                		formData.append("file", data.file[i]);
                	}
                }
                return formData;
            },
            data: { fileInfo: $scope.jsonData, file: $scope.files, fileIDs: $scope.fileIDs }
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