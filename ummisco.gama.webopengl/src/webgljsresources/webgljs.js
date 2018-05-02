var canvas;

var camera, scene, renderer;
var num = 0;
var mesh;
var objects = [];
var initialized = 0;
var xwidth, xheight;


var gl; 
var buffers;

var COLOR_IDX;//36
var VERTICES_IDX;//37
var IDX_BUFF_IDX;//38
var NORMAL_IDX;//39
var UVMAPPING_IDX;//40
var programInfo;
var cubeRotation = 0.0;

var bufferArray=null;
var frameBufferArray=null;
var depthBufferArray=null;
var depthBufferTextureArray=null;
var textureArray=null;
var then = 0;
var deltaTime;

class SubData{
  constructor(v,offset) {
    this.v = v;
    this.offset = offset;
  }
}
class Agent {
  constructor(v, vl,c,idx, nb) {
    this.v = v;
    this.vl = [];
    this.c = c;
	this.idx=idx;
	this.nb=nb;
  }
}
var buffer_type=0;
var ag=null;
function consolelog(str){
//	console.log(str);
}
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
	consolelog("canvas.getContext "+gl); 
	// Only continue if WebGL is available and working
	if (!gl) {
		alert("Unable to initialize WebGL. Your browser or machine may not support it.");
		return;
	}


	  
         /*====================== Shaders =======================*/
 /*
         // Vertex shader source code
         var vertCode =
            'attribute vec3 coordinates;' +
            'void main(void) {' +
               ' gl_Position = vec4(coordinates, 1.0);' +
            '}';
         
         // Create a vertex shader object
         var vertShader = gl.createShader(gl.VERTEX_SHADER);

         // Attach vertex shader source code
         gl.shaderSource(vertShader, vertCode);

         // Compile the vertex shader
         gl.compileShader(vertShader);

         // Fragment shader source code
         var fragCode =
            'void main(void) {' +
               ' gl_FragColor = vec4(0.0, 0.0, 0.0, 0.1);' +
            '}';
         
         // Create fragment shader object 
         var fragShader = gl.createShader(gl.FRAGMENT_SHADER);
         // Attach fragment shader source code
         gl.shaderSource(fragShader, fragCode);
         // Compile the fragmentt shader
         gl.compileShader(fragShader);
         // Create a shader program object to
         // store the combined shader program
         var shaderProgram = gl.createProgram();
         // Attach a vertex shader
         gl.attachShader(shaderProgram, vertShader);
         // Attach a fragment shader
         gl.attachShader(shaderProgram, fragShader);
         // Link both the programs
         gl.linkProgram(shaderProgram);
         // Use the combined shader program object
         gl.useProgram(shaderProgram);

		 */
		 
		 
		 /*
		 var  vertCode = 'attribute vec3 position;'+
	            'uniform mat4 Pmatrix;'+
	            'uniform mat4 Vmatrix;'+
	            'uniform mat4 Mmatrix;'+
	            'attribute vec3 color;'+//the color of the point
	            'varying vec3 vColor;'+
	            'void main(void) { '+//pre-built function
	               'gl_Position = Pmatrix*Vmatrix*Mmatrix*vec4(position, 1.);'+
	               'vColor = color;'+
	            '}';
				/**/
			var vertCode =
				'uniform mat4    Mmatrix;'+
				'uniform mat4    Pmatrix;'+
				'uniform mat4    Vmatrix;'+

				'attribute vec3  position;'+
				'attribute vec3  color;'+
				'varying vec3    vColor;'+
				''+
				'void main(void)'+
				'{'+
				'	vec4 worldPosition = Mmatrix * vec4(position,1.0);'+
				'	'+
				'	vColor = color;'+
				'	mat4 modelView = Vmatrix * Mmatrix;'+
				'		'+
				'	gl_Position = Pmatrix * modelView * vec4(position,1.0);'+
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
				
	         var shaderProgram = gl.createProgram();
	         gl.attachShader(shaderProgram, vertShader);
	         gl.attachShader(shaderProgram, fragShader);
	         gl.linkProgram(shaderProgram);
			 gl.useProgram(shaderProgram);
		 
COLOR_IDX = gl.createBuffer();//36
VERTICES_IDX = gl.createBuffer();//37
IDX_BUFF_IDX = gl.createBuffer();//38
NORMAL_IDX = gl.createBuffer();//39
UVMAPPING_IDX = gl.createBuffer();//40
		 /*
         // Create an empty buffer object to store vertex buffer
         var vertex_buffer = gl.createBuffer();

	     // Create and store data into color buffer
	     var color_buffer = gl.createBuffer ();
         // Create an empty buffer object to store Index buffer
         var Index_Buffer = gl.createBuffer();
		/**/
		
	         var _Pmatrix = gl.getUniformLocation(shaderProgram, "Pmatrix");
	         var _Vmatrix = gl.getUniformLocation(shaderProgram, "Vmatrix");
	         var _Mmatrix = gl.getUniformLocation(shaderProgram, "Mmatrix");

	         gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
	         var _position = gl.getAttribLocation(shaderProgram, "position");
	         gl.vertexAttribPointer(_position, 3, gl.FLOAT, false,0,0);
	         gl.enableVertexAttribArray(_position);
			 

	         gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);
	         var _color = gl.getAttribLocation(shaderProgram, "color");
	         gl.vertexAttribPointer(_color, 3, gl.FLOAT, false,0,0) ;
	         gl.enableVertexAttribArray(_color);
			 
	         function get_projection(angle, a, zMin, zMax) {
	            var ang = Math.tan((angle*.5)*Math.PI/180);//angle*.5
	             return [
					   0.5/ang, 0 , 0, 0,
					   0, 0.5*a/ang, 0, 0,
					   0, 0, -(zMax+zMin)/(zMax-zMin), -1,
					   0, 0, (-2*zMax*zMin)/(zMax-zMin), 0 
					   ];
	         }/**/
	         /*
	         function get_projection(fieldOfViewInRadians, aspect, near, far) {
	             var f = Math.tan(Math.PI * 0.5 - 0.5 * fieldOfViewInRadians);
				var rangeInv = 1.0 / (near - far);

				return [
				  f / aspect, 0, 0, 0,
				  0, f, 0, 0,
				  0, 0, (near + far) * rangeInv, -1,
				  0, 0, near * far * rangeInv * 2, 0
				];
	         }/**/
	         /*
	        var proj_matrix = get_projection(90, canvas.width/canvas.height, 1, 2000);
			//var proj_matrix = perspectiveMatrix(Math.PI / 2,  canvas.width/canvas.height, 0.1, 1000.0);
		
	         //var mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ];
	         var mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ];
	         //var view_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ];
			 
	         var view_matrix = [ 1,0,0,0,    0,1,-1,0,   0,0,1,1,     -(canvas.width/6),-(canvas.height/16),-70,1];

			/**/

	         var zoomFactor=100;
         var proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,-1);
         var mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,0, 0,0,0,1 ];
         var view_matrix = [ 1,0,0,0,     0,1,0,0,    0,0,1,1,    -(canvas.width/6),-(canvas.height/3),-300,-10 ];

		
			
		  var vertices;	 
		    var colors; 
			var indices=[];
			
			
         /*================= Mouse events ======================*/

         var AMORTIZATION = 0;
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
            dX = (e.pageX-old_x)*Math.PI/canvas.width,
            dY = (e.pageY-old_y)*Math.PI/canvas.height;
            THETA+= dX;
            PHI+=dY;
            old_x = e.pageX, old_y = e.pageY;
            e.preventDefault();
         };

         var mouseWheel = function(e){   
        	 zoomFactor *= (e.deltaY ? e.deltaY : e.wheelDelta ? e.wheelDelta : e.detail) < 0 ? 0.91  : 1.1;
         };

         canvas.addEventListener("DOMMouseScroll", mouseWheel, false);
         canvas.addEventListener("mousewheel", mouseWheel, false);
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
					/*
            gl.clearColor(0.0, 0.0, 0.0, 0.0);
            gl.clearDepth(1.0);
            gl.viewport(0.0, 0.0, canvas.width, canvas.height);
            gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
*/
            proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,-1);
			for(var an_agent of objects){

				
				for(var sd of an_agent.vl){
					gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// 37
					vertices=sd.v;		
					gl.bufferData(34962, an_agent.nb, 35044);  
					gl.bufferSubData(gl.ARRAY_BUFFER, sd.offset, new Float32Array(vertices));//
					
		            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
		            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
		            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);

		            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
					gl.drawElements(gl.TRIANGLES, vertices.length, gl.UNSIGNED_SHORT, 0);
				
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
				indices=an_agent.idx;
				
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 

	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);

	            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
				gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
				}
			
			}							
            window.requestAnimationFrame(animate);
             
             
           
         }

         animate(0);
			 
		gl.viewport(0,0,canvas.width,canvas.height);
			 
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

        gl.enable(gl.DEPTH_TEST);
				
         gl.depthFunc(gl.LEQUAL);

	this.appendInfo = function(text) {

				if(ag==null){
					objects=[];
					ag=new Agent();
				}
		
				gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// 37
		
				gl.bufferData(34962, 1152, 35044);  
				vertices=[25,75,0,75,75,0,75,25,0,25,25,0,25,75,50,75,75,50,75,25,50,25,25,50,75,75,0,25,75,0,75,25,0,25,25,0,25,75,50,75,75,50,75,25,50,25,25,50,75,75,0,75,75,50,25,75,50,25,75,0,75,25,0,75,25,50,25,25,0,25,25,50
		
				];
				ag.vl=[];
				var sd=new SubData(vertices,0);
				ag.nb=1152;
				ag.vl.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(vertices));//
		
				gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// 36
				gl.bufferData(34962, 1152, 35044);  
				colors=[0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,0,1,0,1,0,0,0,-1,0,0,1,0,0,1,0,1,0,0,0,-1,0,1,0,0,1,0,0,-1,0,0,-1,0,0,0,-1,0,0,-1,0,-1,0,0,-1,0,0];
				ag.c=colors;
				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(colors));//0,
		
		
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
				indices=[0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22];
				ag.idx=indices;
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
		
						gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
						gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
						gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
				gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);//144
				
				

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;
/*						/*	*/
				
		        // animate(0);
				 
						
				consolelog("appendinfo ");
	};


	this.appendWarn = function(text) {
		
		
		 consolelog("appendWarn ");
		 
		 
		 
		 
		 
		 
	};



	this.clearAll = function() {

	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);

				
	            gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
				

		 consolelog("clearAll");
	};

	this.appendErr = function(text) {
		
		consolelog("appendErr ");
	};

	this.glDrawElements = function(glTriangles, i, glUnsignedInt, j) {
		

		consolelog("gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);//"+i);
		
	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
		gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);

		indices=i;
		var obj_copy = Object.create(ag);
		objects.push(obj_copy);
		ag=null;
		//gl.drawElements(glTriangles, i,  gl.UNSIGNED_BYTE, j);
		//gl.drawArrays(glTriangles, 0, 3);
		//gl.drawElements(gl.POINTS, 8, gl.UNSIGNED_BYTE, 0);
		
	
		//gl.drawElements(gl.TRIANGLE_STRIP, 4, gl.UNSIGNED_SHORT, 0);
		
		//gl.drawElements(glTriangles, i, gl.UNSIGNED_SHORT, j);
		

	};

	this.glBindFramebuffer = function(glFramebuffer, i) {
		//consolelog("gl.bindFramebuffer ("+glFramebuffer+","+i+");  ");
		//gl.bindFramebuffer(glFramebuffer, frameBufferArray);
//		gl.bindFramebuffer(glFramebuffer, i);
//		gl.bindFramebuffer(gl.FRAMEBUFFER, frameBufferArray); 
	};
	this.glViewport = function(i,j,width, height) {
		//consolelog("gl.viewport ("+i+","+j+","+width+","+ height+"); //"+canvas.width+" "+canvas.height);
		//gl.viewport(i,j,width, height);
		//gl.viewport(0,0,canvas.width,canvas.height);
	};


	this.glBindBuffer = function(glElementArrayBuffer, i) {
// gl.bindBuffer(glElementArrayBuffer, i);
		//consolelog("gl.bindBuffer ("+glElementArrayBuffer+", bufferArray  );// "+i);
		if(ag==null){
			objects=[];
			ag=new Agent();
			buffer_type=i;
		}
		if(i==36){			
			consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// "+i);
			gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);					
		}
		if(i==37){			
			consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// "+i);
			gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
		}
		if(i==38){			
			consolelog("gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);// "+i);
			gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
		}
		if(i==39){			
			consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);// "+i);
			gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);
		}
		
		//gl.bindBuffer(gl.ARRAY_BUFFER, bufferArray);
	};

	this.glBindRenderbuffer = function(glRenderbuffer, i) {
// gl.glBindRenderbuffer(depthBufferArray, i);
		//consolelog("gl.bindRenderbuffer ("+glRenderbuffer+", depthBufferArray);//"+i);
		//gl.bindRenderbuffer(gl.RENDERBUFFER, depthBufferArray);
	};

	this.glRenderbufferStorage = function(glRenderbuffer, glDepthComponent, width, height) {
// gl.renderbufferStorage(glRenderbuffer, glDepthComponent, width, height);//not use

//		gl.renderbufferStorage(gl.RENDERBUFFER, gl.RBGA4, 800, 600);
		//consolelog("gl.renderbufferStorage(gl.RENDERBUFFER, gl.RBGA4, 800, 600) = "+glRenderbuffer);
	};
	this.glFramebufferRenderbuffer = function(glFramebuffer, glDepthAttachment, glRenderbuffer, i) {
// gl.framebufferRenderbuffer(glFramebuffer, glDepthAttachment, glRenderbuffer,
// i);
//		gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.RENDERBUFFER, depthBufferArray);
//		consolelog("gl.framebufferRenderbuffer(gl.FRAMEBUFFER, gl.COLOR_ATTACHMENT0, gl.RENDERBUFFER, depthBufferArray); = "+depthBufferArray);
	};


	this.glGenFramebuffers = function(i,f,j) {
		/*
		if(frameBufferArray==null){
			consolelog("frameBufferArray= gl.createFramebuffer();  ");
			frameBufferArray= gl.createFramebuffer();
		}
		/**/
	};

	this.glGenBuffers = function(i,vboHandles,j) {
		/*
		if(bufferArray==null){
			consolelog("bufferArray= gl.createBuffer(); ");
			bufferArray= gl.createBuffer();
		}
		/**/
	};

	this.glBufferData = function(glElementArrayBuffer, numBytes, intIdxBuffer, glStaticDraw) {	  
		intIdxBuffer=JSON.stringify(intIdxBuffer).replace("NaN","-1").replace("-Infinity","-1");
		
		var v = intIdxBuffer.substr( 2, intIdxBuffer.length-2 ).split(',');


		/*
		var min=Math.min(...v);
		
		var max=Math.max(...v);
		

		for(var i=0; i<v.length; i++) {
			v[i] *= (1.0/(max-min));
		}
		/**/
// gl.bufferData(target, new Float32Array(JSON.parse(srcData)),usage);
		if(glElementArrayBuffer==gl.ELEMENT_ARRAY_BUFFER){
			

			v=v.map(function(i){return parseInt(i);});
			//gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);
			//if(isNaN(v)){
				//consolelog("gl.bufferData("+glElementArrayBuffer+", "+numBytes+", "+glStaticDraw+");  ");
				//gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
			//}else{
				consolelog("gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);");
				consolelog("gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);// "+new Uint16Array(v));
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);// 38
				ag.idx=v;
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);
			//}
		}
		if(glElementArrayBuffer==gl.ARRAY_BUFFER){
			

			v=v.map(function(i){return parseFloat(i);});
			if(isNaN(v[0])){
				 consolelog("gl.bufferData("+glElementArrayBuffer+", "+numBytes+", "+glStaticDraw+");  ");
				 ag.nb=numBytes;
				 gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
			}else{
				consolelog("gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);// "+new Float32Array(v));
				ag.c=c;
				gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);
			}
		}
		//gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
		 //new Float32Array(JSON.parse(intIdxBuffer))
	};

	this.glBufferSubData = function(glArrayBuffer, offset, i, fbData) {		
		fbData=JSON.stringify(fbData).replace("NaN","-1").replace("-Infinity","-1");
		//consolelog("gl.bufferSubData("+gl.ARRAY_BUFFER+","+ 512, fbData); = "+glArrayBuffer+"  fbData "+ new Float32Array(JSON.parse(fbData))+" "+offset);
		
		var v = fbData.substr( 2, fbData.length-2 ).split(',');



		v=v.map(function(i){return parseFloat(i);});
		
		/*
		var min=Math.min(...v);
		
		var max=Math.max(...v);
		

		for(var i=0; i<v.length; i++) {
			v[i] *= ((4.0/(max-min)));
		}
		/**/
		//console.log(i+"gl.bufferData("+glArrayBuffer+","+ offset+","+ vertices+");");
        //gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
		consolelog("gl.bufferSubData(gl.ARRAY_BUFFER, offset, new Float32Array(v));//");//+ offset+","+ new Float32Array(v) );
		var sd=new SubData(v,offset);
		ag.vl.push(sd);
		gl.bufferSubData(gl.ARRAY_BUFFER, offset, new Float32Array(v));
	    //gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);

		 
	};

	this.glDrawBuffer = function(glColorAttachment0) {		
		//consolelog("gl.drawbuffers([gl.NONE, "+glColorAttachmbufferSubDataent0+"]); ");		
		//gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]);
		
// gl.drawBuffers(frameBufferArray);
		//gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]);
		 
	};

	this.glClearColor = function(f,g,h,i) {
		
		//consolelog("gl.clearColor("+f+", "+g+", "+h+", "+i+")");
		//gl.clearColor(f,g,h,i);
		 
	};



	this.glEnableVertexAttribArray = function(attributePosition) {
/*
        var coord = gl.getAttribLocation(shaderProgram, "coordinates");
        // point an attribute to the currently bound VBO
        gl.vertexAttribPointer(coord, 2, gl.FLOAT, false, 0, 0);
        // Enable the attribute
        gl.enableVertexAttribArray(coord);
/**/		
		//consolelog("gl.enableVertexAttribArray("+attributePosition+"); //more");
		 
	};


	this.glClear = function(i) {
		
		//consolelog("gl.clear("+i+");");
		//gl.clear(i);
		//gl.clear(gl.COLOR_BUFFER_BIT);
		
		 
	};



	this.glEnable = function(glDepthTest) {
		
		//consolelog("gl.enable("+gl.DEPTH_TEST+"); ");
		 
		//gl.enable(glDepthTest);
		 //gl.enable(gl.DEPTH_TEST);
	};


	this.glGenRenderbuffers = function(i,dBufferArray,j) {
		/*
		if(depthBufferArray==null){
			consolelog("depthBufferArray = gl.createRenderbuffer(); // "+i+" "+dBufferArray+" "+j);		
			depthBufferArray = gl.createRenderbuffer();
		 }
		 /**/
	};

	


	this.glDepthFunc = function(glLequal) {
		//consolelog("gl.depthFunc("+glLequal+"); ");		
		//gl.depthFunc(glLequal);		 
	};




	this.glFrontFace = function(text) {
		//consolelog("gl.frontFace("+text+");");
		//gl.frontFace(gl.CW);
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


// utilities: vector and matrix
var Vec3 = function (x, y, z) {
  var vec = [x, y, z];
  vec.x = x;
  vec.y = y;
  vec.z = z;
  vec.len = function () {
    return Math.sqrt(x * x + y * y + z * z);
  };
  vec.is_zero = function () {
    return vec.len() == 0;
  };
  vec.mul = function (a) {
    return Vec3(a * x, a * y, a * z);
  };
  vec.normalize = function () {
    return vec.is_zero() ? vec : vec.mul(1 / vec.len());
  };
  vec.dot = function (other) {
    return x * other.x + y * other.y + z * other.z;
  };
  vec.add = function (other) {
    return Vec3(x + other.x, y + other.y, z + other.z);
  };
  vec.sub = function (other) {
    return Vec3(x - other.x, y - other.y, z - other.z);
  };
  vec.cross = function (other) {
    return Vec3(
      y * other.z - z * other.y,
      z * other.x - x * other.z,
      x * other.y - y * other.x);
  };
  vec.reflect = function (other) {
    // TBD: it it ok?
    var d2 = vec.dot(other) * 2;
    return Vec3(x - d2 * other.x, y - d2 * other.y, z - d2 * other.z);
    //return other.add(other.sub(vec));
  };
  return vec;
};
var perspectiveMatrix = function (fov, aspect, near, far) {
  // as gluPerspective(), but "fov" is radian, aspect = h / w
  var zoom = 1.0 / Math.tan(fov / 2);
  return tr([
    zoom / aspect, 0, 0, 0,
    0, zoom, 0, 0,
    0, 0, (far + near) / (near - far), 2 * far * near / (near - far),
    0, 0, -1, 0,
  ]);
};
var lookAtMatrix = function (eye, center, up) {
  // as gluLookAt(), but each params are Vec3
  var lz = center.sub(eye).normalize(); // z-axis of eye ray
  var nup = up.normalize();
  var lx = lz.cross(nup); // x-axis of eye ray
  var ly = lx.cross(lz);  // y-axis of eye ray
  return tr([
    lx.x, lx.y, lx.z, -eye.x,
    ly.x, ly.y, ly.z, -eye.y,
    -lz.x, -lz.y, -lz.z, -eye.z,
    0, 0, 0, 1,
  ]); // = identity4.rotate(lx, ly, -lz).translate(-eye)
};
// for C array layout of OpenGL matrix
var tr = function (mat) {
  return [
    mat[0], mat[4], mat[8], mat[12],
    mat[1], mat[5], mat[9], mat[13],
    mat[2], mat[6], mat[10], mat[14],
    mat[3], mat[7], mat[11], mat[15],
  ];
};