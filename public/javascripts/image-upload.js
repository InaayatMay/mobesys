let base64String = "";

function imageUploaded() {
	var file = document.querySelector(
		'input[type=file]')['files'][0];

	var reader = new FileReader();
	console.log("next");
	
	reader.onload = function () {
		base64String = reader.result.replace("data:", "")
			.replace(/^.+,/, "");

		imageBase64Stringsep = base64String;

		// alert(imageBase64Stringsep);
		console.log(base64String);
		base64String = 'data:image/jpeg;base64,' + base64String;
		document.getElementById("profile-image").src = base64String;
		document.getElementById("imageString").value = base64String;
	}
	reader.readAsDataURL(file);
}
