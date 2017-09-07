var icanvas;

			var camera, scene, renderer;
			var num=0;
			var mesh;
			var objects = [];
			var initialized=0;
function WebGLJS(element)  {
	
   
   
   this.div = document.createElement("div");    
   this.div.className = 'webgljs';    
   icanvas = this.div; 
   element.appendChild(icanvas);
   
			

   this.appendInfo = function(text)  {        
			if(initialized <1){
				
			init();
			animate();
    initialized++;
			}
				
			adddd();
    

   };
    
   this.appendErr = function(text)  {  
       this.div.innerHTML = this.div.innerHTML
        + "<p class='err'>ERROR: "+ text +"</p>";
   };
    
   this.appendWarn = function(text)  {        
       this.div.innerHTML = this.div.innerHTML
        + "<p class='warn'>WARN: "+ text +"</p>";
   };
                
   this.clearAll = function()  {  
       this.div.innerHTML = "";
   };
   
}

			

			function init() {

				camera = new THREE.PerspectiveCamera( 70, window.innerWidth / window.innerHeight, 1, 10000 );
				camera.position.z = 1000;
				
				
				/*
				controls = new THREE.TrackballControls( camera );
				controls.rotateSpeed = 1.0;
				controls.zoomSpeed = 1.2;
				controls.panSpeed = 0.8;
				controls.noZoom = false;
				controls.noPan = false;
				controls.staticMoving = true;
				controls.dynamicDampingFactor = 0.3;
				*/
				
				scene = new THREE.Scene();
				scene.background = new THREE.Color( 0xffffff );

				scene.add( new THREE.AmbientLight( 0x505050 ) );

				var light = new THREE.SpotLight( 0xffffff, 1.5 );
				light.position.set( 0, 500, 2000 );
				light.castShadow = true;

				light.shadow = new THREE.LightShadow( new THREE.PerspectiveCamera( 50, 1, 200, 10000 ) );
				light.shadow.bias = - 0.00022;

				light.shadow.mapSize.width = 2048;
				light.shadow.mapSize.height = 2048;

				scene.add( light );

				/*
				var geometry = new THREE.BoxBufferGeometry(  100+Math.random()*200,  100+Math.random()*200,  100+Math.random() *200 );
				
				
				mesh = new THREE.Mesh( geometry );
				mesh.position.set(Math.random()*100,  Math.random()*100,  Math.random() *100);
				scene.add( mesh );
				
				
				*/

				
				
				renderer = new THREE.WebGLRenderer( { antialias: true } );
				
				renderer.setPixelRatio( window.devicePixelRatio );
				renderer.setSize(icanvas.offsetWidth,icanvas.offsetHeight);
				
				renderer.shadowMap.enabled = true;
				renderer.shadowMap.type = THREE.PCFShadowMap;

				if(icanvas.firstChild != null){
					
					icanvas.removeChild(icanvas.firstChild);
				}
				icanvas.appendChild( renderer.domElement );


			}
			
			function adddd(){
				var geometry = new THREE.BoxGeometry( 40, 40, 40 );
						var object = new THREE.Mesh( geometry, new THREE.MeshLambertMaterial( { color: Math.random() * 0xffffff } ) );

					object.position.x = Math.random() * 1000 - 500;
					object.position.y = Math.random() * 600 - 300;
					object.position.z = Math.random() * 800 - 400;

					object.rotation.x = Math.random() * 2 * Math.PI;
					object.rotation.y = Math.random() * 2 * Math.PI;
					object.rotation.z = Math.random() * 2 * Math.PI;

					object.scale.x = Math.random() * 2 + 1;
					object.scale.y = Math.random() * 2 + 1;
					object.scale.z = Math.random() * 2 + 1;

					object.castShadow = true;
					object.receiveShadow = true;

					scene.add( object );

					objects.push( object );
					
					num=num+1;
			}
			
			function animate() {

				requestAnimationFrame( animate );
				for ( var i = 0; i < num; i ++ ) {
					
				objects[i].rotation.x += 0.005;
				objects[i].rotation.y += 0.01;
				}
				/*
				mesh.rotation.x += 0.005;
				mesh.rotation.y += 0.01;
				*/
				renderer.render( scene, camera );

			}