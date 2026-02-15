package dev.artixdev.practice.models;

public class ServerInfo {
    
    public static final int DEFAULT_PORT = 25565;
    
    private final String serverName;
    private final String serverAddress;
    private final int port;
    private final String motd;
    private final int maxPlayers;

    public ServerInfo(String serverName, String serverAddress, int port, String motd, int maxPlayers) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.port = port;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }

    public String getMotd() {
        return motd;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getFullAddress() {
        return serverAddress + ":" + port;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "serverName='" + serverName + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", port=" + port +
                ", motd='" + motd + '\'' +
                ", maxPlayers=" + maxPlayers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerInfo that = (ServerInfo) o;
        return port == that.port &&
                maxPlayers == that.maxPlayers &&
                serverName.equals(that.serverName) &&
                serverAddress.equals(that.serverAddress) &&
                motd.equals(that.motd);
    }

    @Override
    public int hashCode() {
        int result = serverName.hashCode();
        result = 31 * result + serverAddress.hashCode();
        result = 31 * result + port;
        result = 31 * result + motd.hashCode();
        result = 31 * result + maxPlayers;
        return result;
    }
}