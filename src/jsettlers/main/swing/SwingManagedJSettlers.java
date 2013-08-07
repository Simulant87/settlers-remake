package jsettlers.main.swing;

import go.graphics.area.Area;
import go.graphics.nativegl.NativeAreaWindow;
import go.graphics.swing.AreaContainer;
import go.graphics.swing.sound.SwingSoundPlayer;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import jsettlers.common.CommonConstants;
import jsettlers.common.resources.ResourceManager;
import jsettlers.graphics.JSettlersScreen;
import jsettlers.graphics.map.draw.ImageProvider;
import jsettlers.graphics.startscreen.StartScreen;
import jsettlers.graphics.startscreen.interfaces.IStartScreen;
import jsettlers.graphics.swing.SwingResourceLoader;
import jsettlers.graphics.swing.SwingResourceProvider;
import jsettlers.newmain.StartScreenConnector;

public class SwingManagedJSettlers {

	static { // sets the native library path for the system dependent jogl libs
		SwingResourceLoader.setupSwingPaths();
	}

	/**
	 * @param args
	 *            args can have no entries or <br>
	 *            args[0] must be "host" or "client"
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		List<String> argsList = Arrays.asList(args);

		loadDebugSettings(argsList);

		ResourceManager.setProvider(new SwingResourceProvider());
		JSettlersScreen content = startGui(argsList);
		generateContent(new StartScreenConnector(), content);

		ImageProvider.getInstance().startPreloading();
	}

	private static void loadDebugSettings(List<String> argsList) {
		if (argsList.contains("--control-all")) {
			CommonConstants.ENABLE_ALL_PLAYER_FOG_OF_WAR = true;
			CommonConstants.ENABLE_ALL_PLAYER_SELECTION = true;
		}
		if (argsList.contains("-localhost")) {
			CommonConstants.DEFAULT_SERVER_ADDRESS = "localhost";
		}
	}

	/**
	 * Creates a new SWING GUI for the game.
	 * 
	 * @param argsList
	 * @return
	 */
	public static JSettlersScreen startGui(List<String> argsList) {
		Area area = new Area();
		JSettlersScreen content = new JSettlersScreen(new SwingSoundPlayer());
		area.add(content.getRegion());

		if (argsList.contains("--force-jogl")) {
			startJogl(area);
		} else if (argsList.contains("--force-native")) {
			startNative(area);
		} else {
			try {
				startNative(area);
			} catch (Throwable t) {
				startJogl(area);
			}
		}

		startRedrawTimer(content);
		return content;
	}

	private static void generateContent(IStartScreen startScreen,
			JSettlersScreen content) {
		content.setContent(new StartScreen(startScreen, content));
	}

	private static void startRedrawTimer(final JSettlersScreen content) {
		new Timer("opengl-redraw").schedule(new TimerTask() {
			@Override
			public void run() {
				content.getRegion().requestRedraw();
			}
		}, 100, 25);
	}

	private static void startJogl(Area area) {
		SwingResourceLoader.setupSwingPaths();

		JFrame jsettlersWnd = new JFrame("jsettlers");
		AreaContainer panel = new AreaContainer(area);
		panel.setPreferredSize(new Dimension(640, 480));
		jsettlersWnd.add(panel);
		panel.requestFocusInWindow();

		jsettlersWnd.pack();
		jsettlersWnd.setSize(1200, 800);
		jsettlersWnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jsettlersWnd.setVisible(true);
		jsettlersWnd.setLocationRelativeTo(null);
	}

	private static void startNative(Area area) {
		new NativeAreaWindow(area);
	}
}
