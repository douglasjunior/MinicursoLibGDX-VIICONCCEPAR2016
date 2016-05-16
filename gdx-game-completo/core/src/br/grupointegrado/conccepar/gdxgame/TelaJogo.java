package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static br.grupointegrado.conccepar.gdxgame.Util.PIXEL_METRO;

/**
 * Tela que representa o cenário do jogo.
 * Created by Douglas on 14/05/2016.
 */
public class TelaJogo extends TelaBase {

    private SpriteBatch batch;
    private Recursos recursos;

    // objeto que define o comportamento da câmera do jogo
    private OrthographicCamera camera;

    // objeto que representa o mundo fíciso do jogo, com força da gravidade e colisão
    private World mundo;
    private Personagem personagem;
    private Chao chao;
    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();
    private Array<Tiro> tiros = new Array<Tiro>();
    private Array<Explosao> explosoes = new Array<Explosao>();
    private Array objetosMortos = new Array();

    private boolean reiniciarJogo = false;
    private float pontuacao = 0;
    private BitmapFont fonte;

    private Box2DDebugRenderer debug;

    private float atrasoTiro = 0;

    public TelaJogo(MainGame game) {
        super(game);
    }

    /**
     * Método chamado quando a tela é exibida a primeira vez
     */
    @Override
    public void show() {
        batch = game.getBatch();
        recursos = game.getRecursos();
        fonte = game.getFonte();

        initCamera();
        initMundo();
        initPersonagem();
        initMusica();
    }

    /**
     * Inicia as músicas que tocarão durante o jogo
     */
    private void initMusica() {
        recursos.msMenu.setLooping(true);
        recursos.msMenu.setVolume(1f);
        recursos.msMenu.play();

        recursos.msJogo.setLooping(true);
        recursos.msJogo.setVolume(0.2f);
    }

    /**
     * Inicia o objeto da câmera
     */
    private void initCamera() {
        camera = new OrthographicCamera(MainGame.LARGURA_TELA, MainGame.ALTURA_TELA);
        camera.setToOrtho(false, MainGame.LARGURA_TELA, MainGame.ALTURA_TELA);
        camera.update();
    }

    /**
     * Inicia o objeto do mundo físico
     */
    private void initMundo() {
        // cria o mundo físico e configura a gravidade para 200 pixels por segundo
        mundo = new World(new Vector2(0, -9.8f), false);
        // configura o evento que detecta as colisões
        mundo.setContactListener(new EventoColisao() {
            @Override
            public void beginContact(Contact contact) {
                // quando qualquer colisão corre este evento é chamado
                detectouColisao(contact);
            }
        });
        // cria o objeto para debug para desenhar os objetos do corpo físico
        debug = new Box2DDebugRenderer();
    }

    /**
     * Identifica qual a colisão que ocorreu no mundo físico
     *
     * @param contact
     */
    private void detectouColisao(Contact contact) {
        if (Util.colidiu(contact, Tiro.class, Obstaculo.class)) {
            colisaoTiroObstaculo(contact);
        } else if (Util.colidiu(contact, Personagem.class, Obstaculo.class)) {
            // quando o personagem colidir com qualquer obstáculo o jogo termina
            gameOver();
        }
    }

    /**
     * Transfere o personagem para MORTO
     */
    private void gameOver() {
        // garante que o Gameover será chamado só uma vez
        if (personagem.getSituacao() != Personagem.MORTO) {
            personagem.morrer();
            recursos.msJogo.stop();
        }
    }

    /**
     * Evento de colisão entre o tiro e um obstaculo qualquer
     *
     * @param contact
     */
    private void colisaoTiroObstaculo(Contact contact) {
        Tiro tiro;
        Obstaculo obstaculo;
        // verifica se o tiro está na FixtureA ou FixtureB
        if (contact.getFixtureA().getUserData() instanceof Tiro) {
            tiro = (Tiro) contact.getFixtureA().getUserData();
            obstaculo = (Obstaculo) contact.getFixtureB().getUserData();
        } else {
            tiro = (Tiro) contact.getFixtureB().getUserData();
            obstaculo = (Obstaculo) contact.getFixtureA().getUserData();
        }
        // adiciona o Tiro na lista de objetos a serem destruidos
        objetosMortos.add(tiro);
        // verifica se o tiro se colidiu com o barril
        if (obstaculo instanceof Barril) {
            objetosMortos.add(obstaculo);
            criarExplosao((Barril) obstaculo);
        }
    }

    /**
     * Cria o efeito de explosão
     *
     * @param barril
     */
    private void criarExplosao(Barril barril) {
        // define a posição da explosão
        Vector2 posicao = new Vector2();
        posicao.x = barril.getCorpo().getPosition().x * PIXEL_METRO - barril.getLarguraTextura() / 2;
        posicao.y = barril.getCorpo().getPosition().y * PIXEL_METRO - barril.getAlturaTextura() / 2;
        // cria o objeto que representa a explosão
        Explosao explosao = new Explosao(recursos, posicao);
        explosoes.add(explosao);
        recursos.smExplosao.play();
    }

    /**
     * Cria o personagem e o chão
     */
    private void initPersonagem() {
        personagem = new Personagem(recursos, mundo);
        chao = new Chao(recursos, mundo, camera);
    }

    /**
     * Chamado a cada quadro por segundo para desenhar os recursos na tela
     */
    @Override
    protected void desenhar() {
        // inicia o desenho
        batch.begin();
        // configura o batch de acordo com o tamanho da câmera
        batch.setProjectionMatrix(camera.combined);

        desenharFundo();
        desenharObjetos();
        desenharPontuacao();

        // finaliza o desenho
        batch.end();

        // desenha o debug dos corpos físicos
        //debug.render(mundo, camera.combined.cpy().scl(PIXEL_METRO));

        // verifica se o jogo deve ser reiniciado
        if (reiniciarJogo) {
            // cria uma nova tela de jogo
            game.setScreen(new TelaJogo(game));
        }
    }

    /**
     * Desenha a pontuação na tela
     */
    private void desenharPontuacao() {
        float x = camera.position.x - MainGame.LARGURA_TELA / 2 + 20;
        float y = camera.position.y + MainGame.ALTURA_TELA / 2 - 20;
        fonte.draw(batch, (int) pontuacao + " m", x, y);
    }

    /**
     * Desenha os objetos na ordem correta
     */
    private void desenharObjetos() {
        desenharObstaculos();
        personagem.desenhar(batch);
        chao.desenhar(batch);
        desenharTiros();
        desenharExplosoes();
    }

    /**
     * Desenha as explosões
     */
    private void desenharExplosoes() {
        for (Explosao explosao : explosoes) {
            explosao.desenhar(batch);
        }
    }

    /**
     * Desenha os tiros
     */
    private void desenharTiros() {
        for (Tiro tiro : tiros) {
            tiro.desenhar(batch);
        }
    }

    /**
     * Desenha os obstáculos
     */
    private void desenharObstaculos() {
        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.desenhar(batch);
        }
    }

    /**
     * Desenha o fundo de acordo com o tamanho e posição da câmera
     */
    private void desenharFundo() {
        float x = camera.position.x - MainGame.LARGURA_TELA / 2f;
        float y = camera.position.y - MainGame.ALTURA_TELA / 2f;
        batch.draw(recursos.txFundo, x, y, MainGame.LARGURA_TELA, MainGame.ALTURA_TELA);
    }

    /**
     * Chamado a cada quadro (frame) por segundo para atualizar os recursos da tela
     *
     * @param delta
     */
    @Override
    protected void atualizar(float delta) {
        capturarTeclas();
        capturarToques();
        atualizarCamera();
        atualizarObjetos(delta);
        atualizarPontuacao();

        // atualiza o próximo quadro (frame) do mundo
        mundo.step(1f / 60f, 6, 2);

        destruirObjetosMortos();
    }

    /**
     * Atualiza a pontuação de acordo com o deslocamento do personagem
     */
    private void atualizarPontuacao() {
        if (personagem.getSituacao() != Personagem.PARADO &&
                personagem.getSituacao() != Personagem.MORTO) {
            pontuacao = personagem.getPosicao().x;
        }
    }

    /**
     * Captura os toques na tela
     */
    private void capturarToques() {
        // verifica se houve o toque na tela
        if (Gdx.input.justTouched()) {
            if (personagem.getSituacao() == Personagem.PARADO) {
                // jogo deve ser inciado
                iniciarJogo();
                return;
            }
            if (personagem.getSituacao() == Personagem.MORTO) {
                // jogo deve ser reiniciado
                reiniciarJogo = true;
                return;
            }
            // verifica se o toque ocorreu na parte superior direita da tela
            if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 &&
                    Gdx.input.getY() < Gdx.graphics.getHeight() / 2 &&
                    (personagem.getSituacao() == Personagem.CORRENDO ||
                            personagem.getSituacao() == Personagem.ATIRANDO)) {
                personagem.pular();
            }
            // verifica se o toque ocorreu na parte inferior direita da tela
            if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2 &&
                    Gdx.input.getY() >= Gdx.graphics.getHeight() / 2 &&
                    personagem.getSituacao() == Personagem.CORRENDO) {
                personagem.deslizar();
            }
            // verifica se o toque ocorreu na parte esquerda da tela
            if (Gdx.input.getX() <= Gdx.graphics.getWidth() / 2
                    && personagem.getSituacao() == Personagem.CORRENDO) {
                personagem.atirar();
            }
        }
    }

    /**
     * Destroi os objetos que foram adicionados na lista de objetos mortos
     */
    private void destruirObjetosMortos() {
        for (Object obj : objetosMortos) {
            destruirObjeto(obj);
        }
        // limpa a lista de objetos mortos
        objetosMortos.clear();
    }

    /**
     * Método genérico que destroi objetos
     *
     * @param objeto
     */
    private void destruirObjeto(Object objeto) {
        // verifica o tipo do objeto para destruílo
        if (objeto instanceof Tiro) {
            Tiro tiro = (Tiro) objeto;
            tiros.removeValue(tiro, false);
            mundo.destroyBody(tiro.getCorpo());
        } else if (objeto instanceof Obstaculo) {
            Obstaculo obstaculo = (Obstaculo) objeto;
            obstaculos.removeValue(obstaculo, false);
            mundo.destroyBody(obstaculo.getCorpo());
        }
    }

    /**
     * Atualiza todos os objetos do jogo em ordem
     *
     * @param delta
     */
    private void atualizarObjetos(float delta) {
        atualizarObstaculos(delta);
        personagem.atualizar(delta);
        chao.atualizar(delta);
        atualizarTiros(delta);
        atualizarExplosoes(delta);
    }

    /**
     * Atualiza as explosões
     *
     * @param delta
     */
    private void atualizarExplosoes(float delta) {
        for (Explosao explosao : explosoes) {
            // se já terminou a explosão pode ser removina na hora, pois não possui corpo fíciso
            if (explosao.terminou())
                explosoes.removeValue(explosao, false);
            else
                explosao.atualizar(delta);
        }
    }

    /**
     * Atualiza e cria novos tiros
     *
     * @param delta
     */
    private void atualizarTiros(float delta) {
        // incrementa o tempo de atraso dos tiros
        atrasoTiro += delta;
        // verifica se o personagem está atirando, se a quantidade máxima de tiros foi disparada e se o atraso já foi superado
        if (personagem.getSituacao() == Personagem.ATIRANDO && tiros.size < 3 && atrasoTiro >= 1) {
            Tiro tiro = new Tiro(recursos, mundo, camera, personagem.getPosicao().x + 1);
            tiros.add(tiro);
            atrasoTiro = 0;
            recursos.smTiro.play();
        }

        // atualiza ou destroi os tiros se ele já saiu da tela
        for (Tiro tiro : tiros) {
            if (tiro.estaForaDaTela())
                objetosMortos.add(tiro);
            else
                tiro.atualizar(delta);
        }
    }

    /**
     * Atualiza ou cria os obstáculos aleatoriamente
     *
     * @param delta
     */
    private void atualizarObstaculos(float delta) {
        // cria somente 1 obstáculos por vez
        if (obstaculos.size == 0) {
            // número aleatório para decidir qual obstáculo será criado
            int num = MathUtils.random(0, 2);
            Obstaculo obstaculo;
            switch (num) {
                case 0:
                    obstaculo = new Espinho(recursos, mundo, camera, personagem.getPosicao().x + 20);
                    break;
                case 1:
                    obstaculo = new Serra(recursos, mundo, camera, personagem.getPosicao().x + 20);
                    break;
                default:
                    obstaculo = new Barril(recursos, mundo, camera, personagem.getPosicao().x + 20);
                    break;
            }
            obstaculos.add(obstaculo);
        }
        // atualiza os obstáculos ou remove se já saiu da tela
        for (Obstaculo obstaculo : obstaculos) {
            if (obstaculo.estaForaDaTela())
                objetosMortos.add(obstaculo);
            else
                obstaculo.atualizar(delta);
        }
    }

    /**
     * Posiciona a câmera de acordo com a posição do personagem
     */
    private void atualizarCamera() {
        camera.position.x = personagem.getPosicao().x * PIXEL_METRO + camera.viewportWidth / 5;
        camera.update();
    }

    /**
     * Captura as teclas pressionadas no teclado
     */
    private void capturarTeclas() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                && personagem.getSituacao() == Personagem.MORTO) {
            reiniciarJogo = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                && personagem.getSituacao() == Personagem.PARADO) {
            iniciarJogo();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)
                && (personagem.getSituacao() == Personagem.CORRENDO ||
                personagem.getSituacao() == Personagem.ATIRANDO)) {
            personagem.pular();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)
                && personagem.getSituacao() == Personagem.CORRENDO) {
            personagem.deslizar();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)
                && personagem.getSituacao() == Personagem.CORRENDO) {
            personagem.atirar();
        }
    }

    /**
     * Inicia o jogo
     */
    private void iniciarJogo() {
        recursos.msMenu.stop();
        recursos.msJogo.play();
        personagem.correr();
    }

    /**
     * Destroi os recursos criados pela tela
     */
    @Override
    public void dispose() {
        mundo.dispose();
        debug.dispose();
    }
}
