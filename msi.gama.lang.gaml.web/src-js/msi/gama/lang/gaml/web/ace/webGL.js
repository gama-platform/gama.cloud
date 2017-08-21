
//Get the height and the width of the window
var ww = window.innerWidth,
    wh = window.innerHeight;

function init(){

    /* WEBGL RENDERER */

    //Create the webGl renderer from Three
    renderer = new THREE.WebGLRenderer({canvas : document.getElementById('scene')});
    //Set the background of our scene
    renderer.setClearColor(0x3F3F3F);
    //Set the size of my renderer, I want it to be fullscreen
    //renderer.setSize(ww,wh);
	renderer.setSize(800,750);

    /* SCENE */

    //Create my scene
    scene = new THREE.Scene();


    /* CAMERA */

    //Create a new Perspective Camera with four parameters
    //Check this video for more information : https://www.youtube.com/watch?t=35&v=KyTaxN2XUyQ
    camera = new THREE.PerspectiveCamera(50, 800/750, 1, 10000);
    //We set our camera at x:0,y:0 and z:500
    camera.position.set(0, 0, 500);
    //And finally we add our camera in our scene
    scene.add(camera);


    /* LIGHT */

    //Create a white 'directional light'
    light = new THREE.DirectionalLight(0xffffff, 1);

    //We the position of our light
    light.position.set( 50, 250, 500 );

    //We add our light into the scene
    scene.add(light);


    //Create our cube
    createBox();


    //This is very important, it will ask the renderer to render our scene
    renderer.render(scene,camera);

};

function colorConvertor(r,g,b) {
	
}

function createBox(){

    /* The Cube */

    //We create a boxGeometry with three parameters: width, height and depth
    geometry = new THREE.BoxGeometry(200,200,200);

    //We create a texture for our cube, here it's only a red texture
    texture = new THREE.MeshLambertMaterial({color:"rgb(255, 0, 0)"});
	
    //We create the Mesh which contains our geometry (the cube) and its texture
    cube = new THREE.Mesh(geometry, texture);
    //Rotate a bit the cube to see it's in 3D (don't dorget it's in radian)
    cube.rotation.y = Math.PI/4;
    cube.rotation.x = Math.PI/4;

    //And finally we add the cube to our scene
    scene.add(cube);

};

//Init our scene
init();