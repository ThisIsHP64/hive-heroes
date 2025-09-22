package Scripts.TestMap;

import Level.Script;
import ScriptActions.*;
import java.util.ArrayList;

// script for talking to walrus npc
// checkout the documentation website for a detailed guide on how this script works
public class PowerUpScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        // scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTakenPowerUp", false));
                addScriptAction(new TextboxScriptAction() {{
                    addText("Super Saiyan 3!!");
                }});
                addScriptAction(new ChangeFlagScriptAction("hasTakenPowerUp", true));
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTakenPowerUp", true));
                addScriptAction(new TextboxScriptAction("Super saiyan blue coming soon!"));
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
