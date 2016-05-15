package br.grupointegrado.conccepar.gdxgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import br.grupointegrado.conccepar.gdxgame.MainGame;

public class DesktopLauncher {
    /**
     * Configura como o jogo será representado no Desktop
     *
     * @param arg
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = MainGame.LARGURA_TELA;
        config.height = MainGame.ALTURA_TELA;
        new LwjglApplication(new MainGame(), config);
    }
}
