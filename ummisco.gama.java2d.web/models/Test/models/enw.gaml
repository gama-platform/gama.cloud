model TestEquations
global {
	file shp<-file("../includes/test1.shp");
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
