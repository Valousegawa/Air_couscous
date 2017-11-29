/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/ia/MoveLeftPhantom.java 2015-03-11 buixuan.
 * ******************************************************/
package data.ia;

import tools.Position;

import java.util.Random;

import specifications.ElementService;

public class MoveEnnemies implements ElementService {
	private Position position;
	private Random r;
	private int dir;

	public MoveEnnemies(Position p) {
		position = p;
		r = new Random();
		dir = 4;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public ElementService.MOVE getAction() {
		switch (r.nextInt(4)) {
		case 0:
			return ElementService.MOVE.LEFT;
		case 1:
			return ElementService.MOVE.RIGHT;
		case 2:
			return ElementService.MOVE.DOWN;
		default:
			return ElementService.MOVE.UP; 
		}
	}

	@Override
	public void setPosition(Position p) {
		position = p;
	}

	@Override
	public void setDirection(int i) {
		dir = i;
		
	}

	@Override
	public int getDirection() {
		return dir;
	}
}
