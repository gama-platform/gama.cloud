/**
* Name: Life
* Author: 
* Description: A model using a cellular automata to represent the Game of Life, the most famous 
* 	example of cellular automata. Each cell will see if the number of living neighbours meets the 
* 	condition to emerge or to live.
* Tags: grid
*/
model life

//Declare the world as a torus or not torus environment
global torus: torus_environment {
	//Size of the environment
	int environment_width <- 200 min: 10 max: 1000;
	int environment_height <- 200 min: 10 max: 1000;
	bool parallel <- true;
	//Declare as torus or not
	bool torus_environment <- true;
	//Density 
	int density <- 25 min: 1 max: 99;
	//Conditions to live
	list<int> living_conditions <- [2, 3];
	//Conditions to birth
	list<int> birth_conditions <- [3];
	//Color for living cells
	rgb livingcolor <- °white;
	//Color for dying cells
	rgb dyingcolor <- °red;
	//Color for emerging cells
	rgb emergingcolor <- °orange;
	//Color for dead cells
	rgb deadcolor <- °black;
	//Shape of the environment
	geometry shape <- rectangle(environment_width, environment_height);
	
	//Initialization of the model by writing the description of the model in the console
	init {
	}
	
	//Ask at each life_cell to evolve and update
	reflex generation {
		// The computation is made in parallel
		ask life_cell parallel: parallel {
			do evolve;
		}
	}
	

}

//Grid species representing a cellular automata
grid life_cell width: environment_width height: environment_height neighbors: 8  use_individual_shapes: false use_regular_agents: false 
use_neighbors_cache: false parallel: parallel{
	//Boolean to know if it is the new state of the cell
	bool new_state;
	//List of all the neighbours
	list<life_cell> neighbours <- self neighbors_at 1;
	//Boolean  to know if it is a living or dead cell
	bool alive <- (rnd(100)) < density;
	
	rgb color <- alive ? livingcolor : deadcolor;
	
	//Action to evolve the cell considering its neighbours
	action evolve {
		//Count the number of living neighbours of the cells
		int living <- neighbours count each.alive;
		if alive {
			//If the number of living respect the conditions, the cell is still alive
			new_state <- living in living_conditions;
			color <- new_state ? livingcolor : dyingcolor;
		} else {
			//If the number of living meets the conditions, the cell go to born
			new_state <- living in birth_conditions;
			color <- new_state ? emergingcolor : deadcolor;
		}

	}
	//Action to update the new state of the cell
	reflex update {
		alive <- new_state;
	}

}

experiment "batch exp" type:batch until:cycle>10{
	
}
experiment "Game of Life" type: gui {
	output {
		display Life {
			grid life_cell;
		}

	}

}
