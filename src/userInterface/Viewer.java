/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import tools.HardCodedParameters;

import specifications.ViewerService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.ElementService;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public class Viewer implements ViewerService, RequireReadService {
	private static final int spriteSlowDownRate = HardCodedParameters.spriteSlowDownRate;
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
	private ImageView heroesAvatar;
	private Image heroesSpriteSheet;
	private ArrayList<Rectangle2D> heroesAvatarViewports;
	private ArrayList<Integer> heroesAvatarXModifiers;
	private ArrayList<Integer> heroesAvatarYModifiers;
	private int heroesAvatarViewportIndex;
	private double xShrink, yShrink, shrink, xModifier, yModifier, heroesScale;
	private String path;

	public Viewer() {
	}

	@Override
	public void bindReadService(ReadService service) {
		data = service;
	}

	@Override
	public void init() {
		xShrink = 1;
		yShrink = 1;
		xModifier = 0;
		yModifier = 0;

		// Yucky hard-conding
		heroesSpriteSheet = new Image("file:src/images/modern soldier large.png");
		heroesAvatar = new ImageView(heroesSpriteSheet);
		heroesAvatarViewports = new ArrayList<Rectangle2D>();
		heroesAvatarXModifiers = new ArrayList<Integer>();
		heroesAvatarYModifiers = new ArrayList<Integer>();

		heroesAvatarViewportIndex = 0;

		// TODO: readapt with our sprites
		heroesAvatarViewports.add(new Rectangle2D(570, 194, 115, 190));
		heroesAvatarViewports.add(new Rectangle2D(398, 386, 133, 192));
		heroesAvatarViewports.add(new Rectangle2D(155, 194, 147, 190));
		heroesAvatarViewports.add(new Rectangle2D(785, 386, 127, 194));
		heroesAvatarViewports.add(new Rectangle2D(127, 582, 135, 198));
		heroesAvatarViewports.add(new Rectangle2D(264, 582, 111, 200));
		heroesAvatarViewports.add(new Rectangle2D(2, 582, 123, 198));
		heroesAvatarViewports.add(new Rectangle2D(533, 386, 115, 192));
		// heroesAvatarViewports.add(new Rectangle2D(204,386,95,192));

		// heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);
		heroesAvatarXModifiers.add(6);
		heroesAvatarYModifiers.add(-6);
		heroesAvatarXModifiers.add(2);
		heroesAvatarYModifiers.add(-8);
		heroesAvatarXModifiers.add(1);
		heroesAvatarYModifiers.add(-10);
		heroesAvatarXModifiers.add(1);
		heroesAvatarYModifiers.add(-13);
		heroesAvatarXModifiers.add(5);
		heroesAvatarYModifiers.add(-15);
		heroesAvatarXModifiers.add(5);
		heroesAvatarYModifiers.add(-13);
		heroesAvatarXModifiers.add(0);
		heroesAvatarYModifiers.add(-9);
		heroesAvatarXModifiers.add(0);
		heroesAvatarYModifiers.add(-6);
		// heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);

	}

	// TODO: readapt to our project
	@Override
	public Parent getPanel() {
		shrink = Math.min(xShrink, yShrink);
		xModifier = .01 * shrink * defaultMainWidth;
		yModifier = .01 * shrink * defaultMainHeight;

		// Path for power progess bar
		switch (data.getScore()) {
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

		Group panel = new Group();
		panel.getChildren().addAll(map, bar, jail, score, objectif, power);

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
