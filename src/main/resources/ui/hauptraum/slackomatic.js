function device(device, command) {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/slackomatic/devices/' + device + '/' + command, true);
	xhr.send(null);
};

function infocus(command) {
	device('infocus', command);
};

function nec(command) {
	device('nec', command);
};

function hauptraum(command) {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/slackomatic/rooms/hauptraum/' + command, true);
	xhr.send(null);
};

function slackomatic(command) {
	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/slackomatic/functions/' + command, true);
	xhr.send(null);
};