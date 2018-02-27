/**
* Name: Circle
* Author: 
* Description: This model shows the movement of cells trying to do a circle shape with themselves 
* 	considering the other cells. The second experiment shows a bigger circle using more cell agents.
* Tags: skill
*/
model circle_model


global
{
//Number of agents to create
	int number_of_agents min: 1 <- 100;
	//Radius of the circle that the cells will make
	int radius_of_circle min: 10 <- 500;
	//Repulsion strength of one cell to the others
	int repulsion_strength min: 1 <- 5;
	//Size of the environment
	int width_and_height_of_environment min: 10 <- 3000;
	//Range of the agents
	//Speed of the agents
	float speed_of_agents min: 0.1 <- 2.0;
	//Size of the agents
	int size_of_agents <- 50;
	int range_of_agents min: 1 <- size_of_agents * 2;
	//Center of the considered circle created by the cells
	point center1 <- { width_and_height_of_environment / 2, width_and_height_of_environment / 2 };
	list<point> cc <- [center1];
	geometry shape <- square(width_and_height_of_environment);
	init
	{
		loop times: 2
		{
			point nc;
			bool ok <- false;
			loop while: !ok
			{
				nc <- { rnd(width_and_height_of_environment), rnd(width_and_height_of_environment) };
				ok <- true;
				loop cen over: cc
				{
					if (distance_to(nc, cen) < (radius_of_circle * 2) or (nc.x < radius_of_circle) or (nc.y < radius_of_circle) or (nc.x > width_and_height_of_environment - radius_of_circle) or
					(nc.y > width_and_height_of_environment - radius_of_circle))
					{
						ok <- false;
					}

				}

			}

			cc <+ nc;
		}
		//Creation of the cell agents
		create cell number: number_of_agents
		{
			center <- center1;
		}

	}

	reflex sss
	{
		if (length(cell where (each.count > 200)) > 50)
		{
			ask (cell where (each.count > 200))
			{
				center <- cc[rnd(length(cc) - 1)];
				count <- 0;
			}

		}

	}

}

//Species cell which represents the cell agents, using the skill moving
species cell skills: [moving]
{
	point center;
	//Color of the cell, randomly chosen
	rgb color const: true <- [100 + rnd(155), 100 + rnd(155), 100 + rnd(155)] as rgb;
	//Size of the cell
	float size const: true <- float(size_of_agents);
	//Range of the cell
	float range const: true <- float(range_of_agents);
	//Speed of the cell
	float speed const: true <- speed_of_agents;
	//Heading of the cell, the direction it 'watches'
	int heading <- rnd(359);

	//Reflex to make the cell agent fo to the center, calling the derivated action move
	reflex go_to_center
	{
		heading <- (((self distance_to center) > radius_of_circle) ? self towards center : (self towards center) - 180);
		
		do move speed: speed;
	}

	int count <- 0;
	//Reflex to flee of the other cells agents, which will help to design the circle shape
	reflex flee_others
	{
		cell close <- one_of(((self neighbors_at range) of_species cell) sort_by (self distance_to each));
		if close != nil
		{
			heading <- (self towards close) - 180;
			float dist <- self distance_to close;
			do move speed: dist / repulsion_strength heading: heading;
			do wander speed: dist / repulsion_strength;
		}

		int nn <- length((self neighbors_at (range * 1.5)) of_species cell);
		if (nn > 4)
		{
			count <- count + 1;
		}

	}

	aspect default
	{
		draw sphere(size) color: color;
	}

}

experiment main type: gui
{
	output
	{
		display Circle
		{
			species cell;
		}

	}

}
