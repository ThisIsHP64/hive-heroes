package Scripts.DungeonMap;

import Level.MapEntityStatus;
import Level.NPC;
import Level.Player;
import Level.ScriptState;
import NPCs.FrostTunic;
import Players.Bee;
import ScriptActions.ScriptAction;

public class FrostTunicScriptAction extends ScriptAction {

    @Override
    public ScriptState execute() {
        Player player = map.getPlayer();
        if (player instanceof Bee bee) {
            bee.obtainBlueTunic();
        }

        for (NPC npc : this.map.getNPCs()) {
            if (npc instanceof FrostTunic) {
                FrostTunic frostTunic = (FrostTunic) npc;
                frostTunic.setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
        return ScriptState.COMPLETED;
    }
}