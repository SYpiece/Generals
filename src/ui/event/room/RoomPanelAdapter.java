package ui.event.room;

public abstract class RoomPanelAdapter implements RoomPanelListener {
    @Override
    public void joinedRoom(RoomPanelEvent event) {}
    @Override
    public void createdRoom(RoomPanelEvent event) {}
}
