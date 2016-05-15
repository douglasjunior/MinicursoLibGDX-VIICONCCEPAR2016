package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Screen;

/**
 * Classe genérica que define o comportamento das telas do Jogo
 * Created by Douglas on 14/05/2016.
 */
public abstract class TelaBase implements Screen {

    protected MainGame game;

    public TelaBase(MainGame game) {
        this.game = game;
    }

    /**
     * Método chamado pela LibGDX a cada quandro (frame) por segundo
     *
     * @param delta
     */
    @Override
    public void render(float delta) {
        atualizar(delta);
        desenhar();
    }

    /**
     * Método que cada tela do jogo deve implementar para atualizar os atributos dos objetos.
     *
     * @param delta
     */
    protected abstract void atualizar(float delta);

    /**
     * Método que cada tela do jogo deve implementar para desenhar os objetos na tela.
     */
    protected abstract void desenhar();

    /**
     * Chamado sempre que o jogo é minimizado
     */
    @Override
    public void pause() {

    }

    /**
     * Chamado toda vez que a tela muda de tamanho.
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Chamado toda vez que o jogo volta ao topo das janelas
     */
    @Override
    public void resume() {

    }

    /**
     * Chamado quando a tela é substituída por outra
     */
    @Override
    public void hide() {
        // quando a tela é dubstituída o dispose também deve ser invocado
        dispose();
    }
}
