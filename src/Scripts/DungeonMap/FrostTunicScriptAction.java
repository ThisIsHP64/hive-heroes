package Scripts.DungeonMap;

import Engine.Key;
import Flowers.Flower;
import Level.MapEntityStatus;
import Level.Player;
import Level.ScriptState;
import NPCs.FrostTunic;
import Players.Bee;
import ScriptActions.ScriptAction;

public class FrostTunicScriptAction extends ScriptAction {

    public ScriptState execute() {
        Player player = map.getPlayer();
        if (player instanceof Bee bee) {
            bee.obtainTunic();
            bee.obtainBlueTunic();
        }

        for (var npc : this.map.getNPCs()) {
            if (npc instanceof FrostTunic frostTunic) {
                frostTunic.setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
        return ScriptState.COMPLETED;
    }
}