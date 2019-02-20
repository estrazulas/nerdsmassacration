package nerdSpack;

import java.awt.image.BufferedImage;

import com.golden.gamedev.object.AnimatedSprite;

public class TiroAnimado extends AnimatedSprite {
	private static final long serialVersionUID = 1L;
	private int fireDir;

	public TiroAnimado(Nerd nerd, BufferedImage[] tiroDireita12, BufferedImage[] tiroEsquerda1) {
		fireDir = nerd.getDirection();
		if (fireDir == 1) {
			setImages(tiroEsquerda1);
			setAnimate(true);
			setLoopAnim(true);
			getAnimationTimer().setDelay(100L);
			setLocation(nerd.getX() - 10.0D, nerd.getY() + 6.0D);
			setHorizontalSpeed(-0.4D);

		} else if (fireDir == 2) {
			setImages(tiroDireita12);
			setAnimate(true);
			setLoopAnim(true);
			getAnimationTimer().setDelay(100L);
			setLocation(nerd.getX() + 10.0D, nerd.getY() + 6.0D);
			setHorizontalSpeed(0.4D);
		}
	}

	public void update(long elapsedTime) {
		super.update(elapsedTime);
	}
}
