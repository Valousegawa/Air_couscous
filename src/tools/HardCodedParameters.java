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
	public static final int defaultWidth = 800, defaultHeight = 600, heroesStartX = 20, heroesStartY = 500,
			heroesWidth = 21, heroesHeight = 31, heroesStep = 20, ennemyWidth = 21, ennemyHeight = 31,
			phantomStep = 10, bonusWidth = 10, bonusHeight = 20;
	public static final int enginePaceMillis = 100, spriteSlowDownRate = 7, objectif = 30;
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
	public static final int Nbr_obstacle = 10;
	public static final double[] allObstacleX = new double[Nbr_obstacle];
	public static final double[] allObstacleY = new double[Nbr_obstacle];
	
	public static final double[] allObstacleWidth = new double[Nbr_obstacle];
	public static final double[] allObstacleHeight = new double[Nbr_obstacle];

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
