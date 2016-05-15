package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static br.grupointegrado.conccepar.gdxgame.Util.PIXEL_METRO;

/**
 * Created by Douglas on 14/05/2016.
 */
public class Chao {

    // tamanho do chao
    public static final float LARGURA_TEXTURA = 640;
    public static final float ALTURA_TEXTURA = 106.7f;
    public static final float LARGURA_CORPO = LARGURA_TEXTURA / PIXEL_METRO;
    public static final float ALTURA_CORPO = ALTURA_TEXTURA / PIXEL_METRO;

    private Recursos recursos;
    private OrthographicCamera camera;
    private World mundo;
    private Body corpo;
    // o chão possui 2 sprites para fazer o efeito de movimentação
    private Sprite sprite1;
    private Sprite sprite2;

    public Chao(Recursos recursos, World mundo, OrthographicCamera camera) {
        this.recursos = recursos;
        this.camera = camera;
        this.mundo = mundo;

        initCorpo();
        initSprite();
    }

    /**
     * Cria o corpo físico do chão
     */
    private void initCorpo() {
        // cria o corpo
        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0, 0);
        // cria a forma do corpo
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(LARGURA_CORPO / 2, ALTURA_CORPO / 2);
        Util.criarForma(corpo, shape, this);
        shape.dispose();
    }

    /**
     * Cria os sprites do chão
     */
    private void initSprite() {
        sprite1 = new Sprite();
        sprite1.setTexture(recursos.txChao);
        sprite1.setRegion(0, 0, recursos.txChao.getWidth(), recursos.txChao.getHeight());
        sprite1.setSize(LARGURA_TEXTURA, ALTURA_TEXTURA);
        sprite1.setPosition(corpo.getPosition().x * PIXEL_METRO - LARGURA_TEXTURA / 2,
                corpo.getPosition().y * PIXEL_METRO - ALTURA_TEXTURA / 2);

        // o segundo sprite é posicionado com base no primeiro para fazer o efeito de movimentação
        sprite2 = new Sprite();
        sprite2.setTexture(recursos.txChao);
        sprite2.setRegion(0, 0, recursos.txChao.getWidth(), recursos.txChao.getHeight());
        sprite2.setSize(LARGURA_TEXTURA, ALTURA_TEXTURA);
        sprite2.setPosition(sprite1.getX() + LARGURA_TEXTURA, sprite1.getY());
    }

    /**
     * Atualiza os atributos necessários do obstáculo
     *
     * @param delta
     */
    public void atualizar(float delta) {
        corpo.setTransform(camera.position.x / PIXEL_METRO, 0, 0);
        atualizarSprite();
    }

    /**
     * Atualiza os atributos do sprite e faz o efeito de movimentação
     */
    private void atualizarSprite() {
        float cameraX = camera.position.x;
        float cameraWidth = camera.viewportWidth;
        float cameraFim = cameraX - cameraWidth / 2;
        float obstaculoFim = sprite1.getX() + LARGURA_TEXTURA;
        if (obstaculoFim < cameraFim) {
            sprite1.setPosition(sprite2.getX(), sprite2.getY());
            sprite2.setPosition(sprite1.getX() + LARGURA_TEXTURA, sprite1.getY());
        }
    }

    /**
     * Desenha o chão na tela
     *
     * @param batch
     */
    public void desenhar(SpriteBatch batch) {
        sprite1.draw(batch);
        sprite2.draw(batch);
    }
}
