package ui.event.room;

import event.EventListener;

public interface RoomPanelListener extends EventListener<RoomPanelEvent> {
    void joinedRoom(RoomPanelEvent event);
    void createdRoom(RoomPanelEvent event);
}
