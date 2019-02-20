package nerdSpack;

import java.awt.image.BufferedImage;

import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.Timer;

public class CaboDeRedeAnimado extends AnimatedSprite {
	private static final long serialVersionUID = 1L;
	private Nerd nerd;
	private int direcaoDoBoneco;

	public CaboDeRedeAnimado(Nerd nerd, BufferedImage[] caboRedeEsquerda, BufferedImage[] caboRedeDireita) {
		this.nerd = nerd;
		direcaoDoBoneco = nerd.getDirection();

		if (direcaoDoBoneco == 1) {
			setImages(caboRedeEsquerda);
			getAnimationTimer().setDelay(200L);
			setAnimate(true);
		} else if (direcaoDoBoneco == 2) {
			setImages(caboRedeDireita);
			getAnimationTimer().setDelay(200L);
			setAnimate(true);
		}
	}

	public void update(long elapsedTime) {
		super.update(elapsedTime);

		if ((!isAnimate()) || (direcaoDoBoneco != nerd.getDirection())) {
			setActive(false);
		}

		setX(nerd.getX() + (direcaoDoBoneco == 1 ? -50 : 25));
		setY(nerd.getY() + 3.0D);
	}
}
