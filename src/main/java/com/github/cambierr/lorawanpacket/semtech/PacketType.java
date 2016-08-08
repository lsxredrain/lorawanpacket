/*
 * The MIT License
 *
 * Copyright 2016 cambierr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.cambierr.lorawanpacket.semtech;

import com.github.cambierr.lorawanpacket.lorawan.MalformedPacketException;

/**
 *
 * @author cambierr
 */
public enum PacketType {

    PUSH_DATA((byte) 0x00, PushData.class),
    PUSH_ACK((byte) 0x01, PushAck.class),
    PULL_DATA((byte) 0x02, PullData.class),
    PULL_ACK((byte) 0x04, PullAck.class),
    PULL_RESP((byte) 0x03, PullResp.class),
    TX_ACK((byte) 0x05, TxAck.class);

    private PacketType(byte _value, Class<? extends SemtechPacket> _mapper) {
        value = _value;
        mapper = _mapper;
    }

    private final byte value;
    private final Class<? extends SemtechPacket> mapper;

    public static PacketType from(byte _identifier) throws MalformedPacketException {
        for (PacketType v : values()) {
            if (v.value == _identifier) {
                return v;
            }
        }
        throw new MalformedPacketException("PacketType");
    }

    public Class<? extends SemtechPacket> getMapper() {
        return mapper;
    }

    public byte getValue() {
        return value;
    }
}
