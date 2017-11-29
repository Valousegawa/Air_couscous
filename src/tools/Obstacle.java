package tools;

import java.util.ArrayList;

public class Obstacle {
	private Position p;
	private double width;
	private double height;
	private ArrayList<Obstacle> obstacles;
	
	public Obstacle() {
		obstacles = new ArrayList<Obstacle>();
		for(int i = 0; i < HardCodedParameters.Nbr_obstacle; i++) {
			p = new Position(HardCodedParameters.allObstacleX[i], HardCodedParameters.allObstacleY[i]);
			width = HardCodedParameters.allObstacleWidth[i];
			height = HardCodedParameters.allObstacleHeight[i];
			
			obstacles.add(new Obstacle(p, width, height));
		}
	}
	
	public Obstacle(Position p, double width, double height) {
		this.p = p;
		this.width = width;
		this.height = height;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public Position getPosition() {
		return p;
	}


	public double getWidth() {
		return width;
	}


	public double getHeight() {
		return height;
	}
}