package Scripts.VolcanoMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ChangeFlagScriptAction;
import ScriptActions.ConditionalScriptAction;
import ScriptActions.ConditionalScriptActionGroup;
import ScriptActions.CustomRequirement;
import ScriptActions.FlagRequirement;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import Scripts.TeleportScriptActions.TeleportGrassScriptAction;
import Scripts.TeleportScriptActions.TeleportSnowScriptAction;

public class AddRedEmeraldScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        // meant to trigger right as the player teleports into the volcano map (after the evil queen dialogue)
        // if boss is active 

        scriptActions.add(new CheckBossActiveScript());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("bossActive", true));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("The Red Chaos Emerald calls to you.");
                }});

                // if true, then add the chaos emerald in there
                addScriptAction(new AddRedEmeraldScriptAction());

                // stops the textbox
                addScriptAction(new ChangeFlagScriptAction("spawnedYet", true));

            }});
        }});

       return scriptActions;
    }
    
}
