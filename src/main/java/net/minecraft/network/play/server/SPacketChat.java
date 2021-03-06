package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class SPacketChat implements Packet<INetHandlerPlayClient>
{
    private ITextComponent chatComponent;
    private ChatType type;
    public net.md_5.bungee.api.chat.BaseComponent[] components; // Spigot

    public SPacketChat()
    {
    }

    public SPacketChat(ITextComponent componentIn)
    {
        this(componentIn, ChatType.SYSTEM);
    }

    public SPacketChat(ITextComponent message, ChatType type)
    {
        this.chatComponent = message;
        this.type = type;
    }

    // PFServer start
    public SPacketChat(ITextComponent message, byte type)
    {
        this.chatComponent = message;
        this.type = ChatType.byId(type);
    }
    // PFServer end

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chatComponent = buf.readTextComponent();
        this.type = ChatType.byId(buf.readByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        // Spigot start
        if (components != null) {
            buf.writeString(net.md_5.bungee.chat.ComponentSerializer.toString(components));
        } else {
            buf.writeTextComponent(this.chatComponent);
        }
        // Spigot end
        buf.writeByte(this.type.getId());
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChat(this);
    }

    @SideOnly(Side.CLIENT)
    public ITextComponent getChatComponent()
    {
        return this.chatComponent;
    }

    public boolean isSystem()
    {
        return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
    }

    public ChatType getType()
    {
        return this.type;
    }
}