package org.firstinspires.ftc.teamcode.example.util;

public enum Junction {
    HIGH(1600),
    MEDIUM(1200),
    LOW(800),
    GROUND(200),
    NONE(0);

    final int tick;
    Junction(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }
}
