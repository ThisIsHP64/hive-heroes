package Scripts.HiveMap;

import Level.Player;
import Level.Script;
import Level.ScriptState;
import Players.Bee;
import ScriptActions.*;
import java.util.ArrayList;

public class QueenBeeScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new ConditionalScriptAction() {{

            // 🐝 First conversation
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", false));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Rise, brave one of the hive.");
                    addText("You are the spark that will light the sky once more.");
                    addText("Beyond the plains lie uncharted lands, filled with dangers and resources.");
                    addText("Claim its nectar, and our song will echo through the fields again!");
                }});

                addScriptAction(new TextboxScriptAction() {{
                    addText("Use WASD to move and outsmart your enemies!");
                    addText("Press SPACE to attack, E to collect powerups, and 1 to activate them.");
                    addText("Press SPACE near flowers or NPCs to interact.");
                    addText("Press SPACE near my head to deposit nectar.");
                    addText("Now go, my soldier — follow the path of the flower to new regions and prevail!");
                }});

                addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", true));
            }});

            // 🐝 Second time — Inferno dialogue
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new FlagRequirement("hasTalkedToQueen", true));

                addScriptAction(new TextboxScriptAction() {{
                    addText("Do you need a rundown of your duties?", new String[]{"Yes", "No"});
                }});

                addScriptAction(new ConditionalScriptAction() {{

                    // “Yes” → repeat duties
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 0;
                            }
                        });
                        addScriptAction(new ChangeFlagScriptAction("hasTalkedToQueen", false));
                    }});

                    // no - offer Land of Inferno
                    addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                        addRequirement(new CustomRequirement() {
                            @Override
                            public boolean isRequirementMet() {
                                int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                return answer == 1;
                            }
                        });

                        addScriptAction(new TextboxScriptAction() {{
                            addText("Do you wish to travel to the Land of Inferno?", new String[]{"Yes", "No"});
                        }});

                        addScriptAction(new ConditionalScriptAction() {{
                            // yes - gets tunic
                            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                                addRequirement(new CustomRequirement() {
                                    @Override
                                    public boolean isRequirementMet() {
                                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                        return answer == 0;
                                    }
                                });

                                addScriptAction(new TextboxScriptAction("Then take this Red Tunic. It will protect you from the flames!"));

                                // Scriptaction override
                                addScriptAction(new ScriptAction() {
                                    @Override
                                    public ScriptState execute() {
                                        Player player = map.getPlayer();
                                        if (player instanceof Bee bee) {
                                            bee.obtainTunic();
                                        }
                                        return ScriptState.COMPLETED;
                                    }
                                });

                                addScriptAction(new TextboxScriptAction("Press 3 to activate it when in the Inferno Region."));
                            }});

                            // Even if the player selects no, still gets tunic
                            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                                addRequirement(new CustomRequirement() {
                                    @Override
                                    public boolean isRequirementMet() {
                                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                                        return answer == 1;
                                    }
                                });

                                addScriptAction(new TextboxScriptAction("Take it anyways — you might need it."));

                                addScriptAction(new ScriptAction() {
                                    @Override
                                    public ScriptState execute() {
                                        Player player = map.getPlayer();
                                        if (player instanceof Bee bee) {
                                            bee.obtainTunic();
                                        }
                                        return ScriptState.COMPLETED;
                                    }
                                });

                                addScriptAction(new TextboxScriptAction("Press 3 to activate it when in the Inferno Region."));
                            }});
                        }});

                        addScriptAction(new TextboxScriptAction("Good luck out there."));
                    }});
                }});
            }});
        }});

        scriptActions.add(new UnlockPlayerScriptAction());
        return scriptActions;
    }
}
