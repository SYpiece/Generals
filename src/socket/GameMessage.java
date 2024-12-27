package socket;

import java.io.Serializable;

public class GameMessage implements Serializable {
//    private final MessageType _type;
    protected final int _messageType;
    private final Object _data;
//    public MessageType getType() {
//        return _type;
//    }
    public int getMessageType() {
        return _messageType;
    }
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) _data;
    }
//    public GameMessage(MessageType type) {
//        this(type, null);
//    }
//    public GameMessage(MessageType type, Object data) {
//        _type = type;
//        _data = data;
//    }
    public GameMessage(int messageType) {
        this(messageType, null);
    }
    public GameMessage(int messageType, Object data) {
        _messageType = messageType;
        _data = data;
    }
//    public enum MessageType implements Serializable {
//        /**
//         * 游戏模型发生变化
//         */
//        GameModelChanged,
//        /**
//         * 多个游戏模型发生变化
//         */
//        GameModelsChanged,
//        /**
//         * 创建游戏模型
//         */
//        GameModelCreating,
//        /**
//         * 创建多个游戏模型
//         */
//        GameModelsCreating,
//        /**
//         * 读取玩家的操作指令
//         */
//        ReadPlayerOrder,
//        /**
//         * 写出玩家的操作指令
//         */
//        WritePlayerOrder,
//        /**
//         * 读取玩家的聊天消息
//         */
//        ReadPlayerMessage,
//        /**
//         * 写出玩家的聊天消息
//         */
//        WritePlayerMessage,
//        /**
//         * 游戏模型销毁
//         */
//        GameModelReleased,
//        /**
//         * 多个游戏模型销毁
//         */
//        GameModelsReleased,
//        /**
//         * 所有游戏模型销毁
//         */
//        GameAllModelsReleased,
//        /**
//         * 读取玩家的信息
//         */
//        ReadPlayerInformation,
//        /**
//         * 写入玩家的信息
//         */
//        WritePlayerInformation,



//        PlayerJoined,
//        PlayerExited,
//        PlayerMessaged,
//        /**
//         * 客户端通知服务器或服务器通知客户端玩家的强制开始状态改变
//         */
//        PlayerForceState,
//        PlayerTeamChanged,
//        /**
//         * 服务器通知客户端游戏状态发生改变
//         */
//        GameStateChanged,
//        /**
//         * 服务器通知客户端游戏地图更新
//         */
//        GameMapUpdate,
//        GamePlayerInitialized,
//    }
    public static final int PLAYER_JOINED = 5168;
    public static final int PLAYER_EXITED = 3607;
    public static final int PLAYER_MESSAGED = 9780;
    public static final int PLAYER_FORCE_STATE_CHANGED = 514;
    public static final int PLAYER_TEAM_CHANGED = 3538;
    public static final int GAME_STATE_CHANGED = 3585;
    public static final int GAME_MAP_UPDATED = 6153;
    public static final int GAME_PLAYER_INITIALIZED = 5794;
    public static final int GAME_PLAYER_DEFEATED = 6757;
    public static final int GAME_PLAYER_ACTION = 1276;
}
