package Scripts.SnowMap;

import Level.Script;
import ScriptActions.*;
import Scripts.MazeMap.RedTunicScriptAction;
import Scripts.TeleportScriptActions.TeleportGrassScriptAction;
import Utils.Visibility;

import java.util.ArrayList;

public class EmeraldScript extends Script {

    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedEmerald", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Chaos Emerald...");
                    addText("You feel its overwhelming power.");
                    addText("Your head begins to pound.");
                    addText("I must return this to the Queen!");
                    addText("With haste!");
                }});

                addScriptAction(new NPCChangeVisibilityScriptAction(Visibility.HIDDEN));

                addScriptAction(new ChangeFlagScriptAction("collectedEmerald", true));
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }



}
