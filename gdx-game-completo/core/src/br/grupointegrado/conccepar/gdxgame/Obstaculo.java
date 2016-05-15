package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import static br.grupointegrado.conccepar.gdxgame.Util.PIXEL_METRO;

/**
 * Classe genérica que define os obstáculos
 * Created by Douglas on 14/05/2016.
 */
public abstract class Obstaculo {

    private OrthographicCamera camera;
    private World mundo;
    private float posicaoX;
    private Body corpo;
    protected Recursos recursos;
    private Sprite sprite = new Sprite();

    public Obstaculo(Recursos recursos, World mundo, OrthographicCamera camera, float posicaoX) {
        this.recursos = recursos;
        this.mundo = mundo;
        this.camera = camera;
        this.posicaoX = posicaoX;

        initCorpo();
    }

    /**
     * Cria o corpo físico do obstáculo
     */
    private void initCorpo() {
        // cria o corpo
        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posicaoX, getPosicaoCorpoY());
        // cria a forma do corpo
        Shape shape = initShape();
        Util.criarForma(corpo, shape, this);
        shape.dispose();
    }

    /**
     * Cria a forma do corpo físico
     *
     * @return
     */
    protected Shape initShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getLarguraCorpo() / 2f, getAlturaCorpo() / 2f);
        return shape;
    }

    /**
     * Retorna a posição Y do corpo físico em metros
     *
     * @return
     */
    protected abstract float getPosicaoCorpoY();

    /**
     * Retorna a largura da textura
     *
     * @return
     */
    protected abstract float getLarguraTextura();

    /**
     * Retorna a altura da textura
     *
     * @return
     */
    protected abstract float getAlturaTextura();

    /**
     * Retorna a largura do corpo físico em metros
     *
     * @return
     */
    protected float getLarguraCorpo() {
        return getLarguraTextura() / PIXEL_METRO;
    }

    /**
     * Retorna a altura do corpo físico em Metros
     *
     * @return
     */
    protected float getAlturaCorpo() {
        return getAlturaTextura() / PIXEL_METRO;
    }

    /**
     * Retorna o objeto da textura
     *
     * @return
     */
    protected abstract Texture getTextura();

    /**
     * Atualiza os atributos necessários do obstáculo
     *
     * @param delta
     */
    protected void atualizar(float delta) {
        atualizarSprite();
    }

    /**
     * Atualiza os atributos do sprite
     */
    private void atualizarSprite() {
        // atualiza a textura do sprite
        sprite.setTexture(getTextura());
        sprite.setRegion(0, 0, getTextura().getWidth(), getTextura().getHeight());
        // atualiza tamanho do sprite
        sprite.setSize(getLarguraTextura(), getAlturaTextura());
        // atualiza a posição do sprite de acordo com a posição do corpo
        float diferencaY = getAlturaTextura() / 2 - (getAlturaCorpo() * PIXEL_METRO) / 2;
        sprite.setPosition(corpo.getPosition().x * PIXEL_METRO - getLarguraTextura() / 2,
                corpo.getPosition().y * PIXEL_METRO - getAlturaTextura() / 2 + diferencaY);
        // atualiza o ângulo do eprsonagem de acordo com o ângulo do corpo
        sprite.setOriginCenter();
        sprite.setRotation((float) Math.toDegrees(corpo.getAngle()));
    }

    /**
     * Desenha o obstáculo na tela
     *
     * @param batch
     */
    protected void desenhar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Retorna o corpo fíciso do obstáculo
     *
     * @return
     */
    public Body getCorpo() {
        return corpo;
    }

    /**
     * Verifica se o obstáculo já saiu fora da tela
     *
     * @return
     */
    public boolean estaForaDaTela() {
        float cameraX = camera.position.x / PIXEL_METRO;
        float cameraWidth = camera.viewportWidth / PIXEL_METRO;
        float cameraFim = cameraX - cameraWidth / 2;
        float obstaculoFim = getCorpo().getPosition().x + getLarguraCorpo();
        if (obstaculoFim < cameraFim) {
            return true;
        }
        return false;
    }
}
