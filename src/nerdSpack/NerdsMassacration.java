package nerdSpack;

import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.Background;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.GameFontManager;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.collision.AdvanceCollisionGroup;
import com.golden.gamedev.object.collision.BasicCollisionGroup;
import com.golden.gamedev.object.collision.CollisionShape;
import com.golden.gamedev.object.sprite.PatternSprite;
import com.golden.gamedev.object.sprite.VolatileSprite;
import inimigos.Inimigo1;
import inimigos.Inimigo2;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

public class NerdsMassacration extends com.golden.gamedev.GameObject {
	public static final int ENTRADA = 1;
	public static final int JOGANDO = 2;
	public static final int FIMDEJOGO = 3;
	public static final int GANHADOR = 4;
	public static final int aDisquete = 1;
	public static final int aCaboDeRede = 2;
	int estadoDoJogo;
	PlayField campoBatalha;
	Background planoDeFundo;
	SpriteGroup grupoNerd;
	SpriteGroup grupoTiroNerd;
	SpriteGroup grupoParedes;
	SpriteGroup grupoDisquete;
	SpriteGroup grupoDisqueteChave;
	SpriteGroup grupoPadrao;
	SpriteGroup grupoEnfeite;
	SpriteGroup grupoInimigo;
	SpriteGroup grupoArmaDiferente;
	SpriteGroup grupoBonus;
	SpriteGroup grupoChave;
	SpriteGroup grupoVida;
	SpriteGroup grupoInimigo2;
	Nerd heroNerd;
	Inimigo1 inimigoTeste;

	public NerdsMassacration(com.golden.gamedev.GameEngine pai) {
		super(pai);
	}

	java.util.ArrayList<Sprite> sprites = new java.util.ArrayList();

	int nivel;

	int iniMax1 = 50;

	GameFont fonteDoJogo1;

	GameFont fonteDoJogo2;

	GameFont fonteDoNivel;

	GameFont fonteDoStatus;

	Timer temporizadorRolaPlanoFundo;

	Timer temporizadorNivel;

	Timer temporizadorDoFim;

	Timer timerDevelocidade;

	Timer temporizadorInimigos;

	Sprite imagenDoNivel;

	Sprite imagenStatusNerd;

	Sprite caboDeRede;

	AnimatedSprite nerdAtack;

	double velocidadeDeRolagem;

	Inimigo2 ini;
	private boolean pixelPerfectCollision;
	public boolean pulando;

	public void initResources() {
		nivel = 1;
		fonteDoJogo1 = fontManager.getFont(getImages("imagens/SmallFont.png", 8, 12));

		fonteDoJogo2 = fontManager.getFont(getImages("imagens/Font.png", 8, 12));

		fonteDoNivel = fontManager.getFont(getImages("imagens/SmallFont.png", 8, 12));

		temporizadorNivel = new Timer(2500);
		temporizadorInimigos = new Timer(2000);
		imagenDoNivel = new Sprite();
		imagenStatusNerd = new Sprite();
		imagenDoNivel.setHorizontalSpeed(1.25D);

		heroNerd = new Nerd(this);

		inicializarNivel();

		parent.nextGameID = 0;
	}

	private void inicializarNivel() {
		iniMax1 = (30 * nivel);

		estadoDoJogo = 1;

		String arquivoDeNivel = "niveis/nivel" + nivel + ".txt";

		String[] conteudo = com.golden.gamedev.util.FileUtil.fileRead(bsIO.getStream(arquivoDeNivel));
		StringTokenizer token = new StringTokenizer(conteudo[0], "+");
		String[] descricao = new String[token.countTokens()];

		for (int linha = 0; linha < descricao.length; linha++) {
			descricao[linha] = token.nextToken();
		}
		BufferedImage status = getImage("imagens/nerdStatus.png");
		BufferedImage temporario = getImage("imagens/nivelFundo.png");
		BufferedImage imagem = com.golden.gamedev.util.ImageUtil.createImage(temporario.getWidth(),
				temporario.getHeight(), 2);

		Graphics2D g = imagem.createGraphics();
		g.drawImage(temporario, 0, 0, null);

		fonteDoNivel.drawString(g, "NIVEL:  " + nivel, 3, 0, 17, temporario.getWidth());

		fonteDoNivel.drawString(g, "----------- ", 3, 0, 37, temporario.getWidth());

		for (int i = 0; i < descricao.length; i++) {
			fonteDoNivel.drawString(g, descricao[i], 3, 0, 70 + i * 20, temporario.getWidth());
		}

		g.dispose();
		imagenStatusNerd.setImage(status);
		imagenStatusNerd.setActive(true);
		imagenStatusNerd.setActive(true);

		imagenDoNivel.setImage(imagem);
		imagenDoNivel.setLocation(-imagem.getWidth(), 200.0D);
		imagenDoNivel.setActive(true);

		token = new StringTokenizer(conteudo[1], " x ");
		int w = Integer.parseInt(token.nextToken());

		int h = Integer.parseInt(token.nextToken());

		campoBatalha = new PlayField();

		BufferedImage fundoPrincipal = getImage("imagens/fundo" + nivel + ".png", false);

		planoDeFundo = new com.golden.gamedev.object.background.ParallaxBackground(
				new Background[] { new Background(w * 32, h * 32),
						new com.golden.gamedev.object.background.ImageBackground(fundoPrincipal,
								fundoPrincipal.getWidth() > w * 32 ? w * 32 : fundoPrincipal.getWidth(),
								fundoPrincipal.getHeight() > h * 32 ? h * 32 : fundoPrincipal.getHeight()) });

		campoBatalha.setBackground(planoDeFundo);

		grupoParedes = campoBatalha.addGroup(new com.golden.gamedev.object.AdvanceSpriteGroup("Parede", 600));

		grupoVida = campoBatalha.addGroup(new SpriteGroup("vida"));
		grupoChave = campoBatalha.addGroup(new SpriteGroup("Chave"));
		grupoNerd = campoBatalha.addGroup(new SpriteGroup("Nerd"));
		grupoTiroNerd = campoBatalha.addGroup(new SpriteGroup("Tiro"));
		grupoBonus = campoBatalha.addGroup(new SpriteGroup("DEnero"));
		grupoInimigo = campoBatalha.addGroup(new SpriteGroup("Inimigos"));
		grupoInimigo2 = campoBatalha.addGroup(new SpriteGroup("InimigosEspeciais"));
		grupoDisqueteChave = campoBatalha.addGroup(new SpriteGroup("Chave"));
		grupoPadrao = campoBatalha.addGroup(new SpriteGroup("Padrao"));
		grupoArmaDiferente = campoBatalha.addGroup(new SpriteGroup("Omg Armafoda"));
		grupoEnfeite = campoBatalha.addGroup(new SpriteGroup("Enfeites"));

		AnimatedSprite bolaFim = new AnimatedSprite(getImages("imagens/fim.png", 4, 1), -100.0D, -100.0D);

		bolaFim.setAnimate(true);
		bolaFim.setLoopAnim(true);
		bolaFim.getAnimationTimer().setDelay(400L);
		grupoPadrao.add(bolaFim);

		AnimatedSprite enfeite = new AnimatedSprite(getImages("imagens/enf2.png", 2, 1), -100.0D, -100.0D);

		enfeite.setAnimate(true);
		enfeite.setLoopAnim(true);
		grupoPadrao.add(enfeite);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int parede = 0;

				try {
					char c = conteudo[(y + 2)].charAt(x);

					switch (c) {

					case '&':
						parede = 99;
						break;
					case 'b':
						parede = 455;
						break;
					case 'd':
						parede = 456;
						break;
					case 'o':
						parede = 1234;
						break;
					case 'c':
						parede = 1005;
						break;
					case '*':
						parede = 100;
						break;
					case 'X':
						parede = 1000;
						break;
					case 'i':
						parede = 111;
						break;
					case 'j':
						parede = 112;
						break;
					case 'p':
						parede = 1023;
						break;

					default:
						parede = Character.getNumericValue(c);
					}

				} catch (Exception e) {
				}

				switch (parede) {
				case 1:
					Sprite terra = new Sprite(getImage("imagens/cendef.png"), x * 32, y * 32);

					grupoParedes.add(terra);
					break;
				case 2:
					caboDeRede = new Sprite(getImage("imagens/omfBonus.png"), x * 32, y * 32 + 20);

					grupoArmaDiferente.add(caboDeRede);
					break;

				case 3:
					Sprite chao3 = new Sprite(getImage("imagens/cen2.png"), x * 32, y * 32);

					grupoParedes.add(chao3);
					break;

				case 4:
					Sprite chao4 = new Sprite(getImage("imagens/cen3.png"), x * 32, y * 32);

					grupoParedes.add(chao4);
					break;

				case 5:
					Sprite cenarioEnfeite = new PatternSprite(enfeite, x * 32 + 6, y * 32 + 6);

					grupoEnfeite.add(cenarioEnfeite);
					break;
				case 6:
					Sprite tijolo = new Sprite(getImage("imagens/tijolo.png"), x * 32, y * 33);

					grupoParedes.add(tijolo);
					break;
				case 455:
					Sprite tijolo2 = new Sprite(getImage("imagens/tijolo2.png"), x * 32, y * 32);
					grupoParedes.add(tijolo2);
					break;
				case 456:
					Sprite tijolo3 = new Sprite(getImage("imagens/tijolo3.png"), x * 32, y * 32);
					grupoParedes.add(tijolo3);
					break;

				case 8:
					Sprite bonus = new Sprite(getImage("imagens/bonus.png"), x * 32, y * 32);

					bonus.setID(110);
					grupoBonus.add(bonus);
					break;

				case 7:
					Sprite arvore = new Sprite(getImage("imagens/arvore.png"), x * 32, y * 32 - 130);

					grupoEnfeite.add(arvore);
					break;
				case 9:
					Sprite danger = new Sprite(getImage("imagens/signal.png"), x * 32, y * 32);

					grupoEnfeite.add(danger);
					break;
				case 99:
					Sprite disco = new Sprite(getImage("imagens/key.png"), x * 32, y * 32);
					grupoChave.add(disco);
					break;
				case 1005:
					Sprite coca = new Sprite(getImage("imagens/coca.png"), x * 32, y * 32);
					coca.setID(22);
					grupoVida.add(coca);
					break;
				case 1234:
					Sprite cafe = new Sprite(getImage("imagens/cafe.png"), x * 32, y * 32);
					cafe.setID(23);
					grupoVida.add(cafe);
					break;
				case 1023:
					ini = new Inimigo2(this);
					ini.setLocation(x * 32 + 1, y * 16);

					grupoInimigo2.add(ini);
					break;
				case 111:
					Sprite bonus2 = new Sprite(getImage("imagens/bonus2.png"), x * 32, y * 32);

					bonus2.setID(111);
					grupoBonus.add(bonus2);
					break;
				case 112:
					Sprite bonus3 = new Sprite(getImage("imagens/bonus3.png"), x * 32, y * 32);

					bonus3.setID(112);
					grupoBonus.add(bonus3);
					break;
				case 100:
					heroNerd.setLocation(x * 16, y * 16);
					break;

				case 1000:
					Sprite fimDoJogo = new PatternSprite(bolaFim, x * 32 + 6, y * 32 + 6);

					grupoDisqueteChave.add(fimDoJogo);
				}

			}
		}

		grupoNerd.add(heroNerd);

		planoDeFundo.setToCenter(heroNerd);

		heroNerd.resetarEstado();

		campoBatalha.update(0L);

		campoBatalha.addCollisionGroup(grupoInimigo, grupoParedes, new InimigoEParedeColidem());

		campoBatalha.addCollisionGroup(grupoNerd, grupoParedes, new NerdEParedeColidem());

		campoBatalha.addCollisionGroup(grupoTiroNerd, grupoParedes, new TiroEParedeColidem());

		campoBatalha.addCollisionGroup(grupoNerd, grupoDisqueteChave, new NerdDisqueteColidem());

		campoBatalha.addCollisionGroup(grupoNerd, grupoArmaDiferente, new NerdEArmaBonusColidem());

		campoBatalha.addCollisionGroup(grupoNerd, grupoBonus, new NerdEBonusColidem());

		campoBatalha.addCollisionGroup(grupoTiroNerd, grupoInimigo, new TiroEInimigoColidem(heroNerd));

		campoBatalha.addCollisionGroup(grupoNerd, grupoChave, new NerdEDiscoChaveColidem(heroNerd));

		campoBatalha.addCollisionGroup(grupoNerd, grupoVida, new NerdEVidaColidem());

		campoBatalha.addCollisionGroup(grupoInimigo2, grupoParedes, new InimigoEspecialColideParede(ini));

		NerdEInimigoColidem c = new NerdEInimigoColidem(heroNerd);
		pixelPerfectCollision = true;
		campoBatalha.addCollisionGroup(grupoNerd, grupoInimigo, c);

		NerdEInimigo2Colidem cd = new NerdEInimigo2Colidem(heroNerd);
		pixelPerfectCollision = true;
		campoBatalha.addCollisionGroup(grupoNerd, grupoInimigo2, cd);

		TiroEInimigo2Colidem cdd = new TiroEInimigo2Colidem(heroNerd);
		pixelPerfectCollision = true;
		campoBatalha.addCollisionGroup(grupoTiroNerd, grupoInimigo2, cdd);

		bsTimer.refresh();
	}

	void addExplosionEffect(double centerX, double centerY) {
		BufferedImage[] image = getImages("imagens/Explosion.png", 5, 3);
		double x = centerX - image[0].getWidth() / 2;
		double y = centerY - image[0].getHeight() / 2;

		VolatileSprite explosion = new VolatileSprite(image, x, y);
		explosion.getAnimationTimer().setDelay(80L);

		campoBatalha.add(explosion);
	}

	public void render(Graphics2D g) {
		campoBatalha.render(g);
		imagenStatusNerd.render(g);

		fonteDoNivel.drawString(g, String.valueOf(heroNerd.getPontos()), 250, 36);

		fonteDoNivel.drawString(g, String.valueOf(nivel), 570, 36);
		fonteDoNivel.drawString(g, String.valueOf(heroNerd.getVida()), 85, 36);
		switch (heroNerd.getArmaAtual()) {
		case 1:
			fonteDoNivel.drawString(g, heroNerd.getArma1(), 405, 42);
			break;
		case 2:
			fonteDoNivel.drawString(g, heroNerd.getArma2(), 409, 42);
		}

		switch (estadoDoJogo) {

		case 1:
			imagenDoNivel.render(g);
			break;

		case 3:
			fonteDoJogo1.drawString(g, "GAME OVER", 185, 200);
			break;

		case 4:
			fonteDoJogo2.drawString(g, "====================", 3, 0, 165, getWidth());

			fonteDoJogo2.drawString(g, "====================", 3, 0, 190, getWidth());

			fonteDoJogo2.drawString(g, "PONTUACAO FINAL: ", 3, 0, 250, getWidth());

			fonteDoJogo2.drawString(g, " " + heroNerd.getPontos(), 3, 0, 300, getWidth());

			fonteDoJogo2.drawString(g, "PARABENS SEU NERD: ", 3, 0, 340, getWidth());

			fonteDoJogo2.drawString(g, "====================", 3, 0, 245, getWidth());
		}

	}

	public void update(long elapsedTime) {
		campoBatalha.update(elapsedTime);
		if (heroNerd.getVida() <= 0) {
			gameOver();
		}
		switch (estadoDoJogo) {
		case 1:
			imagenDoNivel.update(elapsedTime);

			if (temporizadorNivel.isActive()) {
				if (imagenDoNivel.getX() > 30.0D) {
					imagenDoNivel.setX(30.0D);
					imagenDoNivel.setHorizontalSpeed(0.0D);
				} else if ((imagenDoNivel.getX() == 30.0D) && (temporizadorNivel.action(elapsedTime))) {
					imagenDoNivel.setHorizontalSpeed(1.0D);
					temporizadorNivel.setActive(false);
				}
			}

			if (imagenDoNivel.getX() > 640.0D) {
				estadoDoJogo = 2;
			}

		case 2:
			atualizaNivel(elapsedTime);

			if ((heroNerd.getX() > planoDeFundo.getWidth()) || (heroNerd.getY() > planoDeFundo.getHeight())
					|| (heroNerd.getX() + heroNerd.getWidth() < 0.0D)
					|| (heroNerd.getY() + heroNerd.getHeight() < 0.0D)) {

				gameOver();
			}
			if ((temporizadorInimigos.isActive()) && (temporizadorInimigos.action(elapsedTime))) {

				temporizadorInimigos.setActive(false);
			}
			if ((!temporizadorInimigos.isActive()) && (iniMax1 != 0)) {
				temporizadorInimigos.setActive(true);
				inimigoTeste = new Inimigo1(this);
				inimigoTeste.setLocation(sorteio(), heroNerd.getY() - 300.0D);
				grupoInimigo.add(inimigoTeste);
				iniMax1 -= 1;
			}

			if (keyPressed(27)) {
				gameOver();
			}
			if (keyPressed(49)) {
				heroNerd.setArmaAtual(1);
			}
			if (keyPressed(50)) {
				if (heroNerd.isTemArma2()) {
					heroNerd.setArmaAtual(2);
				} else
					playSound("sons/heart.wav");
			}
			break;

		case 3:
			if (keyPressed(27))
				finish();
			break;

		case 4:
			if ((keyPressed(27)) || (temporizadorDoFim.action(elapsedTime))) {
				finish();
			}
			break;
		}
	}

	private double sorteio() {
		int nerdX = (int) heroNerd.getX();

		if (heroNerd.getDirection() == 1) {
			double posicao = nerdX + Math.random() * 100.0D - 100.0D;
			return posicao;
		}

		double posicao = nerdX + Math.random() * 100.0D + 100.0D;
		return posicao;
	}

	private void atualizaNivel(long elapsedTime) {
		campoBatalha.getBackground().setToCenter(heroNerd);
	}

	private void gameOver() {
		estadoDoJogo = 3;

		bsMusic.stopAll();

		heroNerd.setActive(false);
	}

	class NerdEInimigoColidem extends BasicCollisionGroup {
		Nerd nerd;

		public NerdEInimigoColidem(Nerd nerd) {
			this.nerd = nerd;
		}

		public void collided(Sprite s1, Sprite s2) {
			if (nerd.getVida() <= 0) {
				s1.setActive(false);
				NerdsMassacration.this.gameOver();
			} else {
				playSound("sons/son3.wav");
				nerd.diminuiVida(1);
			}
		}
	}

	class NerdEInimigo2Colidem extends AdvanceCollisionGroup {
		Nerd nerd;

		public NerdEInimigo2Colidem(Nerd nerd) {
			this.nerd = nerd;
		}

		public void collided(Sprite s1, Sprite s2) {
			if (collisionSide == 1) {
				s2.setHorizontalSpeed(-0.3D);
			} else if (collisionSide == 2) {
				s2.setHorizontalSpeed(0.3D);
			}

			if (nerd.getVida() <= 0) {
				s1.setActive(false);
				NerdsMassacration.this.gameOver();
			} else {
				playSound("sons/son3.wav");
				nerd.diminuiVida(40);
			}
		}
	}

	class InimigoEParedeColidem extends AdvanceCollisionGroup {
		InimigoEParedeColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			revertPosition1();

			if (collisionSide == 8) {
				s1.setVerticalSpeed(0.0D);

			} else if ((collisionSide == 1) || (collisionSide == 2)) {
				s1.setVerticalSpeed(0.0D);
			}
		}
	}

	class InimigoEspecialColideParede extends AdvanceCollisionGroup {
		Inimigo2 ini;
		boolean passou = false;

		public InimigoEspecialColideParede(Inimigo2 ini) {
			this.ini = ini;
		}

		public void collided(Sprite s1, Sprite s2) {
			revertPosition1();
			if (collisionSide == 8) {
				s1.setVerticalSpeed(0.0D);

				if (!passou) {
					s1.setHorizontalSpeed(-0.4D);
					passou = true;
				}

			} else if (collisionSide == 1) {
				ini.setDirection(2);
				s1.setHorizontalSpeed(0.4D);
			} else if (collisionSide == 2) {
				ini.setDirection(1);
				s1.setHorizontalSpeed(-0.4D);
			}
		}
	}

	class NerdEParedeColidem extends AdvanceCollisionGroup {
		NerdEParedeColidem() {
		}

		public CollisionShape getCollisionShape1(Sprite s1) {
			rect1.setBounds(s1.getX() + 4.0D, s1.getY(), s1.getWidth() - 10, s1.getHeight());

			return rect1;
		}

		public boolean isCollide(Sprite s1, Sprite s2, CollisionShape shape1, CollisionShape shape2) {
			boolean ignoreCollision = false;

			return ignoreCollision ? false : super.isCollide(s1, s2, shape1, shape2);
		}

		public void collided(Sprite s1, Sprite s2) {
			revertPosition1();

			if (collisionSide == 8) {
				s1.setVerticalSpeed(0.0D);
				pulando = false;
			} else if ((collisionSide == 1) || (collisionSide == 2)) {

				s1.setHorizontalSpeed(0.0D);
			}
		}
	}

	class TiroEParedeColidem extends com.golden.gamedev.object.collision.PreciseCollisionGroup {
		TiroEParedeColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			revertPosition1();
			s1.setActive(false);

			addExplosionEffect(s1.getX() + s1.getWidth() / 2, s1.getY() + s1.getHeight() / 2);
		}
	}

	class TiroEInimigo2Colidem extends BasicCollisionGroup {
		Nerd nerd;
		int vida = 30;

		public TiroEInimigo2Colidem(Nerd nerd) {
			this.nerd = nerd;
		}

		public void collided(Sprite s1, Sprite s2) {
			int arma = nerd.getArmaAtual();
			switch (arma) {
			case 1:
				s1.setActive(false);

				if (vida != 0) {
					vida -= 15;
				} else {
					s2.setActive(false);
					nerd.setTemChave(true);
					nerd.somaPontos(4000);
				}
				break;

			case 2:
				if (vida != 0) {
					vida -= 30;
				} else {
					s2.setActive(false);
					nerd.setTemChave(true);
					nerd.somaPontos(4000);
				}
				break;
			}
		}
	}

	class TiroEInimigoColidem extends BasicCollisionGroup {
		Nerd nerd;
		int vida = 30;

		public TiroEInimigoColidem(Nerd nerd) {
			this.nerd = nerd;
		}

		public void collided(Sprite s1, Sprite s2) {
			int arma = nerd.getArmaAtual();
			switch (arma) {
			case 1:
				s1.setActive(false);

				if (vida != 0) {
					vida -= 15;
				} else {
					s2.setActive(false);
					vida = 30;
					nerd.somaPontos(40);
				}
				break;

			case 2:
				if (vida != 0) {
					vida -= 30;
				} else {
					s2.setActive(false);
					vida = 30;
					nerd.somaPontos(40);
				}
				break;
			}
		}
	}

	class NerdDisqueteColidem extends BasicCollisionGroup {
		NerdDisqueteColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			if (((Nerd) s1).isTemChave()) {
				nivel += 1;

				if (nivel <= 4) {
					NerdsMassacration.this.inicializarNivel();
				} else {
					estadoDoJogo = 4;

					temporizadorDoFim = new Timer(8000);
				}
			}
		}
	}

	class NerdEArmaBonusColidem extends BasicCollisionGroup {
		NerdEArmaBonusColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			s2.setActive(false);
			heroNerd.setTemArma2();
			heroNerd.mudarArmaAtual(2);
		}
	}

	class NerdEBonusColidem extends BasicCollisionGroup {
		int id;

		NerdEBonusColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			s2.setActive(false);
			id = s2.getID();

			playSound("sons/money.wav");
			switch (id) {
			case 110:
				heroNerd.somaPontos(300);
				break;
			case 111:
				heroNerd.somaPontos(1100);
				break;
			case 112:
				heroNerd.somaPontos(3000);
			}

		}
	}

	class NerdEDiscoChaveColidem extends BasicCollisionGroup {
		Nerd heroNerd;

		public NerdEDiscoChaveColidem(Nerd heroNerd) {
			this.heroNerd = heroNerd;
		}

		public void collided(Sprite s1, Sprite s2) {
			s2.setActive(false);
			heroNerd.setTemChave(true);
		}
	}

	class NerdEVidaColidem extends BasicCollisionGroup {
		NerdEVidaColidem() {
		}

		public void collided(Sprite s1, Sprite s2) {
			switch (s2.getID()) {
			case 22:
				s2.setActive(false);
				((Nerd) s1).somaVida(60);
				break;
			case 23:
				s2.setActive(false);
				((Nerd) s1).somaVida(150);
				((Nerd) s1).ativaTemporizadorPulo();
			}
		}
	}
}
