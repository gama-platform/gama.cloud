var icanvas;

			var container, stats;
			var camera, controls, scene, renderer;
			var objects = [];
function WebGLJS(element)  {
	
	
   this.div = document.createElement("div");    
   this.div.className = 'webgljs';    
   icanvas = this.div; 
   element.appendChild(icanvas);
   
   
   
			init();
			animate();
   this.appendInfo = function(text)  {        
   

   
			init();
			animate();
        

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
				scene.background = new THREE.Color( 0xf0f0f0 );

				scene.add( new THREE.AmbientLight( 0x505050 ) );

				var light = new THREE.SpotLight( 0xffffff, 1.5 );
				light.position.set( 0, 500, 2000 );
				light.castShadow = true;

				light.shadow = new THREE.LightShadow( new THREE.PerspectiveCamera( 50, 1, 200, 10000 ) );
				light.shadow.bias = - 0.00022;

				light.shadow.mapSize.width = 2048;
				light.shadow.mapSize.height = 2048;

				scene.add( light );

				var geometry = new THREE.BoxGeometry( 40, 40, 40 );

				for ( var i = 0; i < 200; i ++ ) {

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

				}

				renderer = new THREE.WebGLRenderer( { antialias: true } );
				renderer.setPixelRatio( window.devicePixelRatio );
				renderer.setSize(icanvas.offsetWidth,icanvas.offsetHeight);

				renderer.shadowMap.enabled = true;
				renderer.shadowMap.type = THREE.PCFShadowMap;

				
				
				if(icanvas.firstChild != null){
					
					icanvas.removeChild(icanvas.firstChild);
				}
				icanvas.appendChild( renderer.domElement );
				

				/*
				var dragControls = new THREE.DragControls( objects, camera, renderer.domElement );
				dragControls.addEventListener( 'dragstart', function ( event ) { controls.enabled = false; } );
				dragControls.addEventListener( 'dragend', function ( event ) { controls.enabled = true; } );
				*/
				

				//

				

			}

			
			//

			function animate() {

				requestAnimationFrame( animate );

				render();
				//stats.update();

			}

			function render() {

				//controls.update();
			

				renderer.render( scene, camera );

			}