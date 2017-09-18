var canvas;

var camera, scene, renderer;
var num = 0;
var mesh;
var objects = [];
var initialized = 0;
var xwidth, xheight;


var gl; 
var buffers;
var programInfo;
var cubeRotation = 0.0;

var then = 0;
var deltaTime;

function WebGLJS(e) {

	xwidth= e.style.width.replace("px", "");
	xheight= e.style.height.replace("px", "");

	this.div = document.createElement("canvas");
	this.div.className = 'webgljs';

	canvas = this.div;

	initialized = 0;

	if (e.firstChild != null) {

		e.removeChild(e.firstChild);
	}
	e.appendChild(canvas);



	gl = canvas.getContext('webgl');

	
	// Only continue if WebGL is available and working
	if (!gl) {
		alert("Unable to initialize WebGL. Your browser or machine may not support it.");
		return;
	}


	// Vertex shader program

	var vsSource = `
		attribute vec4 aVertexPosition;
		attribute vec3 aVertexNormal;
		attribute vec2 aTextureCoord;

		uniform mat4 uNormalMatrix;
		uniform mat4 uModelViewMatrix;
		uniform mat4 uProjectionMatrix;

		varying highp vec2 vTextureCoord;
		varying highp vec3 vLighting;

		void main(void) {
		gl_Position = uProjectionMatrix * uModelViewMatrix * aVertexPosition;
		vTextureCoord = aTextureCoord;

		// Apply lighting effect

		highp vec3 ambientLight = vec3(0.3, 0.3, 0.3);
		highp vec3 directionalLightColor = vec3(1, 1, 1);
		highp vec3 directionalVector = normalize(vec3(0.85, 0.8, 0.75));

		highp vec4 transformedNormal = uNormalMatrix * vec4(aVertexNormal, 1.0);

		highp float directional = max(dot(transformedNormal.xyz, directionalVector), 0.0);
		vLighting = ambientLight + (directionalLightColor * directional);
		}
		`;

	// Fragment shader program

	var fsSource = `
		varying highp vec2 vTextureCoord;
		varying highp vec3 vLighting;

		uniform sampler2D uSampler;

		void main(void) {
		highp vec4 texelColor = texture2D(uSampler, vTextureCoord);

		gl_FragColor = vec4(texelColor.rgb * vLighting, texelColor.a);
		}
		`;

	// Initialize a shader program; this is where all the lighting
	// for the vertices and so forth is established.
	var shaderProgram = initShaderProgram(gl, vsSource, fsSource);

	// Collect all the info needed to use the shader program.
	// Look up which attributes our shader program is using
	// for aVertexPosition, aVertexNormal, aTextureCoord,
	// and look up uniform locations.
	programInfo = {
			program: shaderProgram,
			attribLocations: {
				vertexPosition: gl.getAttribLocation(shaderProgram, 'aVertexPosition'),
				vertexNormal: gl.getAttribLocation(shaderProgram, 'aVertexNormal'),
				textureCoord: gl.getAttribLocation(shaderProgram, 'aTextureCoord'),
			},
			uniformLocations: {
				projectionMatrix: gl.getUniformLocation(shaderProgram, 'uProjectionMatrix'),
				modelViewMatrix: gl.getUniformLocation(shaderProgram, 'uModelViewMatrix'),
				normalMatrix: gl.getUniformLocation(shaderProgram, 'uNormalMatrix'),
				uSampler: gl.getUniformLocation(shaderProgram, 'uSampler'),
			},
	};

	// Here's where we call the routine that builds all the
	// objects we'll be drawing.
	// buffers = initBuffers(gl);

	// Create a buffer for the cube's vertex positions.
	/*
	var positionBuffer = gl.createBuffer();

	// Select the positionBuffer as the one to apply buffer
	// operations to from here out.

	gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);

	// Now create an array of positions for the cube.

	var positions = [
		// Front face
		-1.0, -1.0,  1.0,
		1.0, -1.0,  1.0,
		1.0,  1.0,  1.0,
		-1.0,  1.0,  1.0,

		// Back face
		-1.0, -1.0, -1.0,
		-1.0,  1.0, -1.0,
		1.0,  1.0, -1.0,
		1.0, -1.0, -1.0,

		// Top face
		-1.0,  1.0, -1.0,
		-1.0,  1.0,  1.0,
		1.0,  1.0,  1.0,
		1.0,  1.0, -1.0,

		// Bottom face
		-1.0, -1.0, -1.0,
		1.0, -1.0, -1.0,
		1.0, -1.0,  1.0,
		-1.0, -1.0,  1.0,

		// Right face
		1.0, -1.0, -1.0,
		1.0,  1.0, -1.0,
		1.0,  1.0,  1.0,
		1.0, -1.0,  1.0,

		// Left face
		-1.0, -1.0, -1.0,
		-1.0, -1.0,  1.0,
		-1.0,  1.0,  1.0,
		-1.0,  1.0, -1.0,
		];

	// Now pass the list of positions into WebGL to build the
	// shape. We do this by creating a Float32Array from the
	// JavaScript array, then use it to fill the current buffer.

	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(positions), gl.STATIC_DRAW);

	// Set up the normals for the vertices, so that we can compute lighting.

	var normalBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, normalBuffer);

	var vertexNormals = [
		// Front
		0.0,  0.0,  1.0,
		0.0,  0.0,  1.0,
		0.0,  0.0,  1.0,
		0.0,  0.0,  1.0,

		// Back
		0.0,  0.0, -1.0,
		0.0,  0.0, -1.0,
		0.0,  0.0, -1.0,
		0.0,  0.0, -1.0,

		// Top
		0.0,  1.0,  0.0,
		0.0,  1.0,  0.0,
		0.0,  1.0,  0.0,
		0.0,  1.0,  0.0,

		// Bottom
		0.0, -1.0,  0.0,
		0.0, -1.0,  0.0,
		0.0, -1.0,  0.0,
		0.0, -1.0,  0.0,

		// Right
		1.0,  0.0,  0.0,
		1.0,  0.0,  0.0,
		1.0,  0.0,  0.0,
		1.0,  0.0,  0.0,

		// Left
		-1.0,  0.0,  0.0,
		-1.0,  0.0,  0.0,
		-1.0,  0.0,  0.0,
		-1.0,  0.0,  0.0
		];

	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexNormals),
			gl.STATIC_DRAW);

	// Now set up the texture coordinates for the faces.

	var textureCoordBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, textureCoordBuffer);

	var textureCoordinates = [
		// Front
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		// Back
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		// Top
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		// Bottom
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		// Right
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		// Left
		0.0,  0.0,
		1.0,  0.0,
		1.0,  1.0,
		0.0,  1.0,
		];

	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates),
			gl.STATIC_DRAW);

	// Build the element array buffer; this specifies the indices
	// into the vertex arrays for each face's vertices.

	var indexBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indexBuffer);

	// This array defines each face as two triangles, using the
	// indices into the vertex array to specify each triangle's
	// position.

	var indices = [
		0,  1,  2,      0,  2,  3,    // front
		4,  5,  6,      4,  6,  7,    // back
		8,  9,  10,     8,  10, 11,   // top
		12, 13, 14,     12, 14, 15,   // bottom
		16, 17, 18,     16, 18, 19,   // right
		20, 21, 22,     20, 22, 23,   // left
		];

	// Now send the element array to GL

	gl.bufferData(gl.ELEMENT_ARRAY_BUFFER,
			new Uint16Array(indices), gl.STATIC_DRAW);

	buffers= {
			position: positionBuffer,
			normal: normalBuffer,
			textureCoord: textureCoordBuffer,
			indices: indexBuffer,
	};
	 */


	/*
	  gl.clearColor(1.0, 1.0, 1.0, 1.0);  // Clear to black, fully opaque
	  gl.clearDepth(1.0);                 // Clear everything
	  gl.enable(gl.DEPTH_TEST);           // Enable depth testing
	  gl.depthFunc(gl.LEQUAL);            // Near things obscure far things

	  // Clear the canvas before we start drawing on it.

	  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

	var fieldOfView = 45 * Math.PI / 180;   // in radians
	var aspect = gl.canvas.clientWidth / gl.canvas.clientHeight;
	var zNear = 0.1;
	var zFar = 100.0;
	var projectionMatrix = mat4.create();

	// note: glmatrix.js always has the first argument
	// as the destination to receive the result.
	mat4.perspective(projectionMatrix,
			fieldOfView,
			aspect,
			zNear,
			zFar);


	// Set the drawing position to the "identity" point, which is
	// the center of the scene.
	var modelViewMatrix = mat4.create();

	// Now move the drawing position a bit to where we want to
	// start drawing the square.

	mat4.translate(modelViewMatrix,     // destination matrix
			modelViewMatrix,     // matrix to translate
			[-0.0, 0.0, -6.0]);  // amount to translate
	mat4.rotate(modelViewMatrix,  // destination matrix
			modelViewMatrix,  // matrix to rotate
			cubeRotation,     // amount to rotate in radians
			[0, 0, 1]);       // axis to rotate around (Z)
	mat4.rotate(modelViewMatrix,  // destination matrix
			modelViewMatrix,  // matrix to rotate
			cubeRotation * .7,// amount to rotate in radians
			[0, 1, 0]);       // axis to rotate around (X)

	var normalMatrix = mat4.create();
	mat4.invert(normalMatrix, modelViewMatrix);
	mat4.transpose(normalMatrix, normalMatrix);

	// Tell WebGL how to pull out the positions from the position
	// buffer into the vertexPosition attribute
	{
		var numComponents = 3;
		var type = gl.FLOAT;
		var normalize = false;
		var stride = 0;
		var offset = 0;
		gl.bindBuffer(gl.ARRAY_BUFFER, buffers.position);
		gl.vertexAttribPointer(
				programInfo.attribLocations.vertexPosition,
				numComponents,
				type,
				normalize,
				stride,
				offset);
		gl.enableVertexAttribArray(
				programInfo.attribLocations.vertexPosition);
	}

	// Tell WebGL how to pull out the texture coordinates from
	// the texture coordinate buffer into the textureCoord attribute.
	{
		var numComponents = 2;
		var type = gl.FLOAT;
		var normalize = false;
		var stride = 0;
		var offset = 0;
		gl.bindBuffer(gl.ARRAY_BUFFER, buffers.textureCoord);
		gl.vertexAttribPointer(
				programInfo.attribLocations.textureCoord,
				numComponents,
				type,
				normalize,
				stride,
				offset);
		gl.enableVertexAttribArray(
				programInfo.attribLocations.textureCoord);
	}

	// Tell WebGL how to pull out the normals from
	// the normal buffer into the vertexNormal attribute.
	{
		var numComponents = 3;
		var type = gl.FLOAT;
		var normalize = false;
		var stride = 0;
		var offset = 0;
		gl.bindBuffer(gl.ARRAY_BUFFER, buffers.normal);
		gl.vertexAttribPointer(
				programInfo.attribLocations.vertexNormal,
				numComponents,
				type,
				normalize,
				stride,
				offset);
		gl.enableVertexAttribArray(
				programInfo.attribLocations.vertexNormal);
	}
	 */

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 


	 
	 
	this.appendInfo = function(text) {

         // Link both programs
         gl.linkProgram(shaderProgram);

         // Use the combined shader program object
         gl.useProgram(shaderProgram);

		gl.clearColor(1.0, 1.0, 1.0, 1.0);
		gl.enable(gl.DEPTH_TEST);


		gl.useProgram(shaderProgram);
		triangleVertexPositionBuffer = gl.createBuffer();
		gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
		let vertices = [
			0.0,  1.0,  0.0,
			-1.0, -1.0,  0.0,
			1.0, -1.0,  0.0
			];
		gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
		triangleVertexPositionBuffer.itemSize = 3;
		triangleVertexPositionBuffer.numItems = 3;


		
		gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
		gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, triangleVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);


		
		gl.drawArrays(gl.TRIANGLES, 0, triangleVertexPositionBuffer.numItems);

		console.log("appendinfo "+cubeRotation);
	};

	this.glDrawElements = function(mode, count, type, offset) {
		var type = gl.UNSIGNED_SHORT;
		/*		 
		 var vertexCount = 36;
		var type = gl.UNSIGNED_SHORT;
		var offset = 0;
		gl.drawElements(gl.TRIANGLES, vertexCount, type, offset);
		 */


		/*
		gl.useProgram(programInfo.program);

		// Set the shader uniforms

		gl.uniformMatrix4fv(
				programInfo.uniformLocations.projectionMatrix,
				false,
				projectionMatrix);
		gl.uniformMatrix4fv(
				programInfo.uniformLocations.modelViewMatrix,
				false,
				modelViewMatrix);
		gl.uniformMatrix4fv(
				programInfo.uniformLocations.normalMatrix,
				false,
				normalMatrix);


		gl.uniform1i(programInfo.uniformLocations.uSampler, 0);

		{
			gl.drawElements(mode, count, type, offset);

		}
		 */
		gl.bindBuffer(gl.ARRAY_BUFFER, meshVertexPositionBuffer); 
		gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, 
				meshVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0); 
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, meshIndexBuffer);
		gl.drawElements(gl.TRIANGLES, meshIndexBuffer.numberOfItems, gl.UNSIGNED_SHORT, 0);

	};


	this.glBindBuffer = function(target, buffer) {


		/*

		var buffer = gl.createBuffer();
		gl.bindBuffer(target, buffer);
		 */
	};

	this.glBufferData = function(target, srcData, usage) {
		/*
		gl.bufferData(target, new Float32Array(JSON.parse(srcData)),usage);
		 */
	};












	this.glFrontFace = function(text) {
		gl.frontFace(gl.CW);
		console.log('glFrontFace');
	};








	this.appendErr = function(text) {

	};

	this.appendWarn = function(text) {

	};

	this.clearAll = function() {

	};

}







function loadTexture(gl, url) {
	var texture = gl.createTexture();
	gl.bindTexture(gl.TEXTURE_2D, texture);

	// Because images have to be download over the internet
	// they might take a moment until they are ready.
	// Until then put a single pixel in the texture so we can
	// use it immediately. When the image has finished downloading
	// we'll update the texture with the contents of the image.
	var level = 0;
	var internalFormat = gl.RGBA;
	var width = 1;
	var height = 1;
	var border = 0;
	var srcFormat = gl.RGBA;
	var srcType = gl.UNSIGNED_BYTE;
	var pixel = new Uint8Array([0, 0, 255, 255]);  // opaque blue
	gl.texImage2D(gl.TEXTURE_2D, level, internalFormat,
			width, height, border, srcFormat, srcType,
			pixel);

	var image = new Image();
	image.onload = function() {
		gl.bindTexture(gl.TEXTURE_2D, texture);
		gl.texImage2D(gl.TEXTURE_2D, level, internalFormat,
				srcFormat, srcType, image);

		// WebGL1 has different requirements for power of 2 images
		// vs non power of 2 images so check if the image is a
		// power of 2 in both dimensions.
		if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
			// Yes, it's a power of 2. Generate mips.
			gl.generateMipmap(gl.TEXTURE_2D);
		} else {
			// No, it's not a power of 2. Turn of mips and set
			// wrapping to clamp to edge
			gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
			gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
			gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
		}
	};
	image.src = url;

	return texture;
}

function isPowerOf2(value) {
	return (value & (value - 1)) == 0;
}




//Initialize a shader program, so WebGL knows how to draw our data

function initShaderProgram(gl, vsSource, fsSource) {
	var vertexShader = loadShader(gl, gl.VERTEX_SHADER, vsSource);
	var fragmentShader = loadShader(gl, gl.FRAGMENT_SHADER, fsSource);

	// Create the shader program

	var shaderProgram = gl.createProgram();
	gl.attachShader(shaderProgram, vertexShader);
	gl.attachShader(shaderProgram, fragmentShader);
	gl.linkProgram(shaderProgram);

	// If creating the shader program failed, alert

	if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
		alert('Unable to initialize the shader program: ' + gl.getProgramInfoLog(shaderProgram));
		return null;
	}

	return shaderProgram;
}


//creates a shader of the given type, uploads the source and
//compiles it.

function loadShader(gl, type, source) {
	var shader = gl.createShader(type);

	// Send the source to the shader object

	gl.shaderSource(shader, source);

	// Compile the shader program

	gl.compileShader(shader);

	// See if it compiled successfully

	if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
		alert('An error occurred compiling the shaders: ' + gl.getShaderInfoLog(shader));
		gl.deleteShader(shader);
		return null;
	}

	return shader;
}

