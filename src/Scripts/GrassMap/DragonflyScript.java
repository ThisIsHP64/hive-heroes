package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import Scripts.FinalBoss.LavaRainScriptAction;

import java.util.ArrayList;

// script for talking to bug npc
// checkout the documentation website for a detailed guide on how this script works
public class DragonflyScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("Feeling down and thirsty? Be sure to get as much \nnectar from sunflowers! Keep exploring!");
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
