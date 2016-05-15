package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe que repesenta a Explosão
 * Created by Douglas on 15/05/2016.
 */
public class Explosao {

    private final static float LARGURA_TEXTURA = 96, ALTURA_TEXTURA = 96;

    private Recursos recursos;
    private Vector2 posicao;
    private Sprite sprite;

    private float estagio = 0;

    public Explosao(Recursos recursos, Vector2 posicao) {
        this.recursos = recursos;
        this.posicao = posicao;

        initSprite();
    }

    /**
     * Inicia o Sprite da Explosão.
     * A explosão não tem corpo físico pois não necessita de colisão.
     */
    private void initSprite() {
        sprite = new Sprite();
        sprite.setTexture(recursos.txExplosao.first());
        sprite.setRegion(0, 0, recursos.txExplosao.first().getWidth(), recursos.txExplosao.first().getHeight());
        sprite.setSize(LARGURA_TEXTURA, ALTURA_TEXTURA);
        sprite.setPosition(posicao.x, posicao.y);
    }

    /**
     * Atualiza os atributos necessários da explosão
     *
     * @param delta
     */
    public void atualizar(float delta) {
        // faz a troca de textura da explosão para gerar o efeito
        Texture texturaAtual = recursos.txExplosao.get((int) estagio);
        sprite.setTexture(texturaAtual);
        // a explosão não reinicia o estágio, ela deve fazer o efeito uma única vez
        estagio = Util.calcularEstagio(delta, estagio, 18, 99);
    }

    /**
     * Desenha a explosão na tela
     *
     * @param batch
     */
    public void desenhar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Verifica se o efeito da explosão já terminou
     *
     * @return
     */
    public boolean terminou() {
        return estagio >= recursos.txExplosao.size;
    }
}

