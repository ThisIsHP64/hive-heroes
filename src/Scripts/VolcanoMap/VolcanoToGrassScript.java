package Scripts.VolcanoMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;
import Scripts.TeleportScriptActions.*;

public class VolcanoToGrassScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();

        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new HasRedEmeraldScript());
        
        scriptActions.add(new CheckBossActiveScript());

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedRedEmerald", false));
                addRequirement(new FlagRequirement("bossActive", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I must find the Red Emerald to defeat the Queen.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("collectedRedEmerald", true));
                addRequirement(new FlagRequirement("bossActive", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Are you ready for the next challenge?", new String[] { "Yes", "No" });
                }});

                // add a scriptaction that begins the snow level boss
                addScriptAction(new TeleportSnowScriptAction());
            }});


            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("bossActive", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Teleport to Grass Map?", new String[] { "Yes", "No" });
                }});

                addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 0;
                    }
                });

                addScriptAction(new TeleportGrassScriptAction());
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


        // scriptActions.add(new ConditionalScriptAction() {{

        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
        //         addRequirement(new FlagRequirement("bossActive", false));

        //         addScriptAction(new TextboxScriptAction() {{
        //             addText("Teleport to Grass Map?", new String[] { "Yes", "No" });
        //         }});

        //         addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
        //         addRequirement(new CustomRequirement() {
        //             @Override
        //             public boolean isRequirementMet() {
        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
        //                 return answer == 0;
        //             }
        //         });

        //         addScriptAction(new TeleportGrassScriptAction());
        //     }});

        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
        //         addRequirement(new CustomRequirement() {
        //             @Override
        //             public boolean isRequirementMet() {
        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
        //                 return answer == 1;
        //             }
        //         });
                
        //         addScriptAction(new TextboxScriptAction("Oh...uh...awkward..."));
        //     }});

                
        //     }});
        // }});
        // scriptActions.add(new TextboxScriptAction() {{
        //     addText("Teleport to Grass Map?", new String[] { "Yes", "No" });
        // }});

        // scriptActions.add(new ConditionalScriptAction() {{
        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
        //         addRequirement(new CustomRequirement() {
        //             @Override
        //             public boolean isRequirementMet() {
        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
        //                 return answer == 0;
        //             }
        //         });

        //         addScriptAction(new TeleportGrassScriptAction());
        //     }});

        //     addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
        //         addRequirement(new CustomRequirement() {
        //             @Override
        //             public boolean isRequirementMet() {
        //                 int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
        //                 return answer == 1;
        //             }
        //         });
                
        //         addScriptAction(new TextboxScriptAction("Oh...uh...awkward..."));
        //     }});
        // }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
