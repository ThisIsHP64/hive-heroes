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

        scriptActions.add(new HasBlueEmeraldScriptAction());

        scriptActions.add(new CheckBothEmeraldsScriptAction());
        
        scriptActions.add(new CheckBossActiveScript());

        scriptActions.add(new ConditionalScriptAction() {{

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasBothEmeralds", false));
                // addRequirement(new FlagRequirement("collectedBlueEmerald", false));
                addRequirement(new FlagRequirement("bossActive", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("I must find the other Chaos Emeralds to defeat the \ntraitorous Queen.");
                }});
            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasBothEmeralds", true));
                // addRequirement(new FlagRequirement("collectedBlueEmerald", true));

                addRequirement(new FlagRequirement("bossActive", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Are you ready to challenge the Queen?");
                }});

                addScriptAction(new TeleportHiveScriptAction());
            }});


            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("bossActive", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Teleport to Grass Map?", new String[] {"Yes", "No"});
                }});
                        addScriptAction(new ConditionalScriptAction() {{
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
        }});

        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
}
