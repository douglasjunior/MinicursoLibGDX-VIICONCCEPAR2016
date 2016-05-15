package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Classe com métodos utilitários.
 * Created by Douglas on 05/10/2015.
 */
public class Util {

    public static final float PIXEL_METRO = 32;

    /**
     * Cria um corpo dentro do mundo
     *
     * @param mundo
     * @param tipo
     * @param x
     * @param y
     * @return
     */
    public static Body criarCorpo(World mundo, BodyDef.BodyType tipo, float x, float y) {
        BodyDef definicao = new BodyDef();
        definicao.type = tipo;
        definicao.position.set(x, y);
        definicao.fixedRotation = true;
        Body corpo = mundo.createBody(definicao);
        return corpo;
    }

    /**
     * Cria uma forma para o corpo
     *
     * @param corpo
     * @param shape         Forma geométrica do corpo
     * @param identificador Identificador utilizado para detectar a colisão
     * @return
     */
    public static Fixture criarForma(Body corpo, Shape shape, Object identificador) {
        FixtureDef definicao = new FixtureDef();
        definicao.friction = 0.3f; // fricção/atrito entre um corpo e outro
        definicao.shape = shape;
        Fixture forma = corpo.createFixture(definicao);
        forma.setUserData(identificador); // identifição da forma
        return forma;
    }

    /**
     * Calcula o novo estágio com base no delta e na velocidade.
     * Utilizado para alterar as texturas e fazer a animação.
     *
     * @param delta
     * @param estagio
     * @param velocidade Quantas imagens serão trocadas por segundo.
     * @param tamanho
     * @return
     */
    public static float calcularEstagio(float delta, float estagio, float velocidade, int tamanho) {
        estagio += velocidade * delta;
        if (estagio >= tamanho) {
            estagio = 0;
        }
        return estagio;
    }

    /**
     * Verifica se houve colisão entre A e B, e vice-versa.
     *
     * @param contato
     * @param a
     * @param b
     * @return
     */
    public static boolean colidiu(Contact contato, Class a, Class b) {
        if ((a.isAssignableFrom(contato.getFixtureA().getUserData().getClass()) &&
                b.isAssignableFrom(contato.getFixtureB().getUserData().getClass())) ||
                (a.isAssignableFrom(contato.getFixtureB().getUserData().getClass()) &&
                        b.isAssignableFrom(contato.getFixtureA().getUserData().getClass()))) {
            return true;
        }
        return false;
    }

    /**
     * Cria um arquivo BitmapFont de acordo com o tamanho de letra especificado.
     *
     * @param tamanho
     * @return
     */
    public static BitmapFont criarFonte(int tamanho) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonte/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = tamanho;
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        BitmapFont fonte = generator.generateFont(params);
        generator.dispose();
        return fonte;
    }
}
