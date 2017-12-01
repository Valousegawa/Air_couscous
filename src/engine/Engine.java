/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import javafx.animation.AnimationTimer;
import specifications.DataService;
import specifications.ElementService;
import specifications.EngineService;
import specifications.RequireDataService;
import tools.HardCodedParameters;
import tools.Obstacle;
import tools.Position;
import tools.Sound;
import tools.User;

public class Engine implements EngineService, RequireDataService {
	private static final double heroesStep = HardCodedParameters.heroesStep;
	private Timer engineClock;
	private DataService data;
	private User.COMMAND command;
	private Random gen;
	private boolean moveLeft, moveRight, moveUp, moveDown, ennemyStunned;
	private int actualDir;
	private ArrayList<ElementService> foundEnnemy = new ArrayList<ElementService>();
	private ArrayList<ElementService> foundBonus = new ArrayList<ElementService>();
	private double speed;
	private int bonusTimer;
	private String sprite;
	private double timer;
	private int winLose = -1;
	private Clip clipMusic;
	private boolean soundPlayed;

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
		ennemyStunned = false;
		actualDir = 7;
		speed = 15d;
		bonusTimer = 0;
		sprite = "file:src/images/jmlp.png";
		timer = 240000;
		soundPlayed = false;
	}

	@Override
	public void start() {
		try {
			playMusic();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		engineClock.schedule(new TimerTask() {
			public void run() {
				if (!gameState()) {
					if (data.getBonus().size() < 1 && data.getBonusValue() < 100) {
						if (gen.nextInt(10) < 2) {
							spawnBonus();
						}
					}

					updatePositionHeroes();

					if (data.getEnnemies().size() < 3) {
						spawnEnnemy();
					}

					for (ElementService p : data.getEnnemies()) {
						if (!ennemyStunned) {
							if (p.getAction() == ElementService.MOVE.LEFT) {
								if (!ennemyCollisonObstacle(p, 4)) {
									moveLeft(p);
								}
							}
							if (p.getAction() == ElementService.MOVE.RIGHT) {
								if (!ennemyCollisonObstacle(p, 3)) {
									moveRight(p);
								}
							}
							if (p.getAction() == ElementService.MOVE.UP) {
								if (!ennemyCollisonObstacle(p, 2)) {
									moveUp(p);
								}
							}
							if (p.getAction() == ElementService.MOVE.DOWN) {
								if (!ennemyCollisonObstacle(p, 1)) {
									moveDown(p);
								}
							}
						}

						if (heroColisionItems(p)) {
							try {
								playCaptureSound();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							foundEnnemy.add(p);
							data.addScore(1);
						}
					}

					data.getEnnemies().removeAll(foundEnnemy);
					foundEnnemy = new ArrayList<ElementService>();

					for (ElementService p : data.getBonus()) {
						if (heroColisionItems(p)) {
							try {
								playItemSound();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
							foundBonus.add(p);
							data.addBonusValue(10);
						}
					}

					data.getBonus().removeAll(foundBonus);
					foundBonus = new ArrayList<ElementService>();

					if (bonusTimer != 0) {
						bonusTimer -= 100;
					} else {
						ennemyStunned = false;
						speed = 15d;
						sprite = "file:src/images/jmlp.png";
						data.setSoundEffect(Sound.SOUND.None);
					}

					timer -= 100;
					data.setSoundEffect(Sound.SOUND.None);
				} else {
					if (!soundPlayed) {
						try {
							playVictory();
							playVictory2();
							soundPlayed = true;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						
					}
				}
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
			moveRight = false;
			moveUp = false;
			moveDown = false;
		}
		if (c == User.COMMAND.RIGHT) {
			moveLeft = false;
			moveRight = true;
			moveUp = false;
			moveDown = false;
		}
		if (c == User.COMMAND.UP) {
			moveLeft = false;
			moveRight = false;
			moveUp = true;
			moveDown = false;
		}
		if (c == User.COMMAND.DOWN) {
			moveLeft = false;
			moveRight = false;
			moveUp = false;
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

	private void updatePositionHeroes() {
		if (moveDown) {
			if (!heroCollisonObstacle(1)) {
				moveHeroeDown(data);
			}
		} else if (moveUp) {
			if (!heroCollisonObstacle(2)) {
				moveHeroeUp(data);
			}
		} else if (moveRight) {
			if (!heroCollisonObstacle(3)) {
				moveHeroeRight(data);
			}
		} else if (moveLeft) {
			if (!heroCollisonObstacle(4)) {
				moveHeroeLeft(data);
			}
		}
	}

	private void spawnBonus() {
		int x = (int) (HardCodedParameters.defaultWidth * .9);
		int y = (int) (HardCodedParameters.defaultHeight * .9);
		boolean cont = true;
		while (cont) {
			y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6))
					+ HardCodedParameters.defaultHeight * .1);
			x = (int) (gen.nextInt((int) (HardCodedParameters.defaultWidth * .6))
					+ HardCodedParameters.defaultWidth * .1);

			cont = false;
			for (ElementService p : data.getBonus()) {
				if (p.getPosition().equals(new Position(x, y))) {
					cont = true;
				}
			}
			if (y > HardCodedParameters.mapHeight - 100 || y < HardCodedParameters.mapPositionY + 50
					|| x > HardCodedParameters.mapWidth - 50 || x < HardCodedParameters.mapPositionX + 50) {
				cont = true;
			}
		}
		data.addBonus(new Position(x, y));
	}

	private void spawnEnnemy() {
		int x = (int) (HardCodedParameters.defaultWidth * .9);
		int y = (int) (HardCodedParameters.defaultHeight * .9);
		boolean cont = true;
		while (cont) {
			y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6))
					+ HardCodedParameters.defaultHeight * .1);
			x = (int) (gen.nextInt((int) (HardCodedParameters.defaultWidth * .6))
					+ HardCodedParameters.defaultWidth * .1);

			cont = false;
			for (ElementService p : data.getEnnemies()) {
				if (p.getPosition().equals(new Position(x, y))) {
					cont = true;
				}
			}
			if (y > HardCodedParameters.mapHeight - 100 || y < HardCodedParameters.mapPositionY + 50
					|| x > HardCodedParameters.mapWidth - 50 || x < HardCodedParameters.mapPositionX + 50) {
				cont = true;
			}
		}
		data.addEnnemy(new Position(x, y));
	}

	private void moveLeft(ElementService p) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_X;
			double carSpeedX;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_X = p.getPosition().x;
					carSpeedX = 10d;
				}

				double time = (now - startTime) / 2E9d;
				p.setDirection(4);
				p.setPosition(new Position(p.getPosition().x -= carSpeedX * time, p.getPosition().y));
				if (initCarPosition_X >= p.getPosition().x + HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveRight(ElementService p) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_X;
			double carSpeedX;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_X = p.getPosition().x;
					carSpeedX = 10d;
				}

				double time = (now - startTime) / 2E9d;
				p.setDirection(7);
				p.setPosition(new Position(p.getPosition().x += carSpeedX * time, p.getPosition().y));
				if (initCarPosition_X <= p.getPosition().x - HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveUp(ElementService p) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_Y;
			double carSpeedY;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_Y = p.getPosition().y;
					carSpeedY = 10d;
				}

				double time = (now - startTime) / 2E9d;
				p.setDirection(10);
				p.setPosition(new Position(p.getPosition().x, p.getPosition().y -= carSpeedY * time));
				if (initCarPosition_Y >= p.getPosition().y + HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveDown(ElementService p) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_Y;
			double carSpeedY;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_Y = p.getPosition().y;
					carSpeedY = 10d;
				}

				double time = (now - startTime) / 2E9d;
				p.setDirection(1);
				p.setPosition(new Position(p.getPosition().x, p.getPosition().y += carSpeedY * time));
				if (initCarPosition_Y <= p.getPosition().y - HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private boolean heroColisionItems(ElementService p) {
		return ((data.getHeroesPosition().x - p.getPosition().x) * (data.getHeroesPosition().x - p.getPosition().x)
				+ (data.getHeroesPosition().y - p.getPosition().y)
						* (data.getHeroesPosition().y - p.getPosition().y) < 0.25
								* (data.getHeroesWidth() + data.getEnnemyWidth())
								* (data.getHeroesWidth() + data.getEnnemyWidth()));
	}

	private boolean heroCollisionItems() {
		for (ElementService p : data.getEnnemies())
			if (heroColisionItems(p)) {
				return true;
			}
		return false;
	}

	@Override
	public int getPressedDirection() {
		if (moveDown) {
			actualDir = 1;
			return 1;
		} else if (moveUp) {
			actualDir = 10;
			return 10;
		} else if (moveRight) {
			actualDir = 7;
			return 7;
		} else if (moveLeft) {
			actualDir = 4;
			return 4;
		} else {
			return actualDir;
		}
	}

	private void moveHeroeUp(DataService data) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_Y;
			double carSpeedY;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_Y = data.getHeroesPosition().y;
					carSpeedY = speed;
				}

				double time = (now - startTime) / 1E9d;
				data.setHeroesPosition(
						new Position(data.getHeroesPosition().x, data.getHeroesPosition().y -= carSpeedY * time));
				if (initCarPosition_Y >= data.getHeroesPosition().y + HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveHeroeDown(DataService data) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_Y;
			double carSpeedY;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_Y = data.getHeroesPosition().y;
					carSpeedY = speed;
				}

				double time = (now - startTime) / 1E9d;
				data.setHeroesPosition(
						new Position(data.getHeroesPosition().x, data.getHeroesPosition().y += carSpeedY * time));
				if (initCarPosition_Y <= data.getHeroesPosition().y - HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveHeroeLeft(DataService data) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_X;
			double carSpeedX;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_X = data.getHeroesPosition().x;
					carSpeedX = speed;
				}

				double time = (now - startTime) / 1E9d;
				data.setHeroesPosition(
						new Position(data.getHeroesPosition().x -= carSpeedX * time, data.getHeroesPosition().y));
				if (initCarPosition_X >= data.getHeroesPosition().x + HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private void moveHeroeRight(DataService data) {
		new AnimationTimer() {
			long startTime = -1;
			double initCarPosition_X;
			double carSpeedX;

			@Override
			public void handle(long now) {
				if (startTime == -1) {
					startTime = now;
					initCarPosition_X = data.getHeroesPosition().x;
					carSpeedX = speed;
				}

				double time = (now - startTime) / 1E9d;
				data.setHeroesPosition(
						new Position(data.getHeroesPosition().x += carSpeedX * time, data.getHeroesPosition().y));
				if (initCarPosition_X <= data.getHeroesPosition().x - HardCodedParameters.heroesStep) {
					stop();
				}
			}
		}.start();
	}

	private boolean ennemyCollisonObstacle(ElementService p, int dir) {
		switch (dir) {
		case 1:
			if (p.getPosition().y + heroesStep < HardCodedParameters.mapHeight - 100) {
				return false;
			} else {
				return true;
			}
		case 2:
			if (p.getPosition().y - heroesStep > HardCodedParameters.mapPositionY + 50) {
				return false;
			} else {
				return true;
			}
		case 3:
			if (p.getPosition().x + heroesStep < HardCodedParameters.mapWidth - 50) {
				return false;
			} else {
				return true;
			}
		case 4:
			if (p.getPosition().x - heroesStep > HardCodedParameters.mapPositionX + 50) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean heroCollisonObstacle(int dir) {
		switch (dir) {
		case 1:
			if (data.getHeroesPosition().y + heroesStep < HardCodedParameters.mapHeight - 100) {
				return false;
			} else {
				return true;
			}
		case 2:
			if (data.getHeroesPosition().y - heroesStep > HardCodedParameters.mapPositionY + 50) {
				return false;
			} else {
				return true;
			}
		case 3:
			if (data.getHeroesPosition().x + heroesStep < HardCodedParameters.mapWidth - 50) {
				return false;
			} else {
				return true;
			}
		case 4:
			if (data.getHeroesPosition().x - heroesStep > HardCodedParameters.mapPositionX + 50) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void bonus_activated() {
		try {
			playBonusSound();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		switch (data.getBonusValue()) {
		case 30:
		case 40:
		case 50:
			bonusTimer = 3000;
			ennemyStunned = true;
			data.addBonusValue(-30);
			sprite = "file:src/images/mlp.png";
			data.setSoundEffect(Sound.SOUND.bonus1);
			break;
		case 60:
		case 70:
		case 80:
		case 90:
			bonusTimer = 3000;
			speed = 30d;
			data.addBonusValue(-60);
			sprite = "file:src/images/mmlp.png";
			data.setSoundEffect(Sound.SOUND.bonus2);
			break;
		case 100:
			bonusTimer = 3000;
			ennemyStunned = true;
			speed = 25d;
			data.resetBonusValue();
			sprite = "file:src/images/ja.png";
			data.setSoundEffect(Sound.SOUND.bonus3);
			break;
		default:
			break;
		}

	}

	@Override
	public String getSprite() {
		return sprite;
	}

	@Override
	public double getTimer() {
		return timer;
	}

	@Override
	public boolean gameState() {
		if (data.getScore() == HardCodedParameters.objectif && timer != 0) {
			winLose = 1;
			clipMusic.stop();
			return true;
		} else if (data.getScore() != HardCodedParameters.objectif && timer == 0) {
			winLose = 0;
			return true;
		}
		return false;
	}

	public synchronized void playItemSound() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/get.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;
					Clip clip;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playCaptureSound() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/prison.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;
					Clip clip;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playBonusSound() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/bonus3.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;
					Clip clip;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playMusic() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/music.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clipMusic = (Clip) AudioSystem.getLine(info);
					clipMusic.open(stream);
					clipMusic.loop(Clip.LOOP_CONTINUOUSLY);
					Thread.sleep(10000);
					clipMusic.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playVictory() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/win.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;
					Clip clip;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playVictory2() throws FileNotFoundException {
		new Thread(new Runnable() {
			public void run() {
				try {
					File yourFile = new File("src/sound/win2.wav");
					AudioInputStream stream;
					AudioFormat format;
					DataLine.Info info;
					Clip clip;

					stream = AudioSystem.getAudioInputStream(yourFile);
					format = stream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					clip = (Clip) AudioSystem.getLine(info);
					clip.open(stream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	@Override
	public int getWinLose() {
		return winLose;
	}

}
