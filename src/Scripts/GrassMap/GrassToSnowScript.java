package Scripts.GrassMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.ConditionalScriptAction;
import ScriptActions.ConditionalScriptActionGroup;
import ScriptActions.CustomRequirement;
import ScriptActions.FlagRequirement;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Scripts.TeleportScriptActions.TeleportSnowScriptAction;

public class GrassToSnowScript extends Script{

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ProcessLevel4ScriptAction());
        scriptActions.add(new Process150NectarScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel4", false));
                addRequirement(new FlagRequirement("hasCollected150Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Requirements: Level 4 and Deposit 150 Nectar \nto the Queen Bee.");
                    addText("You peer into the harsh winters ahead.");
                    addText("You may need some sort of weather protection.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel4", true));
                addRequirement(new FlagRequirement("hasCollected150Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Queen Bee mandates that I deposit at least 150 \nnectar into the hive first.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel4", false));
                addRequirement(new FlagRequirement("hasCollected150Nectar", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I am not Level 4 yet.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel4", true));
                addRequirement(new FlagRequirement("hasCollected150Nectar", true));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("Enter the Snow Map? You may not make it out.", new String[] { "Yes", "No" });
                }});

            scriptActions.add(new ConditionalScriptAction() {{
                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new CustomRequirement() {
                        @Override
                        public boolean isRequirementMet() {
                            int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                            return answer == 0;
                        } 
                    });

                    addScriptAction(new TeleportSnowScriptAction());
                }});

                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                    addRequirement(new CustomRequirement() {
                        @Override
                        public boolean isRequirementMet() {
                            int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                            return answer == 1;
                        }
                    });

                    addScriptAction(new TextboxScriptAction("..."));

                    }});
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
