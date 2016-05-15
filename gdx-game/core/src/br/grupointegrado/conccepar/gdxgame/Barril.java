package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Classe que representa o Barril
 * Created by Douglas on 14/05/2016.
 */
public class Barril extends Obstaculo {

    public Barril(Recursos recursos, World mundo, OrthographicCamera camera, float posicaoX) {
        super(recursos, mundo, camera, posicaoX);
    }

    /**
     * Retorna a posição Y do corpo físico em metros
     *
     * @return
     */
    @Override
    protected float getPosicaoCorpoY() {
        return Chao.ALTURA_CORPO / 2 + getAlturaCorpo() / 2;
    }

    /**
     * Retorna a largura da textura
     *
     * @return
     */
    @Override
    protected float getLarguraTextura() {
        return 67;
    }

    /**
     * Retorna a altura da textura
     *
     * @return
     */
    @Override
    protected float getAlturaTextura() {
        return 90;
    }

    /**
     * Retorna o objeto da textura
     *
     * @return
     */
    @Override
    protected Texture getTextura() {
        return recursos.txBarril;
    }
}
