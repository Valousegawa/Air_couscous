/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import specifications.ElementService;
import specifications.EngineService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.ViewerService;
import tools.HardCodedParameters;

public class Viewer implements ViewerService, RequireReadService {
	private static final double defaultMainWidth = HardCodedParameters.defaultWidth,
			defaultMainHeight = HardCodedParameters.defaultHeight;
	private static final double mapWidth = HardCodedParameters.mapWidth, mapHeight = HardCodedParameters.mapHeight,
			mapPositionX = HardCodedParameters.mapPositionX, mapPositionY = HardCodedParameters.mapPositionY;
	private static final double barSepWidth = HardCodedParameters.barSepWidth,
			barSepHeight = HardCodedParameters.barSepHeight, sepPositionX = HardCodedParameters.sepPositionX,
			sepPositionY = HardCodedParameters.sepPositionY;
	private static final double jailWidth = HardCodedParameters.jailWidth, jailHeight = HardCodedParameters.jailHeight,
			jailPositionX = HardCodedParameters.jailPositionX, jailPositionY = HardCodedParameters.jailPositionY;
	private static final double objectifWidth = HardCodedParameters.objectifWidth,
			objectifHeight = HardCodedParameters.objectifHeight, scoreWidth = HardCodedParameters.scoreWidth,
			scoreHeight = HardCodedParameters.scoreHeight, timeWidth = HardCodedParameters.timeWidth,
			timeHeight = HardCodedParameters.timeHeight;
	private static final double powerWidth = HardCodedParameters.powerWidth,
			powerHeight = HardCodedParameters.powerHeight, powerPositionX = HardCodedParameters.powerPositionX,
			powerPositionY = HardCodedParameters.powerPositionY;
	private ReadService data;
	private EngineService engine;
	private ImageView heroesAvatar, bonusAvatar, ennemyAvatar;
	private Image heroesSpriteSheet, bonus, ennemySpriteSheet;
	private ArrayList<Rectangle2D> heroesAvatarViewports, ennemyAvatarViewports;
	private double xShrink, yShrink, shrink, xModifier, yModifier, heroesScale, ennemyScale;

	private String path;

	public Viewer() {
	}

	@Override
	public void bindReadService(ReadService service, EngineService engines) {
		data = service;
		engine = engines;
	}

	@Override
	public void init() {
		xShrink = 1;
		yShrink = 1;
		xModifier = 0;
		yModifier = 0;

		heroesSpriteSheet = new Image(engine.getSprite());
		heroesAvatar = new ImageView(heroesSpriteSheet);
		
		ennemySpriteSheet = new Image("file:src/images/arab.png");
		
		bonus = new Image("file:src/images/AFI.png");
		
		heroesAvatarViewports = new ArrayList<Rectangle2D>();

		heroesAvatarViewports.add(new Rectangle2D(4, 1, 25, 31));
		heroesAvatarViewports.add(new Rectangle2D(35, 0, 27, 32));
		heroesAvatarViewports.add(new Rectangle2D(68, 0, 25, 32));
		heroesAvatarViewports.add(new Rectangle2D(5, 33, 127, 194));
		heroesAvatarViewports.add(new Rectangle2D(37, 32, 22, 32));
		heroesAvatarViewports.add(new Rectangle2D(69, 33, 22, 31));
		heroesAvatarViewports.add(new Rectangle2D(5, 67, 22, 31));
		heroesAvatarViewports.add(new Rectangle2D(37, 64, 22, 32));
		heroesAvatarViewports.add(new Rectangle2D(69, 65, 22, 31));
		heroesAvatarViewports.add(new Rectangle2D(5, 97, 22, 31));
		heroesAvatarViewports.add(new Rectangle2D(37, 96, 23, 32));
		heroesAvatarViewports.add(new Rectangle2D(69, 97, 23, 31));
		
		
		ennemyAvatarViewports = new ArrayList<Rectangle2D>();
		
		ennemyAvatarViewports.add(new Rectangle2D(4, 1, 25, 31));
		ennemyAvatarViewports.add(new Rectangle2D(35, 0, 27, 32));
		ennemyAvatarViewports.add(new Rectangle2D(68, 0, 25, 32));
		ennemyAvatarViewports.add(new Rectangle2D(5, 33, 127, 194));
		ennemyAvatarViewports.add(new Rectangle2D(37, 32, 22, 32));
		ennemyAvatarViewports.add(new Rectangle2D(69, 33, 22, 31));
		ennemyAvatarViewports.add(new Rectangle2D(5, 67, 22, 31));
		ennemyAvatarViewports.add(new Rectangle2D(37, 64, 22, 32));
		ennemyAvatarViewports.add(new Rectangle2D(69, 65, 22, 31));
		ennemyAvatarViewports.add(new Rectangle2D(5, 97, 22, 31));
		ennemyAvatarViewports.add(new Rectangle2D(37, 96, 23, 32));
		ennemyAvatarViewports.add(new Rectangle2D(69, 97, 23, 31));
	}

	@Override
	public Parent getPanel() {
		Group panel = new Group();
		
		// Path for power progess bar
		switch (data.getBonusValue()) {
		case 0:
			path = "power.png";
			break;
		case 10:
			path = "power10.png";
			break;
		case 20:
			path = "power20.png";
			break;
		case 30:
			path = "power30.png";
			break;
		case 40:
			path = "power40.png";
			break;
		case 50:
			path = "power50.png";
			break;
		case 60:
			path = "power60.png";
			break;
		case 70:
			path = "power70.png";
			break;
		case 80:
			path = "power80.png";
			break;
		case 90:
			path = "power90.png";
			break;
		case 100:
			path = "power100.png";
			break;
		}

		// Map
		Rectangle map = new Rectangle(-2 * xModifier + shrink * mapWidth,
				-.2 * shrink * mapHeight + shrink * mapHeight);
		Image imgBackground = new Image("/images/map.png");
		map.setFill(new ImagePattern(imgBackground));
		map.setX(mapPositionX);
		map.setY(mapPositionY);
		map.setStroke(Color.rgb(240, 235, 230));
		map.setStrokeWidth(.01 * shrink * defaultMainHeight);
		map.setArcWidth(.04 * shrink * defaultMainHeight);
		map.setArcHeight(.04 * shrink * defaultMainHeight);
		map.setTranslateX(xModifier);
		map.setTranslateY(yModifier);

		// Separation bar
		Rectangle bar = new Rectangle(13 * shrink * powerPositionY + .5 * shrink * powerPositionX,
				-0.05 * shrink * powerPositionX + shrink * powerPositionY, -2 * xModifier + shrink * barSepWidth,
				-.2 * shrink * barSepHeight + shrink * barSepHeight);
		Image imgBar = new Image("/images/bar.png");
		bar.setFill(new ImagePattern(imgBar));
		bar.setTranslateX(xModifier + sepPositionX * xShrink);
		bar.setTranslateY(yModifier + sepPositionY * yShrink);

		// Jail
		Rectangle jail = new Rectangle(7.2 * shrink * jailPositionY + .5 * shrink * jailPositionX,
				-0.05 * shrink * jailPositionX + shrink * jailPositionY, -2 * xModifier + shrink * jailWidth,
				-.2 * shrink * jailHeight + shrink * jailHeight);
		Image Imagejail = new Image("/images/prison.png");
		jail.setFill(new ImagePattern(Imagejail));
		jail.setTranslateX(xModifier + jailPositionX * xShrink);
		jail.setTranslateY(yModifier + jailPositionY * yShrink);

		// Power
		Rectangle power = new Rectangle(-2 * xModifier + shrink * powerWidth,
				-.2 * shrink * powerHeight + shrink * powerHeight);
		Image ImagePower = new Image("/images/" + path);
		power.setFill(new ImagePattern(ImagePower));
		power.setX(powerPositionX);
		power.setY(powerPositionY);
		power.setTranslateX(xModifier + powerPositionX * xShrink);
		power.setTranslateY(yModifier + powerPositionY * yShrink);

		// Objectifs
		Text objectif = new Text(10.3 * shrink * objectifHeight + .5 * shrink * objectifWidth,
				-0.05 * shrink * objectifWidth + shrink * objectifHeight, "Objectif");
		objectif.setFont(new Font(.2 * shrink * objectifHeight));

		// Score
		Text score = new Text(10.3 * shrink * scoreHeight + .5 * shrink * scoreWidth,
				-0.05 * shrink * scoreWidth + shrink * scoreHeight, "Score " + data.getScore());
		score.setFont(new Font(.2 * shrink * scoreHeight));
		
		panel.getChildren().addAll(map, bar, jail, score, objectif, power);
		
		heroesSpriteSheet = new Image(engine.getSprite());
		heroesAvatar = new ImageView(heroesSpriteSheet);
		shrink = Math.min(xShrink, yShrink);
		xModifier = .01 * shrink * defaultMainWidth;
		yModifier = .01 * shrink * defaultMainHeight;

		heroesScale = data.getHeroesHeight() * shrink / heroesAvatarViewports.get(engine.getPressedDirection()).getHeight();
		heroesAvatar.setViewport(heroesAvatarViewports.get(engine.getPressedDirection()));
		heroesAvatar.setFitHeight(data.getHeroesHeight() * shrink);
		heroesAvatar.setPreserveRatio(true);
		heroesAvatar.setTranslateX(shrink * data.getHeroesPosition().x + shrink * xModifier
				+ -heroesScale * 0.5 * heroesAvatarViewports.get(engine.getPressedDirection()).getWidth()
				+ shrink * heroesScale);
		heroesAvatar.setTranslateY(shrink * data.getHeroesPosition().y + shrink * yModifier
				+ -heroesScale * 0.5 * heroesAvatarViewports.get(engine.getPressedDirection()).getHeight()
				+ shrink * heroesScale);
		
		Text textBonus = new Text(-0.1 * shrink * defaultMainHeight + .5 * shrink * defaultMainWidth,
				-0.05 * shrink * defaultMainWidth + shrink * defaultMainHeight, "bonus: " + data.getBonusValue());
		textBonus.setFont(new Font(.05 * shrink * defaultMainHeight));
		
		Text timer = new Text(-0.1 * shrink * defaultMainHeight + .5 * shrink * defaultMainWidth,
				-0.01 * shrink * defaultMainWidth + shrink * defaultMainHeight, String.format("%d:%d", 
					    TimeUnit.MILLISECONDS.toMinutes((long) engine.getTimer()),
					    TimeUnit.MILLISECONDS.toSeconds((long) engine.getTimer()) - 
					    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) engine.getTimer()))
					));
		textBonus.setFont(new Font(.05 * shrink * defaultMainHeight));

		ArrayList<ElementService> bonuses = data.getBonus();
		ElementService b;

		for (int i = 0; i < bonuses.size(); i++) {
			b = bonuses.get(i);
			double radius = .5 * Math.min(shrink * data.getBonusWidth(), shrink * data.getBonusHeight());
			bonusAvatar = new ImageView(bonus);
			bonusAvatar.setEffect(new Lighting());
			bonusAvatar.setPreserveRatio(true);
			bonusAvatar.setFitHeight(data.getBonusHeight() * shrink);
			bonusAvatar.setTranslateX(shrink * b.getPosition().x + shrink * xModifier - radius);
			bonusAvatar.setTranslateY(shrink * b.getPosition().y + shrink * yModifier - radius);
			panel.getChildren().add(bonusAvatar);
		}
		
		ArrayList<ElementService> ennemies = data.getEnnemies();
		ElementService e;
		
		for (int i = 0; i < ennemies.size(); i++) {
			e = ennemies.get(i);
			ennemyAvatar = new ImageView(ennemySpriteSheet);
			ennemyScale = data.getEnnemyHeight() * shrink / ennemyAvatarViewports.get(e.getDirection()).getHeight();
			ennemyAvatar.setViewport(ennemyAvatarViewports.get(e.getDirection()));
			ennemyAvatar.setFitHeight(data.getEnnemyHeight() * shrink);
			ennemyAvatar.setPreserveRatio(true);
			ennemyAvatar.setTranslateX(shrink * e.getPosition().x + shrink * xModifier
					+ -ennemyScale * 0.5 * ennemyAvatarViewports.get(e.getDirection()).getWidth()
					+ shrink * ennemyScale);
			ennemyAvatar.setTranslateY(shrink * e.getPosition().y + shrink * yModifier
					+ -ennemyScale * 0.5 * ennemyAvatarViewports.get(e.getDirection()).getHeight()
					+ shrink * ennemyScale);
			panel.getChildren().add(ennemyAvatar);
		}
		
		panel.getChildren().addAll(heroesAvatar, textBonus, timer);
		return panel;
	}

	@Override
	public void setMainWindowWidth(double width) {
		xShrink = width / defaultMainWidth;
	}

	@Override
	public void setMainWindowHeight(double height) {
		yShrink = height / defaultMainHeight;
	}
}
