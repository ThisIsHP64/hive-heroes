package Scripts.GrassMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;


public class DeadDeerScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

    
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        // dialogue sequence
        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{

                addScriptAction(new TextboxScriptAction() {{
                    addText("...");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*The deer lies motionless on the soft grass...*");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*Its fur is cold...*");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("*What could have done this!?*");
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
