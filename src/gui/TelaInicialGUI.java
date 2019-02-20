package gui;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.engine.BaseAudio;
import com.golden.gamedev.engine.BaseInput;
import com.golden.gamedev.engine.audio.JavaLayerMp3Renderer;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TLabel;
import com.golden.gamedev.gui.TPanel;
import com.golden.gamedev.gui.toolkit.FrameWork;
import com.golden.gamedev.object.GameFontManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;

public class TelaInicialGUI extends GameObject {
	private FrameWork frame;
	private Color bgColor = new Color(255, 128, 64);

	public TelaInicialGUI(GameEngine parent) {
		super(parent);
	}

	public void initResources() {
		bsMusic.setBaseRenderer(new JavaLayerMp3Renderer());

		setMaskColor(Color.GREEN);
		frame = new FrameWork(bsInput, getWidth(), getHeight());

		TButton btn3 = new TButton("LOG IN NERD...", 100, 100, 150, 50) {
			public void doAction() {
				playSound("sons/heart.wav");
				parent.nextGameID = 1;
				finish();
			}
		};
		frame.add(btn3);

		TPanel caixaDeInformacao = new TPanel(460, 0, 180, 480);
		TLabel texto = new TLabel(
				"\n\n\nNerds Massacration\n==============\nDesenvolvido por: \n\n- Daniel S Estrazulas\n- Juliana Erckmann Effting\n- Soraia Goede\n==============\nEngine Utilizada: \n\n- GTGE\n- GTGE ADDONS\n- GTGE GUI\n",
				480, 10, 400, 350);

		texto.UIResource().put("Text Color", Color.RED.brighter());
		texto.UIResource().put("Text Font", fontManager.getFont(new Font("Verdana", 0, 11)));

		frame.add(caixaDeInformacao);
		frame.add(texto);
	}

	public void render(Graphics2D g) {
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		frame.render(g);
	}

	public void update(long arg0) {
		switch (bsInput.getKeyPressed()) {
		case 27:
			finish();
		}

		frame.update();
	}
}
