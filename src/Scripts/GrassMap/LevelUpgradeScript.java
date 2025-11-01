package Scripts.GrassMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ChangeFlagScriptAction;
import ScriptActions.ConditionalScriptAction;
import ScriptActions.ConditionalScriptActionGroup;
import ScriptActions.CustomRequirement;
import ScriptActions.FlagRequirement;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import Scripts.TeleportScriptActions.TeleportVolcanoScriptAction;

public class LevelUpgradeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new ProcessLevelUpScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel1", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You've reached Level 1.");
                }});
            }});
            
            scriptActions.add(new ChangeFlagScriptAction("isLevel1", false));


            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You've reached Level 2.");
                }});
            }});



            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel3", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You've reached Level 3.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel4", true));
                addScriptAction(new TextboxScriptAction() {{
                    addText("You've reached Level 4.");
                }});
            }});
        }});
            
        return scriptActions;
    }
    
}
