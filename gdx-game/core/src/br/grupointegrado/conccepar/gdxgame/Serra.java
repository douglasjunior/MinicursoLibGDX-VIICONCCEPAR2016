package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Classe que representa a Serra
 * Created by Douglas on 14/05/2016.
 */
public class Serra extends Obstaculo {

    public Serra(Recursos recursos, World mundo, OrthographicCamera camera, float posicaoX) {
        super(recursos, mundo, camera, posicaoX);
    }

    /**
     * Cria a forma do corpo físico da Serra
     *
     * @return
     */
    @Override
    protected Shape initShape() {
        getCorpo().setFixedRotation(false);
        CircleShape shape = new CircleShape();
        shape.setRadius(getLarguraCorpo() / 2);
        return shape;
    }

    /**
     * Atualiza os atributos necessários do obstáculo
     *
     * @param delta
     */
    @Override
    protected void atualizar(float delta) {
        // incrementa o ângulo da serra para fazer o efeito de rotação
        float angulo = getCorpo().getAngle() - delta * 10;
        getCorpo().setTransform(getCorpo().getPosition(), angulo);
        // chama o método atualizar genérico da classe Obstaculo
        super.atualizar(delta);
    }

    /**
     * Retorna a posição Y do corpo físico em metros
     *
     * @return
     */
    @Override
    protected float getPosicaoCorpoY() {
        return Chao.ALTURA_CORPO / 2 + getAlturaCorpo() / 2 + 2.6f;
    }

    /**
     * Retorna a largura da textura
     *
     * @return
     */
    @Override
    protected float getLarguraTextura() {
        return 80;
    }

    /**
     * Retorna a altura da textura
     *
     * @return
     */
    @Override
    protected float getAlturaTextura() {
        return 80;
    }

    /**
     * Retorna o objeto da textura
     *
     * @return
     */
    @Override
    protected Texture getTextura() {
        return recursos.txSerra;
    }

}
