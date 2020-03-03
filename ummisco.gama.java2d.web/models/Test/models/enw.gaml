model TestEquations
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

experiment NewModel type: gui {

	output {
		
		display o type: java2D {
			 species parcel;
		}
	}
}
