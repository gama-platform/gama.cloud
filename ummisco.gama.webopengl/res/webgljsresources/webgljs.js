var canvas;

var camera, scene, renderer;
var num = 0;
var mesh;
var objects = [];
var initialized = 0;
var xwidth, xheight;


var gl; 
var buffers;

var COLOR_IDX;// 36
var VERTICES_IDX;// 37
var IDX_BUFF_IDX;// 38
var NORMAL_IDX;// 39
var UVMAPPING_IDX;// 40
var programInfo;
var cubeRotation = 0.0;

var drawElementMode=0;
var requestId;
var bufferArray=null;
var frameBufferArray=null;
var depthBufferArray=null;
var depthBufferTextureArray=null;
var textureArray=null;
var then = 0;
var deltaTime;
//var animate_paused=false;

class SubData{
  constructor(v,offset) {
    this.v = v;
    this.offset = offset;
  }
}
class Agent {
  constructor( vl,c,nm,idx, nb, nbc, nbnm) {
    
    this.vl = [];
    this.c = [];
    this.nm = [];
	this.idx=idx;
	this.nb=nb;
	this.nbc=nbc;
	this.nbnm=nbnm;
  }
}
var buffer_type=0;
var ag=null;
var debug=false;
function consolelog(str){
	 console.log(str);
}
function handleContextLost(event) {
    event.preventDefault();
    window.cancelRequestAnimationFrame=( function() {
    return window.cancelAnimationFrame          ||
        window.webkitCancelRequestAnimationFrame    ||
        window.mozCancelRequestAnimationFrame       ||
        window.oCancelRequestAnimationFrame     ||
        window.msCancelRequestAnimationFrame        ||
        window.clearTimeout;
} )();
    	
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
 'attribute vec4 color;'+// the color of the point
            'attribute vec3 a_normal;'+// the color of the point
 'varying vec4 vColor;'+
            'varying vec3 v_normal;'+
            'void main(void) { '+// pre-built function
               'gl_Position = Pmatrix*Vmatrix*Mmatrix*vec4(position, 1.0);'+
 'vColor = color;'+
               'v_normal = a_normal;'+
            '}';
	         var fragCode = 'precision mediump float;'+
 'varying vec4 vColor;'+
	            'varying vec3 v_normal;'+
	            'uniform vec3 u_reverseLightDirection;'+
//	            'uniform vec4 u_color;'+
	            'void main(void) {'+
	               'vec3 normal = normalize(v_normal);'+
	               'float light = dot(normal, u_reverseLightDirection);'+
	               'gl_FragColor =vColor ;'+
//	               'gl_FragColor = u_color;'+
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
//	         var colorLocation = gl.getUniformLocation(shaderProgram, "u_color");
	         var reverseLightDirectionLocation = gl.getUniformLocation(shaderProgram, "u_reverseLightDirection");

	         gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
	         var _position = gl.getAttribLocation(shaderProgram, "position");
	         gl.vertexAttribPointer(_position, 3, gl.FLOAT, false,0,0);
	         gl.enableVertexAttribArray(_position);
			 
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
	             return [
					   0.5/ang, 0 , 0, 0,
					   0, 0.5*a/ang, 0, 0,
					   0, 0, -(zMax+zMin)/(zMax-zMin), -1,
					   0, 0, (-2*zMax*zMin)/(zMax-zMin), 0 
					   ];
	         }

	         var zoomFactor=40;
	         var translateX=(canvas.width/6);
	         var translateY=(canvas.height/3);
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
        	 -(translateX),-(translateY),-canvas.width/2,1
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
         canvas.addEventListener("mousedown", mouseDown, false);
         canvas.addEventListener("mouseup", mouseUp, false);
         canvas.addEventListener("mouseout", mouseUp, false);
         canvas.addEventListener("mousemove", mouseMove, false);

         canvas.addEventListener("pointerdown", mouseDown, false);
         canvas.addEventListener("touchstart", mouseDown, false);
	
         canvas.addEventListener("pointermove", mouseMove, false);
         canvas.addEventListener("touchmove", mouseMove, false);
	
         canvas.addEventListener("pointerup", mouseUp, false);
         canvas.addEventListener("touchend", mouseUp, false);

         

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
		 var fps = 60;
		 
		 
         var animate = function(time) {
//        	 if(!animate_paused) {
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
		            
		            
		            rotateY(mo_matrix, THETA);
		            rotateX(mo_matrix, PHI);
		            time_old = time; 
		
		            proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,-1);
		            view_matrix = [ 1,0,0,0,     0,1,0,0,    0,0,1,0,   
		// -(canvas.width/8),-(canvas.height/3),-300,-10
		           	 -(translateX),-(translateY),-canvas.width/2,1
		           	 ];
		            
		            
		            
		            
		            
		            
		            objects.forEach(function(an_agent) {
		
						
						gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// 37
						gl.bufferData(34962, an_agent.nb, 35044);  	
						for(var sd1 of an_agent.vl){
							vertices=sd1.v;		
							gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//			
						}
							
		// gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
		// gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
		// gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
		//
		// gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
		// gl.drawElements(gl.TRIANGLES, vertices.length, gl.UNSIGNED_SHORT, 0);
							
						 gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// 36
						 gl.bufferData(34962, an_agent.nbc, 35044);
						 for(var sd1 of an_agent.c){
							 vertices=sd1.v;
							 gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//
						 }
							
		//				gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);// 36
		//				gl.bufferData(34962, an_agent.nbnm, 35044);  
		//				for(var sd1 of an_agent.nm){
		//					vertices=sd1.v;		
		//					gl.bufferSubData(gl.ARRAY_BUFFER, sd1.offset, new Float32Array(vertices));//				
		//				}
							
						gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
						indices=an_agent.idx;
						
						gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
		
						gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
						gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
						gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
						  // Set the color to use
		//				  gl.uniform4fv(colorLocation, [0.2, 1, 0.2, 1]); // green
						 
						  // set the light direction.
						  gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1, 0.1]));
		
					  // gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
						gl.drawElements(drawElementMode, indices.length, gl.UNSIGNED_SHORT , 0);
		//				var max=Math.max(...indices);
		//				consolelog(indices.length);
		//				consolelog(max);
		            });		
		

//        	 }

            // window.requestAnimationFrame(animate);
             
		  setTimeout(function() {
			  requestId = window.requestAnimationFrame(animate);
		  },1000 / fps);
             
           
         }

         animate(0);
			 
		gl.viewport(0,0,canvas.width,canvas.height);
			 
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

        gl.enable(gl.DEPTH_TEST);
				
         gl.depthFunc(gl.LEQUAL);
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         

	this.appendInfo = function(text) {
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

		
		
		
		
		
		
		
		
		
		

		
		
		

				
//				gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
//				gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
//				gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
//				gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1, 0.1]));
//				gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);// 144
				
				

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;
				
				
				
				
				


				
				ag=new Agent();


				ag.vl=[];				
				ag.c=[];
				ag.nm=[];
				
//				gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);// 37
		
						
				ag.nb=288;// 384;
//				gl.bufferData(34962, ag.nb, 35044);
				  
				  
				  
				vertices=[
					64.37971496582031,19.599468231201172,0,74.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,0,64.37971496582031,19.599468231201172,10,74.37971496582031,19.599468231201172,10,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,10,74.37971496582031,19.599468231201172,0,64.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,0,64.37971496582031,19.599468231201172,10,74.37971496582031,19.599468231201172,10,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,10,74.37971496582031,19.599468231201172,0,74.37971496582031,19.599468231201172,10,64.37971496582031,19.599468231201172,10,64.37971496582031,19.599468231201172,0,74.37971496582031,9.599466323852539,0,74.37971496582031,9.599466323852539,10,64.37971496582031,9.599466323852539,0,64.37971496582031,9.599466323852539,10
				];		
				var sd=new SubData(vertices,0);
				ag.vl.push(sd);
//				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(vertices));//
				
				
				
				
				
				
				
				
				
				
				
		
//				gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// 36
		
				ag.nbc=384;
				gl.bufferData(34962, ag.nbc, 35044);  
				colors=[
					0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1,0.3176470696926117,0.3686274588108063,0.24705882370471954,1
				];				
				var sd=new SubData(colors,0);
				ag.c.push(sd);
//				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(colors));// 0,
				
				
				
				
				
				

				
//				gl.bindBuffer(gl.ARRAY_BUFFER, NORMAL_IDX);// 39
		
				ag.nbnm=288;
//				gl.bufferData(34962, ag.nbnm, 35044);  
				normals=[
					0,0,-1,0,0,-1,0,0,-1,0,0,-1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,0,1,0,1,0,0,0,-1,0,0,1,0,0,1,0,1,0,0,0,-1,0,1,0,0,1,0,0,-1,0,0,-1,0,0,0,-1,0,0,-1,0,-1,0,0,-1,0,0
				];				
				var sd=new SubData(normals,0);
				ag.nm.push(sd);
//				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(normals));// 0,
				
				
		
//				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
				// indices=[0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22];
				indices=[
					0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22
				];
				ag.idx=indices;
//				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
		
		
		
		
		
		
		
		
		
//		
//				gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
//				gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
//				gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
//				gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1, 0.1]));
//				gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);// 144
				
				

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;
		        // animate(0);
				 
						
				if(debug){consolelog("appendinfo ");}
	};




	this.glDrawElements = function(glTriangles, i, glUnsignedInt, j) {
		
//		animate_paused=false;
		if(debug){consolelog("gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);//"+i);}
		
//	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
//	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
//	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
////				gl.uniform3fv(reverseLightDirectionLocation, m4.normalize([0.9, 0.1, 0.1]));
//		gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);
// animate(0);
		drawElementMode=glTriangles;
		indices=i;
		var obj_copy = Object.create(ag);
		objects.push(obj_copy);
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
//        animate_paused=true;
		objects=[];
		// consolelog("gl.bindFramebuffer ("+glFramebuffer+","+i+"); ");
		// gl.bindFramebuffer(glFramebuffer, frameBufferArray);
// gl.bindFramebuffer(glFramebuffer, i);
// gl.bindFramebuffer(gl.FRAMEBUFFER, frameBufferArray);
	};
	this.glViewport = function(i,j,width, height) {
		// consolelog("gl.viewport ("+i+","+j+","+width+","+ height+");
		// //"+canvas.width+" "+canvas.height);
		// gl.viewport(i,j,width, height);
		// gl.viewport(0,0,canvas.width,canvas.height);

		gl.viewport(0,0,canvas.width,canvas.height);
			 
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
		
		// gl.bindBuffer(gl.ARRAY_BUFFER, bufferArray);
	};


	this.glBufferData = function(glElementArrayBuffer, numBytes, intIdxBuffer, glStaticDraw) {	  
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

	this.glClearColor = function(f,g,h,i) {
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	};



	this.glBindRenderbuffer = function(glRenderbuffer, i) {	};
	this.glRenderbufferStorage = function(glRenderbuffer, glDepthComponent, width, height) {	};
	this.glFramebufferRenderbuffer = function(glFramebuffer, glDepthAttachment, glRenderbuffer, i) { };
	this.glGenFramebuffers = function(i,f,j) {	};
	this.glGenBuffers = function(i,vboHandles,j) {		
	};
	this.glEnableVertexAttribArray = function(attributePosition) {	};


	this.glClear = function(i) {	};



	this.glEnable = function(glDepthTest) {	};


	this.glGenRenderbuffers = function(i,dBufferArray,j) {	};

	


	this.glDepthFunc = function(glLequal) {	};

	


	this.webgl_destroy = function(text) {	
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








