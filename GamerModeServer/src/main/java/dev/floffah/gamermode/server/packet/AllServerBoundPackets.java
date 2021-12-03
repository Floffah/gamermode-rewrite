package dev.floffah.gamermode.server.packet;

import dev.floffah.gamermode.server.packet.login.EncryptionResponse;
import dev.floffah.gamermode.server.packet.login.LoginStart;
import dev.floffah.gamermode.server.packet.play.message.PluginMessage;
import dev.floffah.gamermode.server.packet.serverlist.Handshake;
import dev.floffah.gamermode.server.packet.serverlist.Ping;
import dev.floffah.gamermode.server.packet.serverlist.Request;
import dev.floffah.gamermode.server.socket.ConnectionState;

public enum AllServerBoundPackets {
    // HANDSHAKE
    HANDSHAKE(0x00, ConnectionState.HANDSHAKE, Handshake.class),

    // STATUS
    REQUEST(0x00, ConnectionState.STATUS, Request.class),
    PING(0x01, ConnectionState.STATUS, Ping.class),

    // LOGIN
    LOGIN_START(0x00, ConnectionState.LOGIN, LoginStart.class),
    ENCRYPTION_RESPONSE(0x01, ConnectionState.LOGIN, EncryptionResponse.class),

    // PLAY
    PLUGIN_MESSAGE(0x0A, ConnectionState.PLAY, PluginMessage.class);

    public final int id;
    public final ConnectionState state;
    public final Class<? extends BasePacket> packet;

    AllServerBoundPackets(
        int id,
        ConnectionState state,
        Class<? extends BasePacket> packet
    ) {
        this.id = id;
        this.state = state;
        this.packet = packet;
    }
}
