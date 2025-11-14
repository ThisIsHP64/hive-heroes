package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class GeneScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Gene: Flew for the Queen.");
            addText("Gene: Fell for the hive.");
            addText("Gene: Burned by both.");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}