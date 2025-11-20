package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

public class GhostScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
            addScriptAction(new TextboxScriptAction() {{
                addText("I am the spirit of the forest.");
            }});
            addScriptAction(new TextboxScriptAction() {{
                addText("Fear not, little one. I mean you no harm.");
            }});
            addScriptAction(new TextboxScriptAction() {{
                addText("This land breathes with memories... and dangers.");
            }});
            addScriptAction(new TextboxScriptAction() {{
                addText("Protect the trees and the creatures that dwell here.");
            }});
            addScriptAction(new TextboxScriptAction() {{
                addText("Your journey is only beginning!");
            }});
        }});
    }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
