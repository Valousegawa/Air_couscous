/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: tools/HardCodedParameters.java 2015-03-11 buixuan.
 * ******************************************************/
package tools;

public class HardCodedParameters {
	// ---HARD-CODED-PARAMETERS---//
	public static String defaultParamFileName = "in.parameters";
	public static final int defaultWidth = 800, defaultHeight = 600, heroesStartX = 150, heroesStartY = 400,
			heroesWidth = 21, heroesHeight = 31, heroesStep = 20, ennemyWidth = 21, ennemyHeight = 31,
			phantomStep = 10, bonusWidth = 10, bonusHeight = 20;
	public static final int enginePaceMillis = 100, spriteSlowDownRate = 7, objectif = 30;
	public static final int maxWidth = 820, phantomWidth = 30,
			phantomHeight = 30;
	public static final int mapWidth = 540, mapHeight = 600, mapPositionX = 10, mapPositionY = 60;
	public static final int barSepWidth = 20, barSepHeight = 685, sepPositionX = 295, sepPositionY = 10;
	public static final int jailWidth = 200, jailHeight = 200, jailPositionX = 20, jailPositionY = 80;
	public static final int objectifWidth = 250, objectifHeight = 65, scoreWidth = 80, scoreHeight = 60, timeWidth = 80,
			timeHeight = 60;
	public static final int powerWidth = 420, powerHeight = 20, powerPositionX = 38, powerPositionY = 15;
	public static final int winWidth = 300, winHeight = 300, winPositionX = 60, winPositionY = 50;
	public static final int gameOverWidth = 200, gameOverHeight = 200, gameOverPositinX = 60, gameOverPositionY = 50;
	public static final double friction = 0.50;
	public static final double resolutionShrinkFactor = 0.95, userBarShrinkFactor = 0.25, menuBarShrinkFactor = 0.5,
			logBarShrinkFactor = 0.15, logBarCharacterShrinkFactor = 0.1175,
			logBarCharacterShrinkControlFactor = 0.01275, menuBarCharacterShrinkFactor = 0.175;
	public static final int displayZoneXStep = 5, displayZoneYStep = 5, displayZoneXZoomStep = 5,
			displayZoneYZoomStep = 5;
	public static final double displayZoneAlphaZoomStep = 0.98;

	// ---MISCELLANOUS---//
	public static final Object loadingLock = new Object();
	public static final String greetingsZoneId = String.valueOf(0xED1C7E), simulatorZoneId = String.valueOf(0x51E77E);
	public static final int Nbr_obstacle = 23;
	public static final double[] allObstacleX = {50,50,50,50,50,170,350,470,470,470,470,85,255,420,470,420,320,245,185,310,240,210,330};
	public static final double[] allObstacleY = {100,100,170,320,440,420,457,355,315,270,225,225,345,430,150,100,110,110,190,225,235,325,295};
	public static final double[] allObstacleWidth = {62,74,35,100,120,87,30,30,30,30,30,30,30,100,35,87,85,85,55,40,66,90,75};;
	public static final double[] allObstacleHeight = {175,125,355,105,55,78,48,48,48,48,48,48,40,100,55,117,45,45,85,30,120,33,80};

	public static <T> T instantiate(final String className, final Class<T> type) {
		try {
			return type.cast(Class.forName(className).newInstance());
		} catch (final InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (final ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
}
