package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class RuthScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Ruth: Her words still coil.");
            addText("Ruth: Survive....");
            addText("Ruth: I can't stop hearing them.");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}