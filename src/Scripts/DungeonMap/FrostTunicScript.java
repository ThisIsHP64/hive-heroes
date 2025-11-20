package Scripts.DungeonMap;

import Level.Script;

import ScriptActions.*;

import java.util.ArrayList;

public class FrostTunicScript extends Script {


    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedFrostTunic", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Frost tunic acquired! Press 4 to toggle.");
                }});

                addScriptAction(new ChangeFlagScriptAction("collectedFrostTunic", true));
                addScriptAction(new FrostTunicScriptAction());

            }});
            
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedFrostTunic", true));

                // addScriptAction(new ChangeFlagScriptAction("collectedFrostTunic", true));


            }});

        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
