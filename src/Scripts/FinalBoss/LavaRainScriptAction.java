package Scripts.FinalBoss;

import Effects.RedRainParticleBossFight;
import Effects.RedRainParticleSystemBossFight;
import Engine.GamePanel;
import Engine.WeatherManager;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class LavaRainScriptAction extends ScriptAction {
    RedRainParticleSystemBossFight rainSystem;


    @Override
    public void setup() {
        rainSystem = new RedRainParticleSystemBossFight(800, 605);
    }

    @Override
    public ScriptState execute() {
        WeatherManager.GLOBAL.enableOverrideMode();
        WeatherManager.GLOBAL.setRedRain(true);

        return ScriptState.COMPLETED;
    }
}
