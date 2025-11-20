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
import Scripts.TeleportScriptActions.TeleportMazeScriptAction;

public class GrassToMazeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ProcessLevel2ScriptAction());
        scriptActions.add(new Process15NectarScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", false));
                addRequirement(new FlagRequirement("hasCollected15Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Requirements: Level 2 and Deposit 15 Nectar \nto the Queen Bee.");
                    addText("What could be in here?");
                    addText("You peek in, and become captivated by the infinitely \nmesmerizing patterns.");
                    addText("You feel like exploring it later.");

                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", true));
                addRequirement(new FlagRequirement("hasCollected15Nectar", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("The Queen Bee mandates that I deposit at least 15 \nnectar into the hive first.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", false));
                addRequirement(new FlagRequirement("hasCollected15Nectar", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I am not Level 2 yet.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("isLevel2", true));
                addRequirement(new FlagRequirement("hasCollected15Nectar", true));

                scriptActions.add(new TextboxScriptAction() {{
                    addText("Would you like to begin the Maze?", new String[] { "Yes", "No" });
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

                    addScriptAction(new TeleportMazeScriptAction());
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
