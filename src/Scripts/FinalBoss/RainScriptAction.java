package Scripts.FinalBoss;

import Engine.WeatherManager;
import Level.ScriptState;
import ScriptActions.ScriptAction;

public class RainScriptAction extends ScriptAction {

    
    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public ScriptState execute() {
        WeatherManager.GLOBAL.enableOverrideMode();
        WeatherManager.GLOBAL.setRaining(true);

        return ScriptState.COMPLETED;
    }

    
}
