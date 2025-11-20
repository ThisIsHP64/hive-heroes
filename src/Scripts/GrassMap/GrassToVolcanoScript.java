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
import Scripts.TeleportScriptActions.TeleportVolcanoScriptAction;

public class GrassToVolcanoScript extends Script{

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ProcessLevel2ScriptAction());

        scriptActions.add(new Process50NectarScriptAction());


        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", false));
                addRequirement(new FlagRequirement("hasCollected50Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Requirements: Level 2 and Deposit 50 Nectar \nto the Queen Bee.");
                    addText("The heat emanating from the entrance is unbearable.");
                    addText("I probably need some sort of heat protection.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", true));
                addRequirement(new FlagRequirement("hasCollected50Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Queen Bee mandates that I deposit at least 50 \nnectar into the hive first.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", false));
                addRequirement(new FlagRequirement("hasCollected50Nectar", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I am not Level 2 yet.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", true));
                addRequirement(new FlagRequirement("hasCollected50Nectar", true));
                scriptActions.add(new TextboxScriptAction() {{
                    addText("The searing passage is lit. Enter the Volcanic Map?", new String[] { "Yes", "No" });
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

                    addScriptAction(new TeleportVolcanoScriptAction());
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
