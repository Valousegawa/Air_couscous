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
import specifications.EngineService;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Viewer implements ViewerService, RequireReadService {
	private static final double defaultMainWidth = HardCodedParameters.defaultWidth,
			defaultMainHeight = HardCodedParameters.defaultHeight;
	private ReadService data;
	private EngineService engine;
	private ImageView heroesAvatar, bonusAvatar, ennemyAvatar;
	private Image heroesSpriteSheet, bonus, ennemySpriteSheet;
	private ArrayList<Rectangle2D> heroesAvatarViewports, ennemyAvatarViewports;
	private double xShrink, yShrink, shrink, xModifier, yModifier, heroesScale, ennemyScale;

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
		
		Text score = new Text(-0.1 * shrink * defaultMainHeight + .5 * shrink * defaultMainWidth,
				-0.1 * shrink * defaultMainWidth + shrink * defaultMainHeight, "Score : " + data.getScore());
		score.setFont(new Font(.05 * shrink * defaultMainHeight));

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

		Group panel = new Group();
		panel.getChildren().addAll(heroesAvatar, score, textBonus, timer);

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
