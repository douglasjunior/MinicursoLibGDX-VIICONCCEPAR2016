package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {

    public static final int LARGURA_TELA = 640, ALTURA_TELA = 360;

    // cobjeto responsável por desenhar as texturas e os sprites
    private SpriteBatch batch;
    // objeto que carrega os recursos das texturas e aúdios
    private Recursos recursos;
    // objeto que desenha texto na tela
    private BitmapFont fonte;

    /**
     * Método chamado quando o jogo é iniciado
     */
    @Override
    public void create() {
        fonte = Util.criarFonte(32);
        batch = new SpriteBatch();
        // define a TelaJogo como tela inicial
        setScreen(new TelaJogo(this));
    }

    /**
     * Método invocado a cada quadro (frame) por segundo.
     */
    @Override
    public void render() {
        // limpa a tela
        Gdx.gl.glClearColor(1, .25f, .25f, .25f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // desenha a tela atual
        super.render();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public synchronized Recursos getRecursos() {
        if (recursos == null) {
            recursos = new Recursos();
            recursos.iniciarRecursos();
        }
        return recursos;
    }

    public BitmapFont getFonte() {
        return fonte;
    }

    /**
     * Destroi os recursos criados no início do jogo.
     */
    @Override
    public void dispose() {
        super.dispose();
        fonte.dispose();
        batch.dispose();
        recursos.destruirRecursos();
    }
}
