model TestJava2D
global {
	file shp<-file("../includes/test.shp");
// 	file shp<-file("../includes/landuse_myxuyen_2005_region.shp");
	geometry shape<-envelope(shp);
	init { 
		create parcel from:shp;
	}
}

species parcel { 
}

experiment TestExp type: gui autorun:true {

	output {
		layout #split navigator:false editors:false;
		display o type: java2D synchronized:true {
			 species parcel{
			     draw shape color:rgb(rnd(255),rnd(255),rnd(255));
			 }
		}
	}
}
