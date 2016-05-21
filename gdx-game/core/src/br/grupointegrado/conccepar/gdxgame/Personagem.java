package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static br.grupointegrado.conccepar.gdxgame.Util.*;

/**
 * Classe que representa o personagem e suas ações
 * Created by Douglas on 14/05/2016.
 */
public class Personagem {

    // situações do personagem
    public static final int PARADO = 0, CORRENDO = 1, ATIRANDO = 2, PULANDO = 3, DESLIZANDO = 4, MORTO = 5;

    // tamanho do personagem
    private static final float LARGURA_TEXTURA = 141.75f;
    private static final float ALTURA_TEXTURA = 139f;
    private static final float LARGURA_CORPO = 40 / PIXEL_METRO;
    private static final float ALTURA_CORPO = 110 / PIXEL_METRO;
    private static final float LARGURA_CORPO_DESLIZANDO = 50 / PIXEL_METRO;
    private static final float ALTURA_CORPO_DESLIZANDO = 80 / PIXEL_METRO;
    private static final float INICIAL_X = 0;
    private static final float INICIAL_Y = (Chao.ALTURA_CORPO / 2 + ALTURA_CORPO / 2) + 100;

    private Recursos recursos;
    private int situacao = PARADO;
    private float estagio = 0;
    private Sprite sprite = new Sprite();

    private World mundo;
    private Body corpo;

    private float tempoDeslizando = 0;
    private float tempoAtirando = 0;
    private float larguraCorpo = LARGURA_CORPO;
    private float alturaCorpo = ALTURA_CORPO;
    private float tempoPassos = 1;

    public Personagem(Recursos recursos, World mundo) {
        this.mundo = mundo;
        this.recursos = recursos;
        initCorpo();
    }

    /**
     * Cria o corpo físico do personagem
     */
    private void initCorpo() {
        // cria o corpo
        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, INICIAL_X, INICIAL_Y);
        // cria a forma do corpo
        atualizarTamanhoCorpo();
    }

    /**
     * Método que atualiza o tamanho do corpo.
     * Chamado sempre que o personagem começa a deslizar ou correr.
     */
    private void atualizarTamanhoCorpo() {
        if (corpo.getFixtureList().size > 0) {
            // remove a forma atual do corpo
            corpo.destroyFixture(corpo.getFixtureList().first());
        }
        // cria uma nova forma para o corpo
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(larguraCorpo / 2f, alturaCorpo / 2f);
        Util.criarForma(corpo, shape, this);
        shape.dispose();
        // atualiza posicao do corpo
        corpo.setTransform(corpo.getPosition().x, Chao.ALTURA_CORPO / 2 + alturaCorpo / 2, 0);
    }

    /**
     * Atualiza os atributos do personagem.
     *
     * @param delta
     */
    public void atualizar(float delta) {
        atualizarPulo();
        atualizarVelocidade(delta);
        atualizarDeslizamento(delta);
        atualizarTiro(delta);
        atualizarSprite(delta);
        atualizarAudio(delta);
    }

    /**
     * Reproduz o som do personagem caminhando
     *
     * @param delta
     */
    private void atualizarAudio(float delta) {
        // reproduz somente se estiver correndo ou atirando
        if (situacao == CORRENDO || situacao == ATIRANDO) {
            tempoPassos += delta;
            // reproduz a cada 1 segundo
            if (tempoPassos >= 1) {
                tempoPassos = 0;
                recursos.smPasso.play();
            }
        }
    }

    /**
     * Modifica a situação do personagem para atirando ou correndo.
     *
     * @param delta
     */
    private void atualizarTiro(float delta) {
        if (situacao != MORTO) {
            if (tempoAtirando > 0) {
                tempoAtirando -= delta;
                situacao = ATIRANDO;
            } else if (situacao == ATIRANDO) {
                situacao = CORRENDO;
            }
        }
    }

    /**
     * Modifica a situação do personagem para pulando ou correndo.
     */
    private void atualizarPulo() {
        if (situacao != MORTO) {
            Vector2 velocidade = corpo.getLinearVelocity();
            if (velocidade.y > 0) {
                situacao = PULANDO;
            }
            if (situacao == PULANDO && velocidade.y == 0) {
                situacao = CORRENDO;
            }
        }
    }

    /**
     * Atualiza a situação de deslizamento do personagem.
     *
     * @param delta
     */
    private void atualizarDeslizamento(float delta) {
        boolean mudouTamanho = false;
        // verifica se está em tempo de deslizar
        if (situacao != MORTO && tempoDeslizando > 0) {
            if (situacao != DESLIZANDO)
                mudouTamanho = true;
            situacao = DESLIZANDO;
            tempoDeslizando -= delta;
            // quando está deslizando o tamanho muda
            larguraCorpo = LARGURA_CORPO_DESLIZANDO;
            alturaCorpo = ALTURA_CORPO_DESLIZANDO;
        } else if (situacao == DESLIZANDO) {
            mudouTamanho = true;
            situacao = CORRENDO;
            larguraCorpo = LARGURA_CORPO;
            alturaCorpo = ALTURA_CORPO;
        }
        // caso mude a situação do personagem, então deve mudar também seu tamanho
        if (mudouTamanho) {
            atualizarTamanhoCorpo();
        }
    }

    /**
     * Mantem o personagem em velocidade constante.
     *
     * @param delta
     */
    private void atualizarVelocidade(float delta) {
        if (situacao != PARADO && situacao != MORTO) {
            Vector2 velocidade = corpo.getLinearVelocity();
            // altera somente a velocidade no sentido X
            velocidade.x = 400 * delta;
            corpo.setLinearVelocity(velocidade);
        }
    }


    /**
     * Atualiza qual a textura que estará no sprite atual.
     *
     * @param delta
     */
    private void atualizarSprite(float delta) {
        Texture textura = texturaAtual(delta);
        // atualiza a textura do sprite
        sprite.setTexture(textura);
        sprite.setRegion(0, 0, textura.getWidth(), textura.getHeight());
        // atualiza tamanho do sprite
        sprite.setSize(LARGURA_TEXTURA, ALTURA_TEXTURA);
        // atualiza a posição do sprite de acordo com a posição do corpo
        float x = corpo.getPosition().x * PIXEL_METRO - LARGURA_TEXTURA / 2;
        float y = corpo.getPosition().y * PIXEL_METRO - ALTURA_TEXTURA / 2;
        // calcula a diferença entre a posição do corpo e do sprite
        float diferencaX = 0;
        float diferencaY = 5;
        if (situacao == DESLIZANDO) {
            diferencaX = 30;
            diferencaY = 15;
        }
        sprite.setPosition(x + diferencaX, y + diferencaY);
        // atualiza o ângulo do eprsonagem de acordo com o ângulo do corpo
        sprite.setRotation((float) Math.toDegrees(corpo.getAngle()));
    }

    /**
     * Retorna a textura de acordo com a situação e o estágio atual do personagem.
     *
     * @return
     */
    private Texture texturaAtual(float delta) {
        Texture textura;
        if (situacao == PULANDO) {
            // quando o personando está pulando só faz a troca de 2 texturas
            // se o pulo está subindo
            if (corpo.getLinearVelocity().y > 0) {
                textura = recursos.txPersonagemPulando.first();
            }
            // se o pulo está caindo
            else {
                textura = recursos.txPersonagemPulando.peek();
            }
        } else if (situacao == MORTO) {
            // quando está morto o personagem faz o efeito de texturas somente uma vez
            Array<Texture> texturas = recursos.txPersonagemMorto;
            if (estagio < texturas.size) {
                textura = texturas.get((int) estagio);
                estagio = Util.calcularEstagio(delta, estagio, 7, 99);
            } else {
                textura = texturas.peek();
            }
        } else {
            Array<Texture> texturas = selecionaTexturas();
            estagio = Util.calcularEstagio(delta, estagio, 10, texturas.size);
            textura = texturas.get((int) estagio);
        }
        return textura;
    }

    /**
     * Retorna o conjunto de texturas de acordo com a situação do personagem
     *
     * @return
     */
    private Array<Texture> selecionaTexturas() {
        switch (situacao) {
            case PARADO:
                return recursos.txPersonagemParado;
            case CORRENDO:
                return recursos.txPersonagemCorrendo;
            case ATIRANDO:
                return recursos.txPersonagemAtirando;
            case DESLIZANDO:
                return recursos.txPersonagemDeslizando;
        }
        return null;
    }

    /**
     * Desenha o personagem na tela.
     *
     * @param batch
     */
    public void desenhar(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Altera a situação para CORRENDO
     */
    public void correr() {
        situacao = CORRENDO;
    }

    public int getSituacao() {
        return situacao;
    }

    /**
     * Aplica um impulso no personagem no sentido Y
     */
    public void pular() {
        tempoAtirando = 0;
        situacao = PULANDO;
        corpo.applyLinearImpulse(new Vector2(0, 7), corpo.getWorldCenter(), false);
    }

    /**
     * Incrementa o tempo de dezliamento do personagem em 1.3 segundos
     */
    public void deslizar() {
        tempoDeslizando = 1.3f;
    }

    /**
     * Retorn a posição do corpo físico do eprsonagem
     *
     * @return
     */
    public Vector2 getPosicao() {
        return corpo.getPosition();
    }

    /**
     * Incremento o tempo de tiro do personagem em 0.5 segundos
     */
    public void atirar() {
        tempoAtirando = 0.5f;
    }

    /**
     * Altera a situação do personagem para MORTO
     */
    public void morrer() {
        estagio = 0;
        situacao = MORTO;
        recursos.smGameover.play();
    }
}
