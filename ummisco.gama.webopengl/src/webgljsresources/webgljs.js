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

var bufferArray;
var frameBufferArray;
var depthBufferArray;
var depthBufferTextureArray;
var textureArray;
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
	console.log("canvas.getContext "+gl); 
	// Only continue if WebGL is available and working
	if (!gl) {
		alert("Unable to initialize WebGL. Your browser or machine may not support it.");
		return;
	}


	// Vertex shader program

//	var vsSource = `
//		attribute vec4 aVertexPosition;
//		attribute vec3 aVertexNormal;
//		attribute vec2 aTextureCoord;
//
//		uniform mat4 uNormalMatrix;
//		uniform mat4 uModelViewMatrix;
//		uniform mat4 uProjectionMatrix;
//
//		varying highp vec2 vTextureCoord;
//		varying highp vec3 vLighting;
//
//		void main(void) {
//		gl_Position = uProjectionMatrix * uModelViewMatrix * aVertexPosition;
//		vTextureCoord = aTextureCoord;
//
//		// Apply lighting effect
//
//		highp vec3 ambientLight = vec3(0.3, 0.3, 0.3);
//		highp vec3 directionalLightColor = vec3(1, 1, 1);
//		highp vec3 directionalVector = normalize(vec3(0.85, 0.8, 0.75));
//
//		highp vec4 transformedNormal = uNormalMatrix * vec4(aVertexNormal, 1.0);
//
//		highp float directional = max(dot(transformedNormal.xyz, directionalVector), 0.0);
//		vLighting = ambientLight + (directionalLightColor * directional);
//		}
//		`;
//
//	// Fragment shader program
//
//	var fsSource = `
//		varying highp vec2 vTextureCoord;
//		varying highp vec3 vLighting;
//
//		uniform sampler2D uSampler;
//
//		void main(void) {
//		highp vec4 texelColor = texture2D(uSampler, vTextureCoord);
//
//		gl_FragColor = vec4(texelColor.rgb * vLighting, texelColor.a);
//		}
//		`;
//
//	// Initialize a shader program; this is where all the lighting
//	// for the vertices and so forth is established.
//	var shaderProgram = initShaderProgram(gl, vsSource, fsSource);
//
//	// Collect all the info needed to use the shader program.
//	// Look up which attributes our shader program is using
//	// for aVertexPosition, aVertexNormal, aTextureCoord,
//	// and look up uniform locations.
//	programInfo = {
//			program: shaderProgram,
//			attribLocations: {
//				vertexPosition: gl.getAttribLocation(shaderProgram, 'aVertexPosition'),
//				vertexNormal: gl.getAttribLocation(shaderProgram, 'aVertexNormal'),
//				textureCoord: gl.getAttribLocation(shaderProgram, 'aTextureCoord'),
//			},
//			uniformLocations: {
//				projectionMatrix: gl.getUniformLocation(shaderProgram, 'uProjectionMatrix'),
//				modelViewMatrix: gl.getUniformLocation(shaderProgram, 'uModelViewMatrix'),
//				normalMatrix: gl.getUniformLocation(shaderProgram, 'uNormalMatrix'),
//				uSampler: gl.getUniformLocation(shaderProgram, 'uSampler'),
//			},
//	};
//
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//	 
//
//
//         gl.linkProgram(shaderProgram);
//
//         // Use the combined shader program object
//         gl.useProgram(shaderProgram);

	  /* Step3: Create and compile Shader programs */
      // Vertex shader source code
      var vertCode =
         'attribute vec2 coordinates;' + 
         'void main(void) {' + ' gl_Position = vec4(coordinates,0.0, 1.0);' + '}';
      // Create a vertex shader object
      var vertShader = gl.createShader(gl.VERTEX_SHADER);
      // Attach vertex shader source code
      gl.shaderSource(vertShader, vertCode);
      // Compile the vertex shader
      gl.compileShader(vertShader);
      // Fragment shader source code
      var fragCode = 'void main(void) {' + 'gl_FragColor = vec4(0.0, 0.0, 0.0, 0.1);' + '}';
      // Create fragment shader object
      var fragShader = gl.createShader(gl.FRAGMENT_SHADER);
      // Attach fragment shader source code
      gl.shaderSource(fragShader, fragCode);
      // Compile the fragment shader
      gl.compileShader(fragShader);
      // Create a shader program object to store combined shader program
      var shaderProgram = gl.createProgram();
      // Attach a vertex shader
      gl.attachShader(shaderProgram, vertShader);         
      // Attach a fragment shader
      gl.attachShader(shaderProgram, fragShader);
      // Link both programs
      gl.linkProgram(shaderProgram);
      // Use the combined shader program object
      gl.useProgram(shaderProgram); 
	 
	this.appendInfo = function(text) {

        /* Step2: Define the geometry and store it in buffer objects */

        var vertices = [-0.5, 0.5, -0.5, -0.5, 0.0, -0.5,  -0.2, 0.2, -0.2, -0.2, 0.0, -0.2];

        // Create a new buffer object
        var vertex_buffer = gl.createBuffer();

        // Bind an empty array buffer to it
        gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);
        
        // Pass the vertices data to the buffer
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        // Unbind the buffer
        gl.bindBuffer(gl.ARRAY_BUFFER, null);


        
        
        /* Step3: Create and compile Shader programs */

        // Vertex shader source code
        var vertCode =
           'attribute vec2 coordinates;' + 
           'void main(void) {' + ' gl_Position = vec4(coordinates,0.0, 1.0);' + '}';

        // Create a vertex shader object
        var vertShader = gl.createShader(gl.VERTEX_SHADER);

        // Attach vertex shader source code
        gl.shaderSource(vertShader, vertCode);

        // Compile the vertex shader
        gl.compileShader(vertShader);

        // Fragment shader source code
        var fragCode = 'void main(void) {' + 'gl_FragColor = vec4(0.0, 0.0, 0.0, 0.1);' + '}';

        // Create fragment shader object
        var fragShader = gl.createShader(gl.FRAGMENT_SHADER);

        // Attach fragment shader source code
        gl.shaderSource(fragShader, fragCode);

        // Compile the fragment shader
        gl.compileShader(fragShader);

        // Create a shader program object to store combined shader program
        var shaderProgram = gl.createProgram();

        // Attach a vertex shader
        gl.attachShader(shaderProgram, vertShader); 
        
        // Attach a fragment shader
        gl.attachShader(shaderProgram, fragShader);

        // Link both programs
        gl.linkProgram(shaderProgram);

        // Use the combined shader program object
        gl.useProgram(shaderProgram);

        
        
        

        /* Step 4: Associate the shader programs to buffer objects */

        // Bind vertex buffer object
        gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);

        // Get the attribute location
        var coord = gl.getAttribLocation(shaderProgram, "coordinates");

        // point an attribute to the currently bound VBO
        gl.vertexAttribPointer(coord, 2, gl.FLOAT, false, 0, 0);

        // Enable the attribute
        gl.enableVertexAttribArray(coord);


        /* Step5: Drawing the required object (triangle) */

        // Clear the canvas
        gl.clearColor(0.5, 0.5, 0.5, 0.9);

        // Enable the depth test
        gl.enable(gl.DEPTH_TEST); 
        
        // Clear the color buffer bit
        gl.clear(gl.COLOR_BUFFER_BIT);

        // Set the view port
        gl.viewport(0,0,canvas.width,canvas.height);

        // Draw the triangle
        gl.drawArrays(gl.TRIANGLES, 0, 6);
		console.log("appendinfo ");
	};


	this.appendErr = function(text) {
		 var vertices = [
	            -1,-1,-1, 1,-1,-1, 1, 1,-1, -1, 1,-1,
	            -1,-1, 1, 1,-1, 1, 1, 1, 1, -1, 1, 1,
	            -1,-1,-1, -1, 1,-1, -1, 1, 1, -1,-1, 1,
	            1,-1,-1, 1, 1,-1, 1, 1, 1, 1,-1, 1,
	            -1,-1,-1, -1,-1, 1, 1,-1, 1, 1,-1,-1,
	            -1, 1,-1, -1, 1, 1, 1, 1, 1, 1, 1,-1, 
	         ];

	         var colors = [
	            5,3,7, 5,3,7, 5,3,7, 5,3,7,
	            1,1,3, 1,1,3, 1,1,3, 1,1,3,
	            0,0,1, 0,0,1, 0,0,1, 0,0,1,
	            1,0,0, 1,0,0, 1,0,0, 1,0,0,
	            1,1,0, 1,1,0, 1,1,0, 1,1,0,
	            0,1,0, 0,1,0, 0,1,0, 0,1,0 
	         ];

	         var indices = [
	            0,1,2, 0,2,3, 4,5,6, 4,6,7,
	            8,9,10, 8,10,11, 12,13,14, 12,14,15,
	            16,17,18, 16,18,19, 20,21,22, 20,22,23 
	         ];

	         // Create and store data into vertex buffer
	         var vertex_buffer = gl.createBuffer ();
	         gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);
	         gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

	         // Create and store data into color buffer
	         var color_buffer = gl.createBuffer ();
	         gl.bindBuffer(gl.ARRAY_BUFFER, color_buffer);
	         gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colors), gl.STATIC_DRAW);

	         // Create and store data into index buffer
	         var index_buffer = gl.createBuffer ();
	         gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index_buffer);
	         gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);
	                                              
	         /*=================== SHADERS =================== */

	         var vertCode = 'attribute vec3 position;'+
	            'uniform mat4 Pmatrix;'+
	            'uniform mat4 Vmatrix;'+
	            'uniform mat4 Mmatrix;'+
	            'attribute vec3 color;'+//the color of the point
	            'varying vec3 vColor;'+
	            'void main(void) { '+//pre-built function
	               'gl_Position = Pmatrix*Vmatrix*Mmatrix*vec4(position, 1.);'+
	               'vColor = color;'+
	            '}';

	         var fragCode = 'precision mediump float;'+
	            'varying vec3 vColor;'+
	            'void main(void) {'+
	               'gl_FragColor = vec4(vColor, 1.);'+
	            '}';

	         var vertShader = gl.createShader(gl.VERTEX_SHADER);
	         gl.shaderSource(vertShader, vertCode);
	         gl.compileShader(vertShader);

	         var fragShader = gl.createShader(gl.FRAGMENT_SHADER);
	         gl.shaderSource(fragShader, fragCode);
	         gl.compileShader(fragShader);
				
	         var shaderprogram = gl.createProgram();
	         gl.attachShader(shaderprogram, vertShader);
	         gl.attachShader(shaderprogram, fragShader);
	         gl.linkProgram(shaderprogram);
	         
	         /*======== Associating attributes to vertex shader =====*/
	         var _Pmatrix = gl.getUniformLocation(shaderprogram, "Pmatrix");
	         var _Vmatrix = gl.getUniformLocation(shaderprogram, "Vmatrix");
	         var _Mmatrix = gl.getUniformLocation(shaderprogram, "Mmatrix");

	         gl.bindBuffer(gl.ARRAY_BUFFER, vertex_buffer);
	         var _position = gl.getAttribLocation(shaderprogram, "position");
	         gl.vertexAttribPointer(_position, 3, gl.FLOAT, false,0,0);
	         gl.enableVertexAttribArray(_position);

	         gl.bindBuffer(gl.ARRAY_BUFFER, color_buffer);
	         var _color = gl.getAttribLocation(shaderprogram, "color");
	         gl.vertexAttribPointer(_color, 3, gl.FLOAT, false,0,0) ;
	         gl.enableVertexAttribArray(_color);
	         gl.useProgram(shaderprogram);

	         /*==================== MATRIX ====================== */
	         
	         function get_projection(angle, a, zMin, zMax) {
	            var ang = Math.tan((angle*.5)*Math.PI/180);//angle*.5
	            return [
	               0.5/ang, 0 , 0, 0,
	               0, 0.5*a/ang, 0, 0,
	               0, 0, -(zMax+zMin)/(zMax-zMin), -1,
	               0, 0, (-2*zMax*zMin)/(zMax-zMin), 0 
				   ];
	         }
	         
	         var proj_matrix = get_projection(40, canvas.width/canvas.height, 1, 100);
	         var mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1 ];
	         var view_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1 ];

	         view_matrix[14] = view_matrix[14]-6;

	         /*================= Mouse events ======================*/

	         var AMORTIZATION = 0.95;
	         var drag = false;
	         var old_x, old_y;
	         var dX = 0, dY = 0;
				
	         var mouseDown = function(e) {
	            drag = true;
	            old_x = e.pageX, old_y = e.pageY;
	            e.preventDefault();
	            return false;
	         };
	         
	         var mouseUp = function(e){
	            drag = false;
	         };
	         
	         var mouseMove = function(e) {
	            if (!drag) return false;
	            dX = (e.pageX-old_x)*2*Math.PI/canvas.width,
	            dY = (e.pageY-old_y)*2*Math.PI/canvas.height;
	            THETA+= dX;
	            PHI+=dY;
	            old_x = e.pageX, old_y = e.pageY;
	            e.preventDefault();
	         };
	         
	         canvas.addEventListener("mousedown", mouseDown, false);
	         canvas.addEventListener("mouseup", mouseUp, false);
	         canvas.addEventListener("mouseout", mouseUp, false);
	         canvas.addEventListener("mousemove", mouseMove, false);

	         /*=========================rotation================*/

	         function rotateX(m, angle) {
	            var c = Math.cos(angle);
	            var s = Math.sin(angle);
	            var mv1 = m[1], mv5 = m[5], mv9 = m[9];
					
	            m[1] = m[1]*c-m[2]*s;
	            m[5] = m[5]*c-m[6]*s;
	            m[9] = m[9]*c-m[10]*s;

	            m[2] = m[2]*c+mv1*s;
	            m[6] = m[6]*c+mv5*s;
	            m[10] = m[10]*c+mv9*s;
	         }

	         function rotateY(m, angle) {
	            var c = Math.cos(angle);
	            var s = Math.sin(angle);
	            var mv0 = m[0], mv4 = m[4], mv8 = m[8];
					
	            m[0] = c*m[0]+s*m[2];
	            m[4] = c*m[4]+s*m[6];
	            m[8] = c*m[8]+s*m[10];

	            m[2] = c*m[2]-s*mv0;
	            m[6] = c*m[6]-s*mv4;
	            m[10] = c*m[10]-s*mv8;
	         }

	         /*=================== Drawing =================== */

	         var THETA = 0,
	         PHI = 0;
	         var time_old = 0;
					
	         var animate = function(time) {
	            var dt = time-time_old;
						
	            if (!drag) {
	               dX *= AMORTIZATION, dY*=AMORTIZATION;
	               THETA+=dX, PHI+=dY;
	            }
	               
	            //set model matrix to I4
						
	            mo_matrix[0] = 1, mo_matrix[1] = 0, mo_matrix[2] = 0,
	            mo_matrix[3] = 0,
						
	            mo_matrix[4] = 0, mo_matrix[5] = 1, mo_matrix[6] = 0,
	            mo_matrix[7] = 0,
						
	            mo_matrix[8] = 0, mo_matrix[9] = 0, mo_matrix[10] = 1,
	            mo_matrix[11] = 0,
						
	            mo_matrix[12] = 0, mo_matrix[13] = 0, mo_matrix[14] = 0,
	            mo_matrix[15] = 1;

	            rotateY(mo_matrix, THETA);
	            rotateX(mo_matrix, PHI);
						
	            time_old = time; 
	            gl.enable(gl.DEPTH_TEST);
						
	            // gl.depthFunc(gl.LEQUAL);
						
	            gl.clearColor(0.5, 0.5, 0.5, 0.9);
	            gl.clearDepth(1.0);
	            gl.viewport(0.0, 0.0, canvas.width, canvas.height);
	            gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);

	            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index_buffer);
	            gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
						
	            window.requestAnimationFrame(animate);
	         }
				
	         animate(0);
		console.log("appendErr ");
	};
	
	
	this.appendWarn = function(text) {

// frameBufferArray= gl.createFramebuffer();
//
// gl.bindFramebuffer ( gl.FRAMEBUFFER,frameBufferArray);
//
// depthBufferArray = gl.createRenderbuffer();
//		 
// gl.bindRenderbuffer (gl.RENDERBUFFER, depthBufferArray);
//
// gl.renderbufferStorage(gl.RENDERBUFFER, gl.RBGA4, 800, 600);
//
// gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0,
// gl.RENDERBUFFER, depthBufferArray);
//
// gl.bindFramebuffer ( gl.FRAMEBUFFER,frameBufferArray);
//
// gl.viewport (0,0,800, 600);
//
// gl.bindFramebuffer ( gl.FRAMEBUFFER,frameBufferArray);
//
// gl.viewport (0,0,800, 600);

// gl.clearColor(1,1,1,1);

// gl.clear(17664);

// gl.enable(gl.DEPTH_TEST);
// gl.enable(gl.DEPTH_TEST);
// gl.enable(gl.DEPTH_TEST);
		var vertices =  [-0.5, 0.5, -0.5, -0.5, 0.0, -0.5,  -0.2, 0.2, -0.2, -0.2, 0.0, -0.2, -0.5,-0.5, -0.25,0.5, 0.0,-0.5,]; // [0,3,1,3,1,2];

        gl.clearColor(0.5, 0.5, 0.5, 0.9);
        // Enable the depth test
        gl.enable(gl.DEPTH_TEST);         
        // Clear the color buffer bit
        gl.clear(gl.COLOR_BUFFER_BIT);
		 bufferArray= gl.createBuffer(); // = [36, 37, 38, 39, 40]

// gl.bindFramebuffer ( gl.FRAMEBUFFER,frameBufferArray);

// gl.viewport (0,0,800, 600);
		 gl.viewport(0,0,canvas.width,canvas.height);

// gl.bindBuffer (gl.ARRAY_BUFFER,bufferArray );
//
// gl.bindBuffer (gl.ARRAY_BUFFER,bufferArray );

		 gl.bindBuffer (gl.ARRAY_BUFFER,bufferArray );

// gl.bufferData(gl.ARRAY_BUFFER, 1024, gl.DYNAMIC_DRAW ); //= 34963 0,1,2,0,2,3
// 35044
		 gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

	        /* Step 4: Associate the shader programs to buffer objects */
	        // Bind vertex buffer object
	        gl.bindBuffer(gl.ARRAY_BUFFER, bufferArray);
	        // Get the attribute location
	        var coord = gl.getAttribLocation(shaderProgram, "coordinates");
	        // point an attribute to the currently bound VBO
	        gl.vertexAttribPointer(coord, 2, gl.FLOAT, false, 0, 0);
	        // Enable the attribute
	        gl.enableVertexAttribArray(coord);
// gl.drawElements(4,6, gl.UNSIGNED_SHORT, 0);

		 gl.drawArrays(gl.TRIANGLES, 0, 9);
		 console.log("appendWarn ");
	};

	this.glDrawElements = function(glTriangles, i, glUnsignedInt, j) {
		
//		gl.drawElements(glTriangles, i, gl.UNSIGNED_SHORT, j);

		 gl.drawArrays(glTriangles, 0, 3);
		console.log("gl.drawElements("+glTriangles+","+i+", gl.UNSIGNED_SHORT, "+j+")");
		 // gl.drawElements(gl.POINTS, 8, gl.UNSIGNED_BYTE, 0);

	};

	this.glBindFramebuffer = function(glFramebuffer, i) {
// gl.bindFramebuffer(glFramebuffer, i);
//		gl.bindFramebuffer(gl.FRAMEBUFFER, frameBufferArray); 
//		console.log("gl.bindFramebuffer ( gl.FRAMEBUFFER,frameBufferArray) = "+frameBufferArray+" "+i);
	};
	this.glViewport = function(i,j,width, height) {
		gl.viewport(i,j,80, 60);
		console.log("gl.viewport (i,j,800, 600) "+width);
	};


	this.glBindBuffer = function(glElementArrayBuffer, i) {
// gl.bindBuffer(glElementArrayBuffer, i);
		gl.bindBuffer(gl.ARRAY_BUFFER, bufferArray);
		console.log("gl.bindBuffer (gl.ARRAY_BUFFER,bufferArray ) = "+bufferArray+" "+i);
	};

	this.glBindRenderbuffer = function(glRenderbuffer, i) {
// gl.glBindRenderbuffer(depthBufferArray, i);
		gl.bindRenderbuffer(gl.RENDERBUFFER, depthBufferArray);
		console.log("gl.bindRenderbuffer (gl.RENDERBUFFER, depthBufferArray)="+depthBufferArray+" "+i);
	};

	this.glRenderbufferStorage = function(glRenderbuffer, glDepthComponent, width, height) {
// gl.renderbufferStorage(glRenderbuffer, glDepthComponent, width, height);//not use

//		gl.renderbufferStorage(gl.RENDERBUFFER, gl.RBGA4, 800, 600);
		console.log("gl.renderbufferStorage(gl.RENDERBUFFER, gl.RBGA4, 800, 600) = "+glRenderbuffer);
	};
	this.glFramebufferRenderbuffer = function(glFramebuffer, glDepthAttachment, glRenderbuffer, i) {
// gl.framebufferRenderbuffer(glFramebuffer, glDepthAttachment, glRenderbuffer,
// i);
//		gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.RENDERBUFFER, depthBufferArray);
//		console.log("gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.RENDERBUFFER, depthBufferArray); = "+depthBufferArray);
	};


	this.glGenFramebuffers = function(i,f,j) {

		frameBufferArray= gl.createFramebuffer();
		console.log("frameBufferArray= gl.createFramebuffer(); = "+frameBufferArray);
	};

	this.glGenBuffers = function(i,vboHandles,j) {

		bufferArray= gl.createBuffer();
		console.log("bufferArray= gl.createBuffer(); = "+vboHandles);
	};

	this.glBufferData = function(glElementArrayBuffer, numBytes, intIdxBuffer, glStaticDraw) {		
// gl.bufferData(target, new Float32Array(JSON.parse(srcData)),usage);
		gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
		console.log("gl.bufferData(gl.ARRAY_BUFFER, 1024, gl.STATIC_DRAW); = "+numBytes+" "+ new Float32Array(JSON.parse(intIdxBuffer)));
		 
	};

	this.glBufferSubData = function(glArrayBuffer, offset, i, fbData) {		
		console.log("gl.bufferSubData(gl.ARRAY_BUFFER, 512, fbData); = "+glArrayBuffer+" "+ new Float32Array(JSON.parse(fbData))+" "+offset);
		//gl.bufferSubData(gl.ARRAY_BUFFER, offset,  new Float32Array(JSON.parse(fbData)));
//		gl.bufferData(glArrayBuffer, offset, i, fbData);
		 
	};

	this.glDrawBuffer = function(glColorAttachment0) {		
// gl.drawBuffers(frameBufferArray);
		gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]);
		console.log("gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]); ");
		 
	};

	this.glClearColor = function(f,g,h,i) {
		
		gl.clearColor(f,g,h,i);
		console.log("gl.clearColor(f,g,h,i); = "+f+" "+g+" "+h+" "+i);
		 
	};



	this.glEnableVertexAttribArray = function(attributePosition) {

        var coord = gl.getAttribLocation(shaderProgram, "coordinates");
        // point an attribute to the currently bound VBO
        gl.vertexAttribPointer(coord, 2, gl.FLOAT, false, 0, 0);
        // Enable the attribute
        gl.enableVertexAttribArray(coord);
		console.log("glEnableVertexAttribArray(attributePosition); = "+attributePosition);
		 
	};


	this.glClear = function(i) {
		
		gl.clear(gl.COLOR_BUFFER_BIT);
		
		console.log("gl.clear(i); = "+i);
		 
	};



	this.glEnable = function(glDepthTest) {
		
		gl.enable(gl.DEPTH_TEST);
		console.log("gl.enable(gl.DEPTH_TEST); ");
		 
	};


	this.glGenRenderbuffers = function(i,dBufferArray,j) {
		
		depthBufferArray = gl.createRenderbuffer();
		console.log("depthBufferArray = gl.createRenderbuffer(); ");
		 
	};

	


	this.glDepthFunc = function(glLequal) {
		
		gl.depthFunc(glLequal);		 
		console.log("gl.depthFunc(glLequal); ");
	};




	this.glFrontFace = function(text) {
		gl.frontFace(gl.CW);
		console.log('gl.frontFace(gl.CW);');
	};








	this.clearAll = function() {

	};

}









// Initialize a shader program, so WebGL knows how to draw our data

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


// creates a shader of the given type, uploads the source and
// compiles it.

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

