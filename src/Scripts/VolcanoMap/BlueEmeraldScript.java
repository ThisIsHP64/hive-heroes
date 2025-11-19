package Scripts.VolcanoMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ChangeFlagScriptAction;
import ScriptActions.ConditionalScriptAction;
import ScriptActions.ConditionalScriptActionGroup;
import ScriptActions.FlagRequirement;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;

public class BlueEmeraldScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new CheckBossActiveScript());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("bossActive", true));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("The Red Chaos Emerald calls to you.");
                    addText("You must stop the possessor of the Green \n Chaos Emerald at all cost.");
                }});

            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("bossActive", false));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("The energy emanating from the emerald is scathing.");
                }});

            }});
        }});
        
        scriptActions.add(new UnlockPlayerScriptAction());


       return scriptActions;
    }
    
}
