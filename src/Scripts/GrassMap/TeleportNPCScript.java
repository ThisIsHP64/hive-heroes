package Scripts.GrassMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;

public class TeleportNPCScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Halt.");
                    addText("Has the Queen Bee given you your orders?");
                    addText("Let's hope you don't fail.");
                }});
                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));
                addScriptAction(new TextboxScriptAction("Good luck."));
                
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}