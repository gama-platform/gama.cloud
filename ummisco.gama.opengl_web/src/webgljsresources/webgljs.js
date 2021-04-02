var canvas;

var camera, scene, renderer;
var num = 0;
var mesh;
var objects = [];
var initialized = 0;

var gl; 
var buffers;

var COLOR_IDX;// 36
var VERTICES_IDX;// 37
var IDX_BUFF_IDX;// 38
var NORMAL_IDX;// 39
var UVMAPPING_IDX;// 40
var programInfo;
var cubeRotation = 0.0;

var clearColor=[1,1,1,1];
var drawElementMode=0;
var requestId;
var bufferArray=null;
var frameBufferArray=null;
var depthBufferArray=null;
var depthBufferTextureArray=null;
var textureArray=null;
var then = 0;
var deltaTime;
var totalRequiredObject=0;

var buffer_type=0;
var ag=null;
var debug=false;
class SubData{
  constructor(v,offset) {
    this.v = v;
    this.offset = offset;
  }
}
class Agent {
  constructor( vl,c,nm,uv,idx, nb, nbc, nbnm, nb_uv) {
    
    this.vl = [];
    this.c = [];
    this.nm = [];
    this.uv=[];
	this.idx=idx;
	this.nb=nb;
	this.nbc=nbc;
	this.nbnm=nbnm;
	this.nb_uv=nb_uv;
  }
}
function consolelog(str){
	if(debug){		
		console.log(str);
	}
}
function handleContextLost(event) {
    event.preventDefault();
    totalRequiredObject=0;
    proj_matrix=null;  
    view_matrix=null;
    mo_matrix=null;
    isPlaying = false;  
    console.log("contextlost "+totalRequiredObject);
    window.cancelRequestAnimationFrame=( function() {
    return window.cancelAnimationFrame          ||
        window.webkitCancelRequestAnimationFrame    ||
        window.mozCancelRequestAnimationFrame       ||
        window.oCancelRequestAnimationFrame     ||
        window.msCancelRequestAnimationFrame        ||
        window.clearTimeout;
} )();
    	
 }


var myparent;
var isPlaying = false;
var start_animation=0;
var mye;
function WebGLJS(p,e) {
	canvas = document.createElement("canvas");
	myparent=p;
	mye=e;
	canvas.className = 'webgljs';
// canvas = this.div;
	
	var area = myparent.getClientArea();

	  canvas.width = area[2];      // make sure bitmap is updated as well
	  canvas.height = area[3];
	  canvas.style.left = area[0] + "px";
	  canvas.style.top = area[1] + "px";
	  canvas.style.width = area[2]+'px';
	  canvas.style.height = area[3]+'px';
	  window.addEventListener('resize', function(event){
			var area = myparent.getClientArea();
//			console.log(area);

			 mye.width = area[2];      // make sure bitmap is updated as well
			 mye.height = area[3];
			 mye.style.left = area[0] + "px";
			 mye.style.top = area[1] + "px";
			 mye.style.width = area[2]+'px';
			 mye.style.height = area[3]+'px';
			 
			  canvas.width = area[2];      // make sure bitmap is updated as
											// well
			  canvas.height = area[3];
			  canvas.style.left = area[0] + "px";
			  canvas.style.top = area[1] + "px";
			  canvas.style.width = area[2]+'px';
			  canvas.style.height = area[3]+'px';

				gl.viewport(0,0,canvas.width,canvas.height);
					 
		});
	  
// canvas.width = 1024;
// canvas.height = 784;

// canvas.style.width = "100%";
// canvas.style.height = "100%";

	initialized = 0;

	if (e.firstChild != null) {
		e.firstChild=null;
		e.removeChild(e.firstChild);
	}
	e.appendChild(canvas);

	gl = canvas.getContext('webgl');
	canvas.addEventListener(
			'webglcontextlost', handleContextLost, false);
	if(debug){consolelog("canvas.getContext "+gl); }

	if (!gl) {
		alert("Unable to initialize WebGL. Your browser or machine may not support it.");
		return;
	}


	  
			var vertCode = 'attribute vec3 position;'+
            'uniform mat4 Pmatrix;'+
            'uniform mat4 Vmatrix;'+
            'uniform mat4 Mmatrix;'+
            'attribute vec2 a_texcoord;'+
            'attribute vec4 color;'+// the color of the point
            'attribute vec3 a_normal;'+// the color of the point
            'varying vec4 vColor;'+
            'varying vec3 v_normal;'+
            'varying vec2 v_texcoord;'+
            'void main(void) { '+// pre-built function
               'gl_Position = Pmatrix*Vmatrix*Mmatrix*vec4(position, 1.0);'+
               'vColor = color;'+
               'v_normal = a_normal;'+
               '  v_texcoord = a_texcoord;'+
            '}';
			
			
			
			
			
	         var fragCode = 'precision mediump float;'+
	         'varying vec4 vColor;'+
	            'varying vec3 v_normal;'+
	            'varying vec2 v_texcoord;'+
	            'uniform vec3 u_reverseLightDirection;'+
	            'uniform sampler2D u_texture;'+
// 'uniform vec4 u_color;'+
	            'void main(void) {'+
	               'vec3 normal = normalize(v_normal);'+
	               'float light = dot(normal, u_reverseLightDirection);'+
// 'gl_FragColor =vColor ;'+
	               'gl_FragColor = texture2D(u_texture, v_texcoord);'+
// 'gl_FragColor = u_color;'+
	               'gl_FragColor.rgb *= light;'+
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
		 
			COLOR_IDX = gl.createBuffer();// 36
			VERTICES_IDX = gl.createBuffer();// 37
			IDX_BUFF_IDX = gl.createBuffer();// 38
			NORMAL_IDX = gl.createBuffer();// 39
			UVMAPPING_IDX = gl.createBuffer();// 40

		
	         var _Pmatrix = gl.getUniformLocation(shaderProgram, "Pmatrix");
	         var _Vmatrix = gl.getUniformLocation(shaderProgram, "Vmatrix");
	         var _Mmatrix = gl.getUniformLocation(shaderProgram, "Mmatrix");
// var colorLocation = gl.getUniformLocation(shaderProgram, "u_color");
	         var reverseLightDirectionLocation = gl.getUniformLocation(shaderProgram, "u_reverseLightDirection");

	         gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
	         var _position = gl.getAttribLocation(shaderProgram, "position");
	         gl.vertexAttribPointer(_position, 3, gl.FLOAT, false,0,0);
	         gl.enableVertexAttribArray(_position);
	         var texcoordLocation = gl.getAttribLocation(shaderProgram, "a_texcoord");
	         var texbuffer = gl.createBuffer();
	         gl.bindBuffer(gl.ARRAY_BUFFER, texbuffer);
	         gl.enableVertexAttribArray(texcoordLocation);
	         gl.vertexAttribPointer(texcoordLocation, 2, gl.FLOAT, false, 0, 0);
	         gl.bufferData(
	        	      gl.ARRAY_BUFFER,
	        	      new Float32Array([
	        	    	  // left column front
// 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
	        	    	  
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0
	        	    	  1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1,
	        	    	  1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 
	        	    	  1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 
	        	    	  0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1,
	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
	        	    	  0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0

// // top rung front
// 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//
// // middle rung front
// 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
//
// // left column back
// 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1,
//
// // top rung back
// 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1,
//
// // middle rung back
// 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1
	        	          
	        	          
	        	          ]),
	        	        gl.STATIC_DRAW);
	         // Create a texture.
	         var texture = gl.createTexture();
	         var texture1 = gl.createTexture();

	         // Asynchronously load an image
	         var image = new Image();
//	         image.src = "http://i931.photobucket.com/albums/ad158/Eraco-Draco-photo/M03FireplaceBox.jpg";
	         image.src = "http://i931.photobucket.com/albums/ad158/Eraco-Draco-photo/woods1.jpg";
//	         image.src = "file://C:/git/gama/msi.gama.models/models/Features/3D Visualization/images/building_texture/texture1.jpg";

	         image.crossOrigin = "anonymous";  // This enables CORS
	         image.addEventListener('load', function() {
		         gl.bindTexture(gl.TEXTURE_2D, texture);
		          
		         // Fill the texture with a 1x1 blue pixel.
		         gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE,
		                       new Uint8Array([0, 0, 255, 255]));
	           // Now that the image has loaded make copy it to the texture.
	           gl.bindTexture(gl.TEXTURE_2D, texture);
	           gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image);
	           gl.generateMipmap(gl.TEXTURE_2D);
	         });
	         // Asynchronously load an image
	         var image1 = new Image();
	         image1.src = "http://i931.photobucket.com/albums/ad158/Eraco-Draco-photo/M03FireplaceBox.jpg";

	         image1.crossOrigin = "anonymous";  // This enables CORS
	         image1.addEventListener('load', function() {
		         gl.bindTexture(gl.TEXTURE_2D, texture1);
		          
		         // Fill the texture with a 1x1 blue pixel.
		         gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, 1, 1, 0, gl.RGBA, gl.UNSIGNED_BYTE,
		                       new Uint8Array([0, 0, 255, 255]));
	           // Now that the image has loaded make copy it to the texture.
	           gl.bindTexture(gl.TEXTURE_2D, texture1);
	           gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image1);
	           gl.generateMipmap(gl.TEXTURE_2D);
	         });
	         
	         
	         
	         var normalLocation = gl.getAttribLocation(shaderProgram, "a_normal");
	      // Turn on the normal attribute
	         gl.enableVertexAttribArray(normalLocation);
	         // Tell the attribute how to get data out of normalBuffer
				// (ARRAY_BUFFER)
	         var size = 3;          // 3 components per iteration
	         var type = gl.FLOAT;   // the data is 32bit floating point values
	         var normalize = false; // normalize the data (convert from 0-255 to
									// 0-1)
	         var stride = 0;        // 0 = move forward size * sizeof(type) each
									// iteration to get the next position
	         var offset = 0;        // start at the beginning of the buffer
	         gl.vertexAttribPointer(
	             normalLocation, size, type, normalize, stride, offset)
	         
			 gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);
			 var _color = gl.getAttribLocation(shaderProgram, "color");
			 gl.vertexAttribPointer(_color, 4, gl.FLOAT, false,0,0) ;
			 gl.enableVertexAttribArray(_color);
			 
	         function get_projection(angle, a, zMin, zMax) {
	            var ang = Math.tan((angle*.5)*Math.PI/180);// angle*.5
	            if(proj_matrix_ori==null){	            	
	             return [
					   0.5/ang, 0 , 0, 0,
					   0, 0.5*a/ang, 0, 0,
					   0, 0, -(zMax+zMin)/(zMax-zMin), -1,
					   0, 0, (-2*zMax*zMin)/(zMax-zMin), 0 
					   ];
	            }else{	     
	            	return [
	            		proj_matrix_ori[0]/ang, proj_matrix_ori[1] , proj_matrix_ori[2], proj_matrix_ori[3],
	            		proj_matrix_ori[4], proj_matrix_ori[5]/ang, proj_matrix_ori[6], proj_matrix_ori[7],
	            		proj_matrix_ori[8], proj_matrix_ori[9], proj_matrix_ori[10]/1, proj_matrix_ori[11],
	            		proj_matrix_ori[12], proj_matrix_ori[13], proj_matrix_ori[14], proj_matrix_ori[15]/ang 
	            		];
	            }
	         }

	         var zoomFactor=90;
	         var translateX=0;//(canvas.width/20);
	         var translateY=0;//(canvas.height/20);
	         var dontknow1=-canvas.width/2;
	         var dontknow2=1;
	     var proj_matrix_ori;
         var view_matrix_ori;
         var mo_matrix_ori;
         var proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,100);
         var mo_matrix = [ 
             1,0,0,0, 
         	0,1,0,0, 
         	0,0,1,0, 
         	0,0,0,1 
       	];
         
         /*
			 * 
			 * 
			 * 1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1
			 */
         var view_matrix = [ 1,0,0,0,     0,1,0,0,    0,0,1,0,   
// -(canvas.width/8),-(canvas.height/3),-300,-10
        	 -(translateX),-(translateY),dontknow1,dontknow2
        	 ];

		
			
		  var vertices;	 
		    var colors; 
		    var normals; 
			var indices=[];
			
			
         /* ================= Mouse events ====================== */

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


         var translationFactor = 0.25;
         var mouseMove = function(e) {
        	 canvas.width+=0;
            if (!drag) return false;

            if (e.ctrlKey) {
            	dX = (e.pageX-old_x)*Math.PI/canvas.width,
            	dY = (e.pageY-old_y)*Math.PI/canvas.height;
            	THETA+= dX;
            	PHI+=dY;
            	old_x = e.pageX, old_y = e.pageY;
            }else{

            	translateX -=( (e.pageX-old_x))*translationFactor;
            	translateY +=( (e.pageY-old_y))*translationFactor;

            	old_x = e.pageX, old_y = e.pageY;
            }
        	e.preventDefault();
         };

         var mouseWheel = function(e){   
        	 zoomFactor *= (e.deltaY ? e.deltaY : e.wheelDelta ? e.wheelDelta : e.detail) < 0 ? 0.99  : 1.01; 
         };

         canvas.addEventListener("DOMMouseScroll", mouseWheel, false);
         canvas.addEventListener("mousewheel", mouseWheel, false);
//         canvas.addEventListener("mousedown", mouseDown, false);
//         canvas.addEventListener("mouseup", mouseUp, false);
//         canvas.addEventListener("mouseout", mouseUp, false);
//         canvas.addEventListener("mousemove", mouseMove, false);

         canvas.addEventListener("pointerdown", mouseDown, false);
//         canvas.addEventListener("touchstart", mouseDown, false);
	
         canvas.addEventListener("pointermove", mouseMove, false);
//         canvas.addEventListener("touchmove", mouseMove, false);
	
         canvas.addEventListener("pointerup", mouseUp, false);
//         canvas.addEventListener("touchend", mouseUp, false);

         canvas.addEventListener("pointerout", mouseUp, false);
         

         /* =========================rotation================ */

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
		 
		 

         
         
         
         
         
         
         
         
         
         
         
         
         /* =================== Drawing =================== */

         var THETA = 0,
         PHI = 0;
         var time_old = 0;
		 var fps = 12;
		 isPlaying = true;  
		 var announced=false;
         var animate = function(time) {
// console.log(objects.length+" "+totalRequiredObject);
        	 if(totalRequiredObject>0 && objects.length>=totalRequiredObject)
	         {
	        		 if(!announced){
	        			 announced=true;
// alert("Received "+objects.length+" objects");
	        		 }
	         }
        	 {	 
        		 
		     		gl.clearColor(clearColor[0],clearColor[1],clearColor[2],clearColor[3]);
		     		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
		            var dt = time-time_old;
							
		            if (!drag) {
		               dX *= AMORTIZATION, dY*=AMORTIZATION;
		               THETA+=dX, PHI+=dY;
		            }
		            mo_matrix = [
		               1,0,0,0, 
		            	0,1,0,0, 
		            	0,0,1,0, 
		            	0,0,0,1 
		            	
		            	];
		            
		            
		            rotateY(mo_matrix, -THETA);
		            rotateX(mo_matrix, -PHI);
		
		            proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,-1);
		            if(view_matrix_ori==null){
			            view_matrix = [ 1,0,0,0,     0,1,0,0,    0,0,1,0,   
			// -(canvas.width/8),-(canvas.height/3),-300,-10
			           	 -(translateX),-(translateY),dontknow1,dontknow2
			           	 ];
		            	
		            }else{
		            	
		            	view_matrix = [ view_matrix_ori[0], view_matrix_ori[1], view_matrix_ori[2], view_matrix_ori[3],
		            		view_matrix_ori[4], view_matrix_ori[5], view_matrix_ori[6], view_matrix_ori[7],    
		            		view_matrix_ori[8], view_matrix_ori[9], view_matrix_ori[10], view_matrix_ori[11],   
		            		view_matrix_ori[12]-(translateX), view_matrix_ori[13]-(translateY), view_matrix_ori[14], view_matrix_ori[15]
		            	];
		            }
		            
		            
		            
		            
		            objects.forEach(function(an_agent,cnt) {
		
						
						gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// 37
						gl.bufferData(34962, an_agent.nb, 35044);  	
						for(var sd1 of an_agent.vl){
							vertices=sd1.v;		
							gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//			
						}


							
						 gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// 36
						 gl.bufferData(34962, an_agent.nbc, 35044);
						 for(var sd1 of an_agent.c){
							 vertices=sd1.v;
							 gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//
						 }
						 if(an_agent.uv.length>0){							 
							 gl.bindBuffer(gl.ARRAY_BUFFER, UVMAPPING_IDX);// 36
							 gl.bufferData(34962, an_agent.nb_uv, 35044);
							 for(var sd1 of an_agent.uv){
								 vertices=sd1.v;
								 gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//
							 }
							 if( cnt % 2 == 0){
								 gl.bindTexture(gl.TEXTURE_2D, texture);
								 gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image);
							 }else{
								 gl.bindTexture(gl.TEXTURE_2D, texture1);
								 gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA,gl.UNSIGNED_BYTE, image1);
							 }
						 }
							
							
						gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
						indices=an_agent.idx;
						
						gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
						gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
						gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
						gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
						  gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1, 0.1]));
				           //gl.generateMipmap(gl.TEXTURE_2D);
						gl.drawElements(drawElementMode, indices.length, gl.UNSIGNED_SHORT , 0);
		// var max=Math.max(...indices);
		// consolelog(indices.length);
		// consolelog(max);
		            });		
		            time_old = time; 
		


		               
		             
        	 }
            // window.requestAnimationFrame(animate);

	  		  setTimeout(function() {

	  		    if (isPlaying === true) {
	  		    	requestId = window.requestAnimationFrame(animate);	  		    
	  		    }
	  		  },1/ fps );
         }

         animate(0);
			 
// gl.viewport(0,0,canvas.width,canvas.height);
//			 
// gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

 gl.enable(gl.DEPTH_TEST);
				
 gl.depthFunc(gl.LEQUAL);
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         

	this.appendInfo = function(text) {
		zoomFactor=3;
		translateX=+50;
		translateY=+20;
		totalRequiredObject=2;
			drawElementMode=gl.TRIANGLES;
				if(ag==null){
					objects=[];
					ag=new Agent();
				}
				ag.vl=[];				
				ag.c=[];
				ag.nm=[];


						
				ag.nb=288;// 384;
				vertices=[
					33.19456100463867,31.732711791992188,0,43.19456100463867,31.732711791992188,0,43.19456100463867,21.732711791992188,0,33.19456100463867,21.732711791992188,0,33.19456100463867,31.732711791992188,10,43.19456100463867,31.732711791992188,10,43.19456100463867,21.732711791992188,10,33.19456100463867,21.732711791992188,10,43.19456100463867,31.732711791992188,0,33.19456100463867,31.732711791992188,0,43.19456100463867,21.732711791992188,0,33.19456100463867,21.732711791992188,0,33.19456100463867,31.732711791992188,10,43.19456100463867,31.732711791992188,10,43.19456100463867,21.732711791992188,10,33.19456100463867,21.732711791992188,10,43.19456100463867,31.732711791992188,0,43.19456100463867,31.732711791992188,10,33.19456100463867,31.732711791992188,10,33.19456100463867,31.732711791992188,0,43.19456100463867,21.732711791992188,0,43.19456100463867,21.732711791992188,10,33.19456100463867,21.732711791992188,0,33.19456100463867,21.732711791992188,10
				];		
				var sd=new SubData(vertices,0);
				ag.vl.push(sd);



				ag.nbc=384;

				colors=[
					0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1,0.9764705896377563,0.41960784792900085,0.8980392217636108,1
				];				
				var sd=new SubData(colors,0);
				ag.c.push(sd);

		
				ag.nbnm=288;
				normals=[
					0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,0,1,0,1,0,0,0,-1,0,0,1,0,0,1,0,1,0,0,0,-1,0,1,0,0,1,0,0,-1,0,0,-1,0,0,0,-1,0,0,-1,0,-1,0,0,-1,0,0
				];				
				var sd=new SubData(normals,0);
				ag.nm.push(sd);



				indices=[
					0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22
				];
				ag.idx=indices;

		
		
		
		

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;



				
				ag=new Agent();


				ag.vl=[];				
				ag.c=[];
				ag.nm=[];
				
		
						
				ag.nb=288;// 384;
				  
				  
				  
				vertices=[
					64.37971496582031,19.599468231201172,0,74.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,0,64.37971496582031,19.599468231201172,10,74.37971496582031,19.599468231201172,10,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,10,74.37971496582031,19.599468231201172,0,64.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,0,64.37971496582031,19.599468231201172,10,74.37971496582031,19.599468231201172,10,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,10,74.37971496582031,19.599468231201172,0,74.37971496582031,19.599468231201172,10,64.37971496582031,19.599468231201172,10,64.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,10
				];		
				var sd=new SubData(vertices,0);
				ag.vl.push(sd);
				
				
				
				
				
				
				
				
				
				
				
		
				ag.nbc=384;
				gl.bufferData(34962, ag.nbc, 35044);  
				colors=[
					0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1
				];				
				var sd=new SubData(colors,0);
				ag.c.push(sd);
				
				
				
				
				
				

		
				ag.nbnm=288;
				normals=[
					0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,0,1,0,1,0,0,0,-1,0,0,1,0,0,1,0,1,0,0,0,-1,0,1,0,0,1,0,0,-1,0,0,-1,0,0,0,-1,0,0,-1,0,-1,0,0,-1,0,0
				];				
				var sd=new SubData(normals,0);
				ag.nm.push(sd);
				
				
		
				indices=[
					0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22
				];
				ag.idx=indices;
		
		
		
		
		
		
		
		

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;
		        // animate(0);
				 
						
				if(debug){consolelog("appendinfo ");}
	};




	this.glDrawElements = function(glTriangles, i, glUnsignedInt, j) {
		
		if(debug){consolelog("gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);//"+i);}
		
// gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
// gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
// gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
// // gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1,
// 0.1]));
// gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);
// animate(0);
		drawElementMode=glTriangles;
// indices=i;
// var obj_copy = ag;
		objects.push(ag);
		ag=null;
		// gl.drawElements(glTriangles, i, gl.UNSIGNED_BYTE, j);
		// gl.drawArrays(glTriangles, 0, 3);
		// gl.drawElements(gl.POINTS, 8, gl.UNSIGNED_BYTE, 0);
		
	
		// gl.drawElements(gl.TRIANGLE_STRIP, 4, gl.UNSIGNED_SHORT, 0);
		
		// gl.drawElements(glTriangles, i, gl.UNSIGNED_SHORT, j);
		

	};

	this.glBindFramebuffer = function(glFramebuffer, i) {
		
		if(debug){			
			for(var an_agent of objects){
				consolelog(an_agent.vl.length);
				var inds=an_agent.idx;
				var max=Math.max(...inds);
				consolelog(inds.length);
				consolelog(max);
			}
		}
		objects=[];
		
		// consolelog("gl.bindFramebuffer ("+glFramebuffer+","+i+"); ");
		// gl.bindFramebuffer(glFramebuffer, frameBufferArray);
// gl.bindFramebuffer(glFramebuffer, i);
// gl.bindFramebuffer(gl.FRAMEBUFFER, frameBufferArray);
	};

	this.glSendImage = function(img) {
//		 console.log(img);
		 image.src = "data:image/png;base64,"+img;
// console.log("gl.viewport ("+i+","+j+","+width+","+ height+");"+canvas.width+" "+canvas.height);
// gl.viewport(i,j,width, height);

//	 	zoomFactor=10+((width*height)/(canvas.width*canvas.height)*15);
//	 	translateX=(canvas.width/20)+((width-canvas.width)/18);
//	 	translateY=(canvas.height/20)+((height-canvas.height)/4); 
			 
	};
	
	this.glViewport = function(i,j,width, height) {
// console.log("gl.viewport ("+i+","+j+","+width+","+ height+");"+canvas.width+" "+canvas.height);
// gl.viewport(i,j,width, height);

//	 	zoomFactor=10+((width*height)/(canvas.width*canvas.height)*15);
//	 	translateX=(canvas.width/20)+((width-canvas.width)/18);
//	 	translateY=(canvas.height/20)+((height-canvas.height)/4);
		gl.viewport(0,0,canvas.width,canvas.height);
			 
	};

	this.glUniformMatrix4fv = function(type, i, b, arr, j) { 

		arr=JSON.stringify(arr).replace("NaN","-1").replace("-Infinity","-1");
		
		var v = arr.substr( 2, arr.length-2 ).split(',');

	

			v=v.map(function(i){return parseFloat(i);});

		if(type==0){
		     proj_matrix_ori=v;
		}
		if(type==1){
			view_matrix_ori=v;
		}
		if(type==2){
			mo_matrix_ori=v;
		}
 
	};

	
	this.glBindBuffer = function(glElementArrayBuffer, i) {
// gl.bindBuffer(glElementArrayBuffer, i);
		// consolelog("gl.bindBuffer ("+glElementArrayBuffer+", bufferArray );//
		// "+i);
		if(ag==null){
			// objects=[];
			ag=new Agent();
		}
		buffer_type=i;
		if(i==36){			
			if(debug){consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// "+i);}
			gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);					
		}
		if(i==37){			
			if(debug){consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// "+i);}
			gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
		}
		if(i==38){			
			if(debug){consolelog("gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);// "+i);}
			gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
		}
		if(i==39){			
			if(debug){consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);// "+i);}
			gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);
		}
		if(i==40){			
			if(debug){consolelog("gl.bindBuffer(gl.ARRAY_BUFFER, UVMAPPING_IDX);// "+i);}
			gl.bindBuffer(gl.ARRAY_BUFFER, UVMAPPING_IDX);
		}
		
		// gl.bindBuffer(gl.ARRAY_BUFFER, bufferArray);
	};


	this.glBufferData = function(glElementArrayBuffer, numBytes, intIdxBuffer, glStaticDraw) {	  
		if(ag==null){
			ag=new Agent();
		}
		intIdxBuffer=JSON.stringify(intIdxBuffer).replace("NaN","-1").replace("-Infinity","-1");
		
		var v = intIdxBuffer.substr( 2, intIdxBuffer.length-2 ).split(',');


		/***********************************************************************
		 * var min=Math.min(...v);
		 * 
		 * var max=Math.max(...v);
		 * 
		 * 
		 * for(var i=0; i<v.length; i++) { v[i] *= (1.0/(max-min)); } /
		 **********************************************************************/
// gl.bufferData(target, new Float32Array(JSON.parse(srcData)),usage);
		if(glElementArrayBuffer==gl.ELEMENT_ARRAY_BUFFER){
			

			v=v.map(function(i){return parseInt(i);});
			// gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v),
			// gl.STATIC_DRAW);
			// if(isNaN(v)){
				// consolelog("gl.bufferData("+glElementArrayBuffer+",
				// "+numBytes+", "+glStaticDraw+"); ");
				// gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, numBytes,
				// gl.STATIC_DRAW);
			// }else{
				if(debug){consolelog("gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);");}
				if(debug){consolelog("gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);// "+new Uint16Array(v));}
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);// 38
				
				ag.idx=v;
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);
			// }
		}
		if(glElementArrayBuffer==gl.ARRAY_BUFFER){
			

			v=v.map(function(i){return parseFloat(i);});
			if(isNaN(v[0])){
				 if(debug){consolelog("gl.bufferData("+glElementArrayBuffer+", "+numBytes+", "+glStaticDraw+");  ");}
						 
				 	if(buffer_type==36){			
						// COLOR_IDX);// "+i);
					 	ag.nbc=numBytes;
					}
					if(buffer_type==37){			
						// VERTICES_IDX);// "+i);
					 	ag.nb=numBytes;
					}
					if(buffer_type==39){			
						// NORMAL_IDX);// "+i);
						ag.nbnm=numBytes;
					}

					if(buffer_type==40){			
						// UVMAPPING_IDX);// "+i);
						ag.nb_uv=numBytes;
					}
				 
				 
				 
				 gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
			}else{
				if(debug){consolelog("gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);// "+new Float32Array(v));}
				gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);
			}
		}
		// gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
		 // new Float32Array(JSON.parse(intIdxBuffer))
	};

	this.glBufferSubData = function(glArrayBuffer, offset, i, fbData) {		
		if(ag==null){
			ag=new Agent();
		}
		fbData=JSON.stringify(fbData).replace("NaN","-1").replace("-Infinity","-1");
		// consolelog("gl.bufferSubData("+gl.ARRAY_BUFFER+","+ 512, fbData); =
		// "+glArrayBuffer+" fbData "+ new Float32Array(JSON.parse(fbData))+"
		// "+offset);
		
		var v = fbData.substr( 2, fbData.length-2 ).split(',');



		v=v.map(function(i){return parseFloat(i);});
		/*
		 * 
		 * var min=Math.min(...v);
		 * 
		 * var max=Math.max(...v);
		 * 
		 * 
		 * for(var i=0; i<v.length; i++) { v[i] *= ((4.0/(max-min))); }
		 * 
		 */
		// consolelog(i+"gl.bufferData("+glArrayBuffer+","+ offset+","+
		// vertices+");");
        // gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices),
		// gl.STATIC_DRAW);
		if(debug){consolelog("gl.bufferSubData(gl.ARRAY_BUFFER, offset, new Float32Array(v));//"+ offset+","+ new Float32Array(v) );}
		var sd=new SubData(v,offset);
		
		if(buffer_type==36){			
			// COLOR_IDX);// "+i);
			ag.c.push(sd);
		}
		if(buffer_type==37){			
			// VERTICES_IDX);// "+i);
			ag.vl.push(sd);
		}
		if(buffer_type==39){			
			// NORMAL_IDX);// "+i);
			ag.nm.push(sd);
		}
		if(buffer_type==40){			
			// UVMAPPING_IDX);// "+i);
			ag.uv.push(sd);
//			console.log("UVMAPPING_IDX "+v);
		}
		gl.bufferSubData(gl.ARRAY_BUFFER, offset, new Float32Array(v));
	    // gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);

		 
	};

	this.glDrawBuffer = function(glColorAttachment0) {		
		// consolelog("gl.drawbuffers([gl.NONE,
		// "+glColorAttachmbufferSubDataent0+"]); ");
		// gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]);
		
// gl.drawBuffers(frameBufferArray);
		// gl.drawbuffers([gl.NONE, gl.COLOR_ATTACHMENT1]);
		 
	};


	this.glBindRenderbuffer = function(glRenderbuffer, i) {	};
	this.glRenderbufferStorage = function(glRenderbuffer, glDepthComponent, width, height) {	};
	this.glFramebufferRenderbuffer = function(glFramebuffer, glDepthAttachment, glRenderbuffer, i) { };
	this.glGenFramebuffers = function(i,f,j) {	};
	this.glGenBuffers = function(i,vboHandles,j) {		
	};
	this.glEnableVertexAttribArray = function(attributePosition) {	};
	this.glStartAnimation = function(b) {	start_animation=b; };



	this.glClearColor = function(f,g,h,i) {	
// console.log("gl.clearColor"+f+" "+g+" "+h+" "+i);
		clearColor=[f,g,h,i];
// gl.clearColor(f,g,h,i);
	};

	this.glClear = function(i) {	
// console.log("gl.clear");
// gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
		};



	this.glEnable = function(glDepthTest) {        
		gl.enable(gl.DEPTH_TEST);
	};


	this.glGenRenderbuffers = function(i,dBufferArray,j) {	};

	


	this.glDepthFunc = function(glLequal) {
        gl.depthFunc(gl.LEQUAL);
	};

	

	this.totalObject = function(total) {	
		totalRequiredObject=total;
	};


	this.webgl_destroy = function(text) {	
		start_animation=0;
		gl.getExtension('WEBGL_lose_context').loseContext();
	};

	this.glFrontFace = function(text) {	};


	this.clearAll = function() {

	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);

				
	            gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
				

		 if(debug){consolelog("clearAll");}
	};

	this.appendErr = function(text) {		
		if(debug){consolelog("appendErr ");}
	};

	this.appendWarn = function(text) {
		 if(debug){consolelog("appendWarn ");}
	};






}








