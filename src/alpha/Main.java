/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: alpha/Main.java 2015-03-11 buixuan.
 * ******************************************************/
package alpha;

import tools.HardCodedParameters;
import tools.User;
import tools.Sound;

import specifications.DataService;
import specifications.EngineService;
import specifications.ViewerService;
import specifications.AlgorithmService;

import data.Data;
import engine.Engine;
import userInterface.Viewer;
//import algorithm.RandomWalker;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {
	// ---HARD-CODED-PARAMETERS---//
	private static String fileName = HardCodedParameters.defaultParamFileName;

	// ---VARIABLES---//
	private static DataService data;
	private static EngineService engine;
	private static ViewerService viewer;
	private static AnimationTimer timer;

	// ---EXECUTABLE---//
	public static void main(String[] args) {
		// readArguments(args);

		data = new Data();
		engine = new Engine();
		viewer = new Viewer();

		((Engine) engine).bindDataService(data);
		((Viewer) viewer).bindReadService(data, engine);

		data.init();
		engine.init();
		viewer.init();

		launch(args);
	}

	@Override
	public void start(Stage stage) {
		final Scene scene = new Scene(((Viewer) viewer).getPanel());

		scene.setFill(Color.rgb(240, 235, 230));
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT)
					engine.setHeroesCommand(User.COMMAND.LEFT);
				if (event.getCode() == KeyCode.RIGHT)
					engine.setHeroesCommand(User.COMMAND.RIGHT);
				if (event.getCode() == KeyCode.UP)
					engine.setHeroesCommand(User.COMMAND.UP);
				if (event.getCode() == KeyCode.DOWN)
					engine.setHeroesCommand(User.COMMAND.DOWN);
				if (event.getCode() == KeyCode.SPACE)
					engine.bonus_activated();
				event.consume();
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT)
					engine.releaseHeroesCommand(User.COMMAND.LEFT);
				if (event.getCode() == KeyCode.RIGHT)
					engine.releaseHeroesCommand(User.COMMAND.RIGHT);
				if (event.getCode() == KeyCode.UP)
					engine.releaseHeroesCommand(User.COMMAND.UP);
				if (event.getCode() == KeyCode.DOWN)
					engine.releaseHeroesCommand(User.COMMAND.DOWN);
				event.consume();
			}
		});
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				viewer.setMainWindowWidth(newSceneWidth.doubleValue());
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				viewer.setMainWindowHeight(newSceneHeight.doubleValue());
			}
		});

		stage.setScene(scene);
		stage.setWidth(HardCodedParameters.maxWidth);
		stage.setHeight(HardCodedParameters.defaultHeight);
		stage.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				engine.start();
			}
		});
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				engine.stop();
			}
		});
		stage.show();

		timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				scene.setRoot(((Viewer) viewer).getPanel());
				System.out.println(data.getSoundEffect());
				switch (data.getSoundEffect()) {
				case bonus1:
					new MediaPlayer(new Media(getHostServices().getDocumentBase() + "src/sound/bonus3.mp3")).play();
					break;
				case bonus2:
					new MediaPlayer(new Media(getHostServices().getDocumentBase() + "src/sound/bonus3.mp3")).play();
					break;
				case bonus3:
					new MediaPlayer(new Media(getHostServices().getDocumentBase() + "src/sound/bonus3.mp3")).play();
					break;
				default:
					break;
				}
			}
		};
		timer.start();
	}
}
