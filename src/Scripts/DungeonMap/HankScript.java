package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class HankScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Hank: Her light chose me.");
            addText("Hank: Her shadow kept me.");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}