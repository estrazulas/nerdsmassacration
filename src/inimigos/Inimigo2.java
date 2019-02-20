package inimigos;

import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.sprite.AdvanceSprite;
import nerdSpack.NerdsMassacration;

public class Inimigo2 extends AdvanceSprite {
	public static final int ESQUERDA = 1;
	public static final int DIREITA = 2;
	public static final int ANDANDO = 1;
	Timer tempAndando;
	Timer tempAndando2;
	NerdsMassacration proprio;

	public Inimigo2(NerdsMassacration proprio) {
		super(proprio.getImages("imagens/inimigo3.png", 2, 2), 0.0D, 0.0D);
		this.proprio = proprio;

		tempAndando = new Timer(400);
		tempAndando2 = new Timer(300);
	}

	protected void animationChanged(int oldStat, int oldDir, int estado, int direcao) {
		switch (direcao) {
		case 2:
			setAnimationFrame(0, 1);
			break;
		case 1:
			setAnimationFrame(2, 3);
		}

		switch (estado) {
		case 1:
			setAnimate(true);
			setLoopAnim(true);
			getAnimationTimer().setDelay(400L);
		}
	}

	public void update(long _tempoPercorrido) {
		addVerticalSpeed(_tempoPercorrido, 0.002D, 0.5D);
		super.update(_tempoPercorrido);
	}

	public void movimento(long _tempoPercorrido) {
		if (tempAndando2.action(_tempoPercorrido)) {
			setHorizontalSpeed(-0.2D);
			setDirection(1);
			setStatus(1);
		}
		if (tempAndando.action(_tempoPercorrido)) {
			setHorizontalSpeed(0.2D);
			setDirection(2);
			setStatus(1);
		}
	}
}
