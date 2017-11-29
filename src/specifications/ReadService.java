/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ReadService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Obstacle;
import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface ReadService {
	public Position getHeroesPosition();

	public double getHeroesWidth();

	public double getHeroesHeight();

	public double getEnnemyWidth();

	public double getEnnemyHeight();
	
	public double getBonusWidth();

	public double getBonusHeight();

	public int getStepNumber();

	public int getScore();

	public ArrayList<ElementService> getEnnemies();
	
	public ArrayList<ElementService> getBonus();

	public Sound.SOUND getSoundEffect();
	
	public ArrayList<Obstacle> getObstacles();
	
	public int getBonusValue();
}
