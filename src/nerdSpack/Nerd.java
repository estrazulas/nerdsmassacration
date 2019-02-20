package nerdSpack;

import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.sprite.AdvanceSprite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Nerd extends AdvanceSprite {
	private static final long serialVersionUID = 1L;
	public static final int PARADO = 1;
	public static final int ANDANDO = 2;
	public static final int ESQUERDA = 1;
	public static final int DIREITA = 2;
	boolean pulando;
	Timer temporizadorDePulo;
	Timer temporizadorDeTiro;
	BufferedImage[] tiroDireita1;
	BufferedImage[] tiroEsquerda1;
	BufferedImage[] caboRedeDireita;
	BufferedImage[] caboRedeEsquerda;
	NerdsMassacration proprio;
	AnimatedSprite nerdAtack;
	SpriteGroup teste;

	public boolean isTemChave() {
		return temChave;
	}

	String arma1 = "Disquete";

	public void setTemChave(boolean temChave) {
		this.temChave = temChave;
	}

	String arma2 = "C-Rede";
	boolean temChave = false;

	boolean temArma2;

	double vSpeedPulo = -0.7D;

	private int armaAtual;
	int pontos;
	int vida;

	public int getPontos() {
		return pontos;
	}

	public void setPontos(int pontos) {
		this.pontos = pontos;
	}

	public int getVida() {
		return vida;
	}

	public void somaVida(int mais) {
		vida += mais;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public void ativaTemporizadorPulo() {
		temporizadorDePulo.setActive(true);
	}

	public Nerd(NerdsMassacration proprio) {
		super(proprio.getImages("imagens/nerdFigura1.png", 3, 2), 0.0D, 0.0D);

		this.proprio = proprio;
		pontos = 0;
		temChave = false;
		vida = 300;

		tiroDireita1 = proprio.getImages("imagens/dir1.png", 4, 1);
		tiroEsquerda1 = proprio.getImages("imagens/esq1.png", 4, 1);
		caboRedeDireita = proprio.getImages("imagens/cabotest.png", 6, 1);
		caboRedeEsquerda = proprio.getImages("imagens/cabotestEsq.png", 6, 1);

		temporizadorDePulo = new Timer(150);
		temporizadorDeTiro = new Timer(400);
		resetarEstado();
	}

	public void resetarEstado() {
		armaAtual = 1;
		temChave = false;

		setAnimation(1, 2);
		setSpeed(0.0D, 0.0D);
		pulando = false;
		temporizadorDeTiro.setActive(false);
	}

	protected void animationChanged(int oldStat, int oldDir, int estado, int direcao) {
		switch (direcao) {
		case 2:
			setAnimationFrame(0, 1);
			break;
		case 1:
			setAnimationFrame(3, 5);
		}

		switch (estado) {
		case 1:
			setLoopAnim(false);
			break;
		case 2:
			setAnimate(true);
			setLoopAnim(true);
			getAnimationTimer().setDelay(200L);
		}

	}

	public void update(long _tempoPercorrido) {
		if ((temporizadorDeTiro.isActive()) && (temporizadorDeTiro.action(_tempoPercorrido))) {

			temporizadorDeTiro.setActive(false);
		}

		addVerticalSpeed(_tempoPercorrido, 0.002D, 0.5D);

		detectaEventoDoTeclado(_tempoPercorrido);
		super.update(_tempoPercorrido);
	}

	private void detectaEventoDoTeclado(long tempoPercorrido) {
		if (proprio.keyDown(37)) {
			addHorizontalSpeed(tempoPercorrido, -0.002D, -0.2D);
			setDirection(1);
			setStatus(2);
		} else if (proprio.keyDown(39)) {
			addHorizontalSpeed(tempoPercorrido, 0.002D, 0.2D);
			setDirection(2);
			setStatus(2);

		} else if (getHorizontalSpeed() > 0.0D) {
			addHorizontalSpeed(tempoPercorrido, -0.005D, 0.0D);
		} else if (getHorizontalSpeed() < 0.0D) {
			addHorizontalSpeed(tempoPercorrido, 0.005D, 0.0D);
		} else {
			setStatus(1);
		}

		if ((proprio.keyPressed(38)) && (!pulando)) {
			pulando = true;
			setVerticalSpeed(vSpeedPulo);
		}

		if ((proprio.keyPressed(17)) && (!temporizadorDeTiro.isActive())) {

			temporizadorDeTiro.setActive(true);

			switch (armaAtual) {
			case 1:
				proprio.grupoTiroNerd.add(new TiroAnimado(this, tiroDireita1,tiroEsquerda1));
				proprio.playSound("sons/son1.wav");
				break;
			case 2:
				proprio.grupoTiroNerd.add(new CaboDeRedeAnimado(this, caboRedeDireita, caboRedeEsquerda));
				proprio.playSound("sons/son2.wav");
			}

		}
	}

	public int pegaArmaAtual() {
		return armaAtual;
	}

	public void mudarArmaAtual(int codigoDaArma) {
		armaAtual = codigoDaArma;
	}

	public void render(Graphics2D g, int x, int y) {
		super.render(g, x, y);
	}

	public String getArma1() {
		return arma1;
	}

	public void setTemArma2() {
		temArma2 = true;
	}

	public void setArma1(String arma1) {
		this.arma1 = arma1;
	}

	public String getArma2() {
		return arma2;
	}

	public void setArma2(String arma2) {
		this.arma2 = arma2;
	}

	public void somaPontos(int valor) {
		pontos += valor;
	}

	public int getArmaAtual() {
		return armaAtual;
	}

	public void setArmaAtual(int armaAtual) {
		this.armaAtual = armaAtual;
	}

	public boolean isTemArma2() {
		return temArma2;
	}

	public void diminuiVida(int perda) {
		vida -= perda;
	}
}
