package org.ndsc.mimicIO.mip;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.projectfloodlight.openflow.protocol.OFMessage;

/**
 * Class representing a message of type OPENFLOW.
 * Note that this only serves as a convenience class - if the MessageType is manipulated, the class will not recognize that.
 */
public class OpenFlowMessage extends Message {
    private OFMessage ofMessage;

    /**
     * Instantiates a new Open flow message.
     */
    public OpenFlowMessage() {
        super(new MessageHeader(), new byte[0]);
        header.setMessageType(MessageType.OPENFLOW);
    }

    /**
     * Gets of message.
     *
     * @return the of message
     */
    public OFMessage getOfMessage() {
        return ofMessage;
    }

    /**
     * Sets of message.
     *
     * @param ofMessage the of message
     */
    public void setOfMessage(OFMessage ofMessage) {
        this.ofMessage = ofMessage;
    }

    @Override
    public byte[] getPayload() {
        ChannelBuffer dcb = ChannelBuffers.dynamicBuffer();
        ofMessage.writeTo(dcb);
        this.payload = new byte[dcb.readableBytes()];
        dcb.readBytes(this.payload, 0, this.payload.length);
        return this.payload;
    }

    @Override
    public String toString() {
        return "OpenFlowMessage [Header=" + header.toString() + ",Type=" + ofMessage.getType().toString() + ",OFMessage=" + ofMessage.toString() + "]";
    }
}
