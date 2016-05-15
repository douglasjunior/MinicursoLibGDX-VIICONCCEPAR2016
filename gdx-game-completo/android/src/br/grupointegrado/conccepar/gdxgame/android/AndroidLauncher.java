package br.grupointegrado.conccepar.gdxgame.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import br.grupointegrado.conccepar.gdxgame.MainGame;

public class AndroidLauncher extends AndroidApplication {
    /**
     * Configura como o jogo será representado no Android
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainGame(), config);
    }
}
