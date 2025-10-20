package Scripts.GrassMap;

import java.util.ArrayList;

import Game.ScreenCoordinator;
import Level.Script;
import ScriptActions.*;

public class TeleportScript extends Script {
    ScreenCoordinator screenCoordinator;


    public TeleportScript(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    public TeleportScript(String[] text) {
        
    }
    
    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());
        scriptActions.add(new SwitchScreenScriptAction());
        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
    
}
