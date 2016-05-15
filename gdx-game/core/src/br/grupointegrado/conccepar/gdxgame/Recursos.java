package br.grupointegrado.conccepar.gdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Classe reponsável por armazenar os recursos utilizados no jogo.
 * Created by Douglas on 14/05/2016.
 */
public class Recursos {

    // guarda uma cópia de todos os recursos para facilitar destruílos no final
    Array<Disposable> todosRecursos = new Array<Disposable>();

    Array<Texture> txPersonagemAtirando;
    Array<Texture> txPersonagemCorrendo;
    Array<Texture> txPersonagemDeslizando;
    Array<Texture> txPersonagemMorto;
    Array<Texture> txPersonagemParado;
    Array<Texture> txPersonagemPulando;
    Array<Texture> txPersonagemTiro;
    Array<Texture> txExplosao;
    Texture txBarril;
    Texture txChao;
    Texture txEspinho;
    Texture txFundo;
    Texture txSerra;
    Music msMenu;
    Music msJogo;
    Sound smExplosao;
    Sound smGameover;
    Sound smPasso;
    Sound smTiro;

    /**
     * Inicia todos os recursos utilizados no jogo, texturas, músicas, etc...
     */
    public void iniciarRecursos() {
        iniciarTexturas();
        initAudios();
    }

    /**
     * Carrega todos os arquivos de áudio do jogo
     */
    private void initAudios() {
        msMenu = criarMusica(Gdx.files.internal("audio/menu.mp3"));
        msJogo = criarMusica(Gdx.files.internal("audio/jogo.mp3"));
        smExplosao = criarSom(Gdx.files.internal("audio/explosao.ogg"));
        smGameover = criarSom(Gdx.files.internal("audio/gameover.ogg"));
        smPasso = criarSom(Gdx.files.internal("audio/passo.ogg"));
        smTiro = criarSom(Gdx.files.internal("audio/tiro.ogg"));
    }

    /**
     * Carrega todas as texturas utilizadas no jogo.
     */
    private void iniciarTexturas() {
        txPersonagemAtirando = carregarTexturas("personagem/atirando");
        txPersonagemCorrendo = carregarTexturas("personagem/correndo");
        txPersonagemDeslizando = carregarTexturas("personagem/deslizando");
        txPersonagemMorto = carregarTexturas("personagem/morto");
        txPersonagemParado = carregarTexturas("personagem/parado");
        txPersonagemPulando = carregarTexturas("personagem/pulando");
        txPersonagemTiro = carregarTexturas("personagem/tiro");

        txExplosao = carregarTexturas("explosao");

        txBarril = criarTextura(Gdx.files.internal("cenario/barril.png"));
        txChao = criarTextura(Gdx.files.internal("cenario/chao.png"));
        txEspinho = criarTextura(Gdx.files.internal("cenario/espinho.png"));
        txFundo = criarTextura(Gdx.files.internal("cenario/fundo.png"));
        txSerra = criarTextura(Gdx.files.internal("cenario/serra.png"));
    }

    /**
     * Percorre todos os arquivos da pasta e carrega como Texture
     *
     * @param pasta
     * @return
     */
    private Array<Texture> carregarTexturas(String pasta) {
        Array<Texture> imagens = new Array<Texture>();
        // recupera os arquivos da pasta
        FileHandle[] arquivos = Gdx.files.internal(pasta).list();
        // percorre os arquivos para carregar as texturas
        for (FileHandle arquivo : arquivos) {
            Texture textura = criarTextura(arquivo);
            imagens.add(textura);
        }
        return imagens;
    }

    /**
     * Cria um objeto Texture a partir de um arquivo
     *
     * @param arquivo
     * @return
     */
    private Texture criarTextura(FileHandle arquivo) {
        Texture textura = new Texture(arquivo);
        // armazena o recurso na lista para ser destruido no final
        todosRecursos.add(textura);
        return textura;
    }

    /**
     * Cria um objeto Music a partir de um arquivo
     *
     * @param arquivo
     * @return
     */
    private Music criarMusica(FileHandle arquivo) {
        Music musica = Gdx.audio.newMusic(arquivo);
        // armazena o recurso na lista para ser destruido no final
        todosRecursos.add(musica);
        return musica;
    }

    /**
     * Cria um objeto Sound a partir de um arquivo
     *
     * @param arquivo
     * @return
     */
    private Sound criarSom(FileHandle arquivo) {
        Sound som = Gdx.audio.newSound(arquivo);
        // armazena o recurso na lista para ser destruido no final
        todosRecursos.add(som);
        return som;
    }

    /**
     * Destroi todos os recursos carregados
     */
    public void destruirRecursos() {
        for (Disposable recurso : todosRecursos) {
            recurso.dispose();
        }
    }

}
