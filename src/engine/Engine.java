/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;

import specifications.EngineService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.ElementService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService {
	private static final double friction = HardCodedParameters.friction, heroesStep = HardCodedParameters.heroesStep,
			phantomStep = HardCodedParameters.phantomStep;
	private Timer engineClock;
	private DataService data;
	private User.COMMAND command;
	private Random gen;
	private boolean moveLeft, moveRight, moveUp, moveDown;

	public Engine() {
	}

	@Override
	public void bindDataService(DataService service) {
		data = service;
	}

	@Override
	public void init() {
		engineClock = new Timer();
		command = User.COMMAND.NONE;
		gen = new Random();
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
	}

	@Override
	public void start() {
		engineClock.schedule(new TimerTask() {
			public void run() {

				if (gen.nextInt(10) < 3) {
					spawnEnnemies();
				}
				
				updatePositionHeroes();

				ArrayList<ElementService> ennemies = new ArrayList<ElementService>();
				int score = 0;

				data.setSoundEffect(Sound.SOUND.None);

				for (ElementService p : data.getEnnemies()) {
					if (p.getAction() == ElementService.MOVE.LEFT) {
						moveLeft(p);
					}
					if (p.getAction() == ElementService.MOVE.RIGHT) {
						moveRight(p);
					}
					if (p.getAction() == ElementService.MOVE.UP) {
						moveUp(p);
					}
					if (p.getAction() == ElementService.MOVE.DOWN) {
						moveDown(p);
					}

					if (heroesCapturedEnnemies(p)) {
						data.setSoundEffect(Sound.SOUND.HeroesGotHit);
						score++;
					} else {
						if (p.getPosition().x > 0)
							ennemies.add(p);
					}
				}

				data.addScore(score);

				data.setBonus(ennemies);

				data.setStepNumber(data.getStepNumber() + 1);
			}
		}, 0, HardCodedParameters.enginePaceMillis);
	}

	@Override
	public void stop() {
		engineClock.cancel();
	}

	@Override
	public void setHeroesCommand(User.COMMAND c) {
		if (c == User.COMMAND.LEFT) {
			moveLeft = true;
		}
		if (c == User.COMMAND.RIGHT) {
			moveRight = true;
		}
		if (c == User.COMMAND.UP) {
			moveUp = true;
		}
		if (c == User.COMMAND.DOWN) {
			moveDown = true;
		}
	}

	@Override
	public void releaseHeroesCommand(User.COMMAND c) {
		if (c == User.COMMAND.LEFT) {
			moveLeft = false;
		}
		if (c == User.COMMAND.RIGHT) {
			moveRight = false;
		}
		if (c == User.COMMAND.UP) {
			moveUp = false;
		}
		if (c == User.COMMAND.DOWN) {
			moveDown = false;
		}
	}

	//TODO
	private void updatePositionHeroes() {
		data.setHeroesPosition(
				new Position(data.getHeroesPosition().x + 1, data.getHeroesPosition().y + 2));
	}

	private void spawnEnnemies() {
		int x = (int) (HardCodedParameters.defaultWidth * .9);
		int y = 0;
		boolean cont = true;
		while (cont) {
			y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6))
					+ HardCodedParameters.defaultHeight * .1);
			cont = false;
			for (ElementService p : data.getEnnemies()) {
				if (p.getPosition().equals(new Position(x, y)))
					cont = true;
			}
		}
		data.addEnnemy(new Position(x, y));
	}

	//TODO
	private void spawnBonus() {
		int x = (int) (HardCodedParameters.defaultWidth * .9);
		int y = 0;
		boolean cont = true;
		while (cont) {
			y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6))
					+ HardCodedParameters.defaultHeight * .1);
			cont = false;
			for (ElementService p : data.getBonus()) {
				if (p.getPosition().equals(new Position(x, y)))
					cont = true;
			}
		}
		data.addBonus(new Position(x, y));
	}

	//TODO
	private void moveLeft(ElementService p) {
		p.setPosition(new Position(p.getPosition().x - phantomStep, p.getPosition().y));
	}

	private void moveRight(ElementService p) {
		p.setPosition(new Position(p.getPosition().x + phantomStep, p.getPosition().y));
	}

	private void moveUp(ElementService p) {
		p.setPosition(new Position(p.getPosition().x, p.getPosition().y - phantomStep));
	}

	private void moveDown(ElementService p) {
		p.setPosition(new Position(p.getPosition().x, p.getPosition().y + phantomStep));
	}

	private boolean heroesCapturedEnnemies(ElementService p) {
		return ((data.getHeroesPosition().x - p.getPosition().x) * (data.getHeroesPosition().x - p.getPosition().x)
				+ (data.getHeroesPosition().y - p.getPosition().y)
						* (data.getHeroesPosition().y - p.getPosition().y) < 0.25
								* (data.getHeroesWidth() + data.getEnnemyWidth())
								* (data.getHeroesWidth() + data.getEnnemyWidth()));
	}

	private boolean heroesCapturedEnnemies() {
		for (ElementService p : data.getEnnemies())
			if (heroesCapturedEnnemies(p)) {
				return true;
			}
		return false;
	}
}
