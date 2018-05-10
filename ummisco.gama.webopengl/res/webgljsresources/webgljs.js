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

var disposed=false;
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
  constructor( vl,c,idx, nb, nbc) {
    
    this.vl = [];
    this.c = [];
	this.idx=idx;
	this.nb=nb;
	this.nbc=nbc;
  }
}
var buffer_type=0;
var ag=null;
function consolelog(str){
	//console.log(str);
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

	if (!gl) {
		alert("Unable to initialize WebGL. Your browser or machine may not support it.");
		return;
	}


	  
			var vertCode = 'attribute vec3 position;'+
            'uniform mat4 Pmatrix;'+
            'uniform mat4 Vmatrix;'+
            'uniform mat4 Mmatrix;'+
            'attribute vec4 color;'+// the color of the point
            'varying vec4 vColor;'+
            'void main(void) { '+// pre-built function
               'gl_Position = Pmatrix*Vmatrix*Mmatrix*vec4(position, 1.0);'+
               'vColor = color;'+
            '}';
	         var fragCode = 'precision mediump float;'+
	            'varying vec4 vColor;'+
	            'void main(void) {'+
	               'gl_FragColor =vColor ;'+
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

	         gl.bindBuffer(gl.ARRAY_BUFFER, VERTICES_IDX);
	         var _position = gl.getAttribLocation(shaderProgram, "position");
	         gl.vertexAttribPointer(_position, 3, gl.FLOAT, false,0,0);
	         gl.enableVertexAttribArray(_position);
			 

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
	         }/**/
	         /***************************************************************
				 * function get_projection(fieldOfViewInRadians, aspect, near,
				 * far) { var f = Math.tan(Math.PI * 0.5 - 0.5 *
				 * fieldOfViewInRadians); var rangeInv = 1.0 / (near - far);
				 * 
				 * return [ f / aspect, 0, 0, 0, 0, f, 0, 0, 0, 0, (near + far) *
				 * rangeInv, -1, 0, 0, near * far * rangeInv * 2, 0 ]; }/
				 **************************************************************/
	         /***************************************************************
				 * var proj_matrix = get_projection(90,
				 * canvas.width/canvas.height, 1, 2000); //var proj_matrix =
				 * perspectiveMatrix(Math.PI / 2, canvas.width/canvas.height,
				 * 0.1, 1000.0);
				 * 
				 * //var mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ]; var
				 * mo_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ]; //var
				 * view_matrix = [ 1,0,0,0, 0,1,0,0, 0,0,1,1, 0,0,0,1 ];
				 * 
				 * var view_matrix = [ 1,0,0,0, 0,1,-1,0, 0,0,1,1,
				 * -(canvas.width/6),-(canvas.height/16),-70,1]; /
				 **************************************************************/

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
        	 zoomFactor *= (e.deltaY ? e.deltaY : e.wheelDelta ? e.wheelDelta : e.detail) < 0 ? 0.91  : 1.1;
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
		 var fps = 25;
		 
		 
         var animate = function(time) {
            var dt = time-time_old;
					
            if (!drag) {
               dX *= AMORTIZATION, dY*=AMORTIZATION;
               THETA+=dX, PHI+=dY;
            }
               
            // set model matrix to I4
            mo_matrix = [
               1,0,0,0, 
            	0,1,0,0, 
            	0,0,1,0, 
            	0,0,0,1 
            	
            	];
            /*
			 * 
			 * 0.9999451693655121, -0.010195108091019157, -0.002391241014555666,
			 * 0, 0, 0.22835087011065547,-0.9735789028731603,0,
			 * 0.010471784116245783,0.9735255209241918, 0.22833834948756146, 0,
			 * 0,0,0,1
			 * 
			 */
            rotateY(mo_matrix, THETA);
            rotateX(mo_matrix, PHI);
            time_old = time; 
					/*
					 * gl.clearColor(0.0, 0.0, 0.0, 0.0); gl.clearDepth(1.0);
					 * gl.viewport(0.0, 0.0, canvas.width, canvas.height);
					 * gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
					 */
            proj_matrix = get_projection(zoomFactor, canvas.width/canvas.height, 1,-1);
            view_matrix = [ 1,0,0,0,     0,1,0,0,    0,0,1,0,   
// -(canvas.width/8),-(canvas.height/3),-300,-10
           	 -(translateX),-(translateY),-canvas.width/2,1
           	 ];
            
            
            
            
            
            
    
		gl.viewport(0,0,canvas.width,canvas.height);
			 
		gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

        gl.enable(gl.DEPTH_TEST);
				
         gl.depthFunc(gl.LEQUAL);
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            

			
		
			
			
			
			
			
			
			
			

			 
			
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            for(var an_agent of objects){

				
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
					
					gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
					indices=an_agent.idx;
					
					gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
	
		            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
		            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
		            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
	
		          // gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
					gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);
			
			}		
		



            // window.requestAnimationFrame(animate);
             
		  setTimeout(function() {
			  if(!disposed){				  
				  window.requestAnimationFrame(animate);
			  }            
		  }, 2000 / fps);
             
           
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
		
						
				ag.vl=[];				
				ag.nb=864;// 384;
				gl.bufferData(34962, 864, 35044);
				  
				  
				  
				vertices=[
					51.21086120605469,14.64973258972168,0,71.21086120605469,14.64973258972168,0,71.21086120605469,-5.35026741027832,0,51.21086120605469,-5.35026741027832,0,51.21086120605469,14.64973258972168,20,71.21086120605469,14.64973258972168,20,71.21086120605469,-5.35026741027832,20,51.21086120605469,-5.35026741027832,20,71.21086120605469,14.64973258972168,0,51.21086120605469,14.64973258972168,0,71.21086120605469,-5.35026741027832,0,51.21086120605469,-5.35026741027832,0,51.21086120605469,14.64973258972168,20,71.21086120605469,14.64973258972168,20,71.21086120605469,-5.35026741027832,20,51.21086120605469,-5.35026741027832,20,71.21086120605469,14.64973258972168,0,71.21086120605469,14.64973258972168,20,51.21086120605469,14.64973258972168,20,51.21086120605469,14.64973258972168,0,71.21086120605469,-5.35026741027832,0,71.21086120605469,-5.35026741027832,20,51.21086120605469,-5.35026741027832,0,51.21086120605469,-5.35026741027832,20
				];		
				var sd=new SubData(vertices,0);
				ag.vl.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(vertices));//
				
				vertices=[
					2.3990964889526367,36.84853744506836,0,22.399097442626953,36.84853744506836,0,22.399097442626953,16.84853744506836,0,2.3990964889526367,16.84853744506836,0,2.3990964889526367,36.84853744506836,20,22.399097442626953,36.84853744506836,20,22.399097442626953,16.84853744506836,20,2.3990964889526367,16.84853744506836,20,22.399097442626953,36.84853744506836,0,2.3990964889526367,36.84853744506836,0,22.399097442626953,16.84853744506836,0,2.3990964889526367,16.84853744506836,0,2.3990964889526367,36.84853744506836,20,22.399097442626953,36.84853744506836,20,22.399097442626953,16.84853744506836,20,2.3990964889526367,16.84853744506836,20,22.399097442626953,36.84853744506836,0,22.399097442626953,36.84853744506836,20,2.3990964889526367,36.84853744506836,20,2.3990964889526367,36.84853744506836,0,22.399097442626953,16.84853744506836,0,22.399097442626953,16.84853744506836,20,2.3990964889526367,16.84853744506836,0,2.3990964889526367,16.84853744506836,20
				];		
				var sd=new SubData(vertices,288);
				ag.vl.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 288, new Float32Array(vertices));//
				
				
				vertices=[
					-4.225046634674072,58.820411682128906,0,15.774953842163086,58.820411682128906,0,15.774953842163086,38.820411682128906,0,-4.225046634674072,38.820411682128906,0,-4.225046634674072,58.820411682128906,20,15.774953842163086,58.820411682128906,20,15.774953842163086,38.820411682128906,20,-4.225046634674072,38.820411682128906,20,15.774953842163086,58.820411682128906,0,-4.225046634674072,58.820411682128906,0,15.774953842163086,38.820411682128906,0,-4.225046634674072,38.820411682128906,0,-4.225046634674072,58.820411682128906,20,15.774953842163086,58.820411682128906,20,15.774953842163086,38.820411682128906,20,-4.225046634674072,38.820411682128906,20,15.774953842163086,58.820411682128906,0,15.774953842163086,58.820411682128906,20,-4.225046634674072,58.820411682128906,20,-4.225046634674072,58.820411682128906,0,15.774953842163086,38.820411682128906,0,15.774953842163086,38.820411682128906,20,-4.225046634674072,38.820411682128906,0,-4.225046634674072,38.820411682128906,20
				];		
				var sd=new SubData(vertices,576);
				ag.vl.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 576, new Float32Array(vertices));//
				
				
				
				
				
				
				
				
				
		
				gl.bindBuffer(gl.ARRAY_BUFFER, COLOR_IDX);// 36
		
				ag.c=[];
				ag.nbc=1152;
				gl.bufferData(34962, 1152, 35044);  
				colors=[
					0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1,0.364705890417099,0.16078431904315948,0.7647058963775635,1
				];				
				var sd=new SubData(colors,0);
				ag.c.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 0, new Float32Array(colors));// 0,
				
				colors=[
					0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1,0.45098039507865906,0.1411764770746231,0.7058823704719543,1
				];
				var sd=new SubData(colors,384);
				ag.c.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 384, new Float32Array(colors));// 0,
				
				colors=[
					0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1,0.14509804546833038,0.48627451062202454,0.40784314274787903,1
				];
				var sd=new SubData(colors,768);
				ag.c.push(sd);
				gl.bufferSubData(gl.ARRAY_BUFFER, 768, new Float32Array(colors));// 0,
				
				
				
				
				
				
		
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);
				// indices=[0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22];
				indices=[
					0,3,2,2,1,0,7,4,5,5,6,7,9,8,13,13,12,9,16,10,14,14,17,16,20,11,15,15,21,20,22,19,18,18,23,22,24,27,26,26,25,24,31,28,29,29,30,31,33,32,37,37,36,33,40,34,38,38,41,40,44,35,39,39,45,44,46,43,42,42,47,46,48,51,50,50,49,48,55,52,53,53,54,55,57,56,61,61,60,57,64,58,62,62,65,64,68,59,63,63,69,68,70,67,66,66,71,70
				];
				ag.idx=indices;
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);// 
		
						gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
						gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
						gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
				gl.drawElements(gl.TRIANGLES, indices.length, gl.UNSIGNED_SHORT, 0);// 144
				
				

				var obj_copy = Object.create(ag);
				objects.push(obj_copy);
				ag=null;
/* /* */
				
		        // animate(0);
				 
						
				consolelog("appendinfo ");
	};




	this.glDrawElements = function(glTriangles, i, glUnsignedInt, j) {
		
		disposed=false;
		consolelog("gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);//"+i);
		
	            gl.uniformMatrix4fv(_Pmatrix, false, proj_matrix);
	            gl.uniformMatrix4fv(_Vmatrix, false, view_matrix);
	            gl.uniformMatrix4fv(_Mmatrix, false, mo_matrix);
		gl.drawElements(gl.TRIANGLES, i, gl.UNSIGNED_SHORT, 0);

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
	};

	
	this.glBindBuffer = function(glElementArrayBuffer, i) {
// gl.bindBuffer(glElementArrayBuffer, i);
		// consolelog("gl.bindBuffer ("+glElementArrayBuffer+", bufferArray );//
		// "+i);
		if(ag==null){
			objects=[];
			ag=new Agent();
		}
		buffer_type=i;
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
				consolelog("gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);");
				consolelog("gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);// "+new Uint16Array(v));
				gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, IDX_BUFF_IDX);// 38
				ag.idx=v;
				gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(v), gl.STATIC_DRAW);
			// }
		}
		if(glElementArrayBuffer==gl.ARRAY_BUFFER){
			

			v=v.map(function(i){return parseFloat(i);});
			if(isNaN(v[0])){
				 consolelog("gl.bufferData("+glElementArrayBuffer+", "+numBytes+", "+glStaticDraw+");  ");
				 if(ag.nb==null){
				 	ag.nb=numBytes;
				 }else{				 
				 	ag.nbc=numBytes;
				 }
				 gl.bufferData(gl.ARRAY_BUFFER, numBytes, gl.STATIC_DRAW);
			}else{
				consolelog("gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(v), gl.STATIC_DRAW);// "+new Float32Array(v));
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
		
		/***********************************************************************
		 * var min=Math.min(...v);
		 * 
		 * var max=Math.max(...v);
		 * 
		 * 
		 * for(var i=0; i<v.length; i++) { v[i] *= ((4.0/(max-min))); } /
		 **********************************************************************/
		// console.log(i+"gl.bufferData("+glArrayBuffer+","+ offset+","+
		// vertices+");");
        // gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices),
		// gl.STATIC_DRAW);
		consolelog("gl.bufferSubData(gl.ARRAY_BUFFER, offset, new Float32Array(v));//"+ offset+","+ new Float32Array(v) );
		var sd=new SubData(v,offset);
		
		if(buffer_type==36){			
			// COLOR_IDX);// "+i);
			ag.c.push(sd);
		}
		if(buffer_type==37){			
			// VERTICES_IDX);// "+i);
			ag.vl.push(sd);
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

	this.glClearColor = function(f,g,h,i) {	};



	this.glBindRenderbuffer = function(glRenderbuffer, i) {	};
	this.glRenderbufferStorage = function(glRenderbuffer, glDepthComponent, width, height) {	};
	this.glFramebufferRenderbuffer = function(glFramebuffer, glDepthAttachment, glRenderbuffer, i) { };
	this.glGenFramebuffers = function(i,f,j) {	};
	this.glGenBuffers = function(i,vboHandles,j) {	};
	this.glEnableVertexAttribArray = function(attributePosition) {	};


	this.glClear = function(i) {	};



	this.glEnable = function(glDepthTest) {	};


	this.glGenRenderbuffers = function(i,dBufferArray,j) {	};

	


	this.glDepthFunc = function(glLequal) {	};

	


	this.webgl_destroy = function(text) {	
		disposed=true;
	};

	this.glFrontFace = function(text) {	};


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

	this.appendWarn = function(text) {
		 consolelog("appendWarn ");
	};






}








