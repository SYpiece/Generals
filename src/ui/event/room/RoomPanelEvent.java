package ui.event.room;

import event.Event;

public class RoomPanelEvent extends Event {
    public static final int joinedRoom = 1, createdRoom = 2;
    public RoomPanelEvent(int id) {
        super(id);
    }
}
