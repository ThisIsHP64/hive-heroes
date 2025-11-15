package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

// script for talking to bug npc
// checkout the documentation website for a detailed guide on how this script works
public class LadyBugScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addScriptAction(new TextboxScriptAction() {{
                    addText("Hi!");
                    addText("Fun fact! In the snow region, it will snow! In the volcano \nregion, lava will rain down! Have fun!");

                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
