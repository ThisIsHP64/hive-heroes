package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class DougScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Doug: Gold rivers.");
            addText("Doug: Sweet air.");
            addText("Doug: Drowned in her promise.");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}