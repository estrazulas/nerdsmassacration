package nerdSpack;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.GameObject;
import gui.TelaInicialGUI;
import java.awt.Dimension;

public class NerdsMassacrationMain extends GameEngine {
	public static final int modoJogo = 1;
	public static final int modoGUI = 0;

	public GameObject getGame(int GameID) {
		switch (GameID) {
		case 1:
			return new NerdsMassacration(this);
		case 0:
			return new TelaInicialGUI(this);
		}

		return null;
	}

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new NerdsMassacrationMain(), new Dimension(640, 480), false);
		game.start();
	}

	public NerdsMassacrationMain() {
		distribute = true;
	}
}
