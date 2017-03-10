package org.ndsc.mimicIO.mip;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by timvi on 06.08.2015.
 */
public abstract class NetIPConverter {

    /**
     * Parse concrete message.
     *
     * @param data the data
     * @return the message
     */
    public static Message parseConcreteMessage(byte[] data) {
        return NetIPUtils.ConcretizeMessage(parseRawMessage(data));
    }


    /**
     * Parse raw message.
     *
     * @param data the data
     * @return the message
     */
    public static Message parseRawMessage(byte[] data) {
        return new Message(parseHeader(Arrays.copyOfRange(data, 0, MessageHeader.HEADER_BYTES)), Arrays.copyOfRange(data, MessageHeader.HEADER_BYTES, data.length));
    }

    /**
     * Parse header.
     *
     * @param data the data
     * @return the message header
     */
    public static MessageHeader parseHeader(byte[] data) {
        if (data.length != MessageHeader.HEADER_BYTES)
            throw new IllegalArgumentException("Header byte size has to be " + MessageHeader.HEADER_BYTES);
        MessageHeader header = new MessageHeader();
        header.setNetIDEProtocolVersion(NetIDEProtocolVersion.parse(data[0]));
        header.setMessageType(MessageType.parse(data[1]));
        header.setPayloadLength(ByteBuffer.wrap(data, 2, 2).getShort());
        header.setTransactionId(ByteBuffer.wrap(data, 4, 4).getInt());
        header.setModuleId(ByteBuffer.wrap(data, 8, 4).getInt());
        header.setDatapathId(ByteBuffer.wrap(data, 12, 8).getLong());
        return header;
    }
}
