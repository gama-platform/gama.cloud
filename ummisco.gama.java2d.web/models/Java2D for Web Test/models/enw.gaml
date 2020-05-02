model TestJava2D
global {
	file shp<-file("../includes/test.shp");
	image_file my_icon <- image_file("../includes/sheep.png");
// 	file shp<-file("../includes/landuse_myxuyen_2005_region.shp");
// 	geometry shape<-envelope(shp);
	init { 
// 		create parcel from:shp;
		create aa number:100;
        create parcel{
            shape<-rectangle(world.shape.width,world.shape.height) at_location {world.shape.width/2,world.shape.height/2};
        }
	}
} 
species aa skills:[moving]{
	float size<-1.0;
    reflex ss{
        do wander speed:2.0;
    }

	aspect icon {
		draw my_icon size:2 * size;
	}
    aspect default{
        draw circle(size) color:#red;
    }
}
species parcel { 
}
grid cell width:100 height:100{
    
	float max_food <- 1.0;
	float food_prod <- rnd(0.01);
	float food <- rnd(1.0) max: max_food update: food + food_prod;
	rgb color <- rgb(int(255 * (1 - food)), 255, int(255 * (1 - food))) update: rgb(int(255 * (1 - food)), 255, int(255 * (1 - food)));
	reflex ss{
	    color <- rgb(rnd(255));//rgb(int(255 * (1 - food)), 255, int(255 * (1 - food))) update: rgb(int(255 * (1 - food)), 255, int(255 * (1 - food)));
	}
}
experiment TestExp type: gui autorun:true {
 
	output { 
		display o type: java2D synchronized:true { 
		    grid cell;
			 //species parcel{
			 //    draw shape color:rgb(rnd(255),rnd(255),rnd(255));
			 //}
		    species aa  aspect: icon;
		  //species parcel;
		}
	}
}
