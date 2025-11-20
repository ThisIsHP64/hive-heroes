package Scripts.GrassMap;

import Level.Script;
import ScriptActions.ConditionalScriptAction;
import ScriptActions.ConditionalScriptActionGroup;
import ScriptActions.CustomRequirement;
import ScriptActions.FlagRequirement;
import ScriptActions.LockPlayerScriptAction;
import ScriptActions.ScriptAction;
import ScriptActions.TextboxScriptAction;
import ScriptActions.UnlockPlayerScriptAction;
import Scripts.TeleportScriptActions.TeleportDungeonScriptAction;
import java.util.ArrayList;

public class GrassToDungeonScript extends Script{

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());
        
        scriptActions.add(new ProcessLevel3ScriptAction());
        scriptActions.add(new Process100NectarScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel3", false));
                addRequirement(new FlagRequirement("hasCollected100Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Requirements: Level 3 and Deposit 100 Nectar \nto the Queen Bee.");
                    addText("You hear things coming from within the dungeon.");
                    addText("The screams send shivers down your spine. \nMaybe I'll come back later.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel3", true));
                addRequirement(new FlagRequirement("hasCollected100Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Queen Bee mandates that I deposit at least 100 \nnectar into the hive first.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel3", false));
                addRequirement(new FlagRequirement("hasCollected100Nectar", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I am not Level 3 yet.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel3", true));
                addRequirement(new FlagRequirement("hasCollected100Nectar", true));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("The Dungeon invites you. Will you accept?", new String[] { "Yes", "No" });
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

                    addScriptAction(new TeleportDungeonScriptAction());
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
