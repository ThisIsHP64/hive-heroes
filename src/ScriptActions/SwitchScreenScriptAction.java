package ScriptActions;

import Level.ScriptState;

public class SwitchScreenScriptAction extends ScriptAction {
    
    // add instance vars/constructor mapping if needed...
    public SwitchScreenScriptAction() {

    }

    // this method is only run once when script action is loaded up (before execute)
    // it is optional
    @Override
    public void setup() {
    }

    // this method is called once every frame while the script action is active
    // it is where the actual logic should take place for carrying out a specific event
    @Override
    public ScriptState execute() {
        // ... script action execute logic
        return ScriptState.COMPLETED;
    }

    // this method is only run once after script action has completed
    // it is optional
    @Override
    public void cleanup() {

    }





}
