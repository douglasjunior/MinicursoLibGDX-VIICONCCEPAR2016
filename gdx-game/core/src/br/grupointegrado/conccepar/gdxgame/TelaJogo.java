package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    }

    /**
     * Inicia o objeto da câmera
     */
    private void initCamera() {

    }

    /**
     * Inicia o objeto do mundo físico
     */
    private void initMundo() {

    }

    /**
     * Identifica qual a colisão que ocorreu no mundo físico
     *
     * @param contact
     */
    private void detectouColisao(Contact contact) {

    }

    /**
     * Transfere o personagem para MORTO
     */
    private void gameOver() {

    }

    /**
     * Evento de colisão entre o tiro e um obstaculo qualquer
     *
     * @param contact
     */
    private void colisaoTiroObstaculo(Contact contact) {

    }

    /**
     * Cria o efeito de explosão
     *
     * @param barril
     */
    private void criarExplosao(Barril barril) {

    }

    /**
     * Cria o personagem e o chão
     */
    private void initPersonagem() {

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

        // desenhar aqui...


        // finaliza o desenho
        batch.end();

        // desenha o debug dos corpos físicos
        debug.render(mundo, camera.combined.cpy().scl(PIXEL_METRO));
    }

    /**
     * Desenha a pontuação na tela
     */
    private void desenharPontuacao() {

    }

    /**
     * Desenha os objetos na ordem correta
     */
    private void desenharObjetos() {

    }

    /**
     * Desenha as explosões
     */
    private void desenharExplosoes() {

    }

    /**
     * Desenha os tiros
     */
    private void desenharTiros() {

    }

    /**
     * Desenha os obstáculos
     */
    private void desenharObstaculos() {

    }

    /**
     * Desenha o fundo de acordo com o tamanho e posição da câmera
     */
    private void desenharFundo() {

    }

    /**
     * Chamado a cada quadro (frame) por segundo para atualizar os recursos da tela
     *
     * @param delta
     */
    @Override
    protected void atualizar(float delta) {

    }

    /**
     * Atualiza a pontuação de acordo com o deslocamento do personagem
     */
    private void atualizarPontuacao() {

    }

    /**
     * Destroi os objetos que foram adicionados na lista de objetos mortos
     */
    private void destruirObjetosMortos() {

    }

    /**
     * Método genérico que destroi objetos
     *
     * @param objeto
     */
    private void destruirObjeto(Object objeto) {

    }

    /**
     * Atualiza todos os objetos do jogo em ordem
     *
     * @param delta
     */
    private void atualizarObjetos(float delta) {

    }

    /**
     * Atualiza as explosões
     *
     * @param delta
     */
    private void atualizarExplosoes(float delta) {

    }

    /**
     * Atualiza e cria novos tiros
     *
     * @param delta
     */
    private void atualizarTiros(float delta) {

    }

    /**
     * Atualiza ou cria os obstáculos aleatoriamente
     *
     * @param delta
     */
    private void atualizarObstaculos(float delta) {

    }

    /**
     * Posiciona a câmera de acordo com a posição do personagem
     */
    private void atualizarCamera() {

    }

    /**
     * Inicia o jogo
     */
    private void iniciarJogo() {

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
     * Destroi os recursos criados pela tela
     */
    @Override
    public void dispose() {
        mundo.dispose();
        debug.dispose();
    }
}
