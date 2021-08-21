package net.taccy.spleef.game;

public enum GameStateType {

    WAITING ("Waiting"), COUNTDOWN ("Countdown"), GRACE ("Grace Period"), ACTIVE ("Active"), END ("Ending");

    private final String displayName;

    GameStateType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
