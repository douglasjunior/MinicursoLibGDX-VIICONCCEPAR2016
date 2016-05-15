package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static br.grupointegrado.conccepar.gdxgame.Util.PIXEL_METRO;

/**
 * Created by Douglas on 14/05/2016.
 */
public class Tiro {

    private static float LARGURA_TEXTURA = 30, ALTURA_TEXTURA = 24.2f,
            RAIO_CORPO = (LARGURA_TEXTURA / 2) / PIXEL_METRO;

    private OrthographicCamera camera;
    private World mundo;
    private float posicaoX;
    private Body corpo;
    protected Recursos recursos;
    private Sprite sprite = new Sprite();
    private float estagio = 0;

    public Tiro(Recursos recursos, World mundo, OrthographicCamera camera, float posicaoX) {
        this.recursos = recursos;
        this.mundo = mundo;
        this.camera = camera;
        this.posicaoX = posicaoX;

        initCorpo();
    }

    /**
     * Cria o corpo físico do tiro
     */
    private void initCorpo() {
        // cria o corpo
        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, posicaoX, 3.5f);
        // cria a forma do corpo
        CircleShape shape = new CircleShape();
        shape.setRadius(RAIO_CORPO);
        Util.criarForma(corpo, shape, this);
        shape.dispose();
    }

    /**
     * Atualiza os atributos necessários do tiro
     *
     * @param delta
     */
    public void atualizar(float delta) {
        atualizarVelocidade(delta);
        atualizarSprite(delta);
    }

    /**
     * Mantém o tiro em velocidade constante no sentido X
     *
     * @param delta
     */
    private void atualizarVelocidade(float delta) {
        Vector2 velocidade = corpo.getLinearVelocity();
        velocidade.x = 1000 * delta;
        velocidade.y = 0;
        corpo.setLinearVelocity(velocidade);
    }

    /**
     * Atualiza os atributos do sprite
     */
    private void atualizarSprite(float delta) {
        Texture textura = texturaAtual(delta);
        // atualiza a textura do sprite
        sprite.setTexture(textura);
        sprite.setRegion(0, 0, textura.getWidth(), textura.getHeight());
        // atualiza tamanho do sprite
        sprite.setSize(LARGURA_TEXTURA, ALTURA_TEXTURA);
        // atualiza a posição do sprite de acordo com a posição do corpo
        sprite.setPosition(corpo.getPosition().x * PIXEL_METRO - LARGURA_TEXTURA / 2 + 5,
                corpo.getPosition().y * PIXEL_METRO - ALTURA_TEXTURA / 2);
    }

    /**
     * Retorna a textura de acordo com a situação e o estágio atual.
     *
     * @return
     */
    private Texture texturaAtual(float delta) {
        Array<Texture> texturas = recursos.txPersonagemTiro;
        estagio = Util.calcularEstagio(delta, estagio, 10, texturas.size);
        Texture textura = texturas.get((int) estagio);

        return textura;
    }

    /**
     * Desenha o tiro na tela
     *
     * @param batch
     */
    public void desenhar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Body getCorpo() {
        return corpo;
    }

    /**
     * Verifica se o tiro já saiu fora da tela
     *
     * @return
     */
    public boolean estaForaDaTela() {
        float cameraX = camera.position.x / PIXEL_METRO;
        float cameraWidth = camera.viewportWidth / PIXEL_METRO;
        float cameraFim = cameraX + cameraWidth / 2;
        float obstaculoFim = getCorpo().getPosition().x - RAIO_CORPO * 2;
        if (obstaculoFim > cameraFim) {
            return true;
        }
        return false;
    }
}
