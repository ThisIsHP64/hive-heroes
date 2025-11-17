package Scripts.DungeonMap;

import Level.Script;
import ScriptActions.*;
import Scripts.FinalBoss.LavaRainScriptAction;

import java.util.ArrayList;

public class CarlScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        
        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new TextboxScriptAction() {{
            addText("Name's Carl. Pest control at your service!");
            addText("You know I use to be a hero like you but then I \ntook a stinger to the knee...");
            addText("And then I ended up in this place....");
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());
        
        return scriptActions;
    }
}