/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import java.util.ArrayList;

import data.ia.MoveEnnemies;
import specifications.DataService;
import specifications.ElementService;
import tools.HardCodedParameters;
import tools.Obstacle;
import tools.Position;
import tools.Sound;

public class Data implements DataService {
	private Position heroesPosition;
	private int stepNumber, score, bonus_value;
	private ArrayList<ElementService> ennemies;
	private ArrayList<ElementService> bonus;
	private ArrayList<Obstacle> obstacles;
	private double heroesWidth, heroesHeight, ennemyWidth, ennemyHeight, bonusWidth, bonusHeight;
	private Sound.SOUND sound;

	public Data() {
	}

	@Override
	public void init() {
		heroesPosition = new Position(HardCodedParameters.heroesStartX, HardCodedParameters.heroesStartY);
		ennemies = new ArrayList<ElementService>();
		bonus = new ArrayList<ElementService>();
		obstacles = new Obstacle().getObstacles();
		
		stepNumber = 0;
		score = 0;
		bonus_value = 0;
		heroesWidth = HardCodedParameters.heroesWidth;
		heroesHeight = HardCodedParameters.heroesHeight;
		ennemyWidth = HardCodedParameters.ennemyWidth;
		ennemyHeight = HardCodedParameters.ennemyHeight;
		bonusWidth = HardCodedParameters.bonusWidth;
		bonusHeight = HardCodedParameters.bonusHeight;
		sound = Sound.SOUND.None;
	}

	@Override
	public Position getHeroesPosition() {
		return heroesPosition;
	}

	@Override
	public double getHeroesWidth() {
		return heroesWidth;
	}

	@Override
	public double getHeroesHeight() {
		return heroesHeight;
	}

	@Override
	public double getEnnemyWidth() {
		return ennemyWidth;
	}

	@Override
	public double getEnnemyHeight() {
		return ennemyHeight;
	}

	@Override
	public int getStepNumber() {
		return stepNumber;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public ArrayList<ElementService> getEnnemies() {
		return ennemies;
	}

	@Override
	public Sound.SOUND getSoundEffect() {
		return sound;
	}

	@Override
	public void setHeroesPosition(Position p) {
		heroesPosition = p;
	}

	@Override
	public void setStepNumber(int n) {
		stepNumber = n;
	}

	@Override
	public void addScore(int score) {
		this.score += score;
	}

	@Override
	public void addEnnemy(Position p) {
		ennemies.add(new MoveEnnemies(p));
	}

	@Override
	public void setBonus(ArrayList<ElementService> bonus) {
		this.bonus = bonus;
	}

	@Override
	public void setSoundEffect(Sound.SOUND s) {
		sound = s;
	}

	@Override
	public double getBonusWidth() {
		return bonusWidth;
	}

	@Override
	public double getBonusHeight() {
		return bonusHeight;
	}

	@Override
	public ArrayList<ElementService> getBonus() {
		return bonus;
	}

	@Override
	public void addBonus(Position p) {
		bonus.add(new MoveEnnemies(p));
		
	}

	@Override
	public void setEnnemies(ArrayList<ElementService> ennemies) {
		this.ennemies = ennemies;
		
	}

	@Override
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	@Override
	public int getBonusValue() {
		return bonus_value;
	}

	@Override
	public void addBonusValue(int value) {
		bonus_value += value;
		
	}

	@Override
	public void resetBonusValue() {
		bonus_value = 0;
	}
}
