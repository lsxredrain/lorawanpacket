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
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author cambierr
 */
public abstract class SemtechPacket {

    private byte version;
    private final byte[] randoms;
    private final PacketType identifier;

    protected SemtechPacket(byte[] _randoms, PacketType _identifier) {
        randoms = _randoms;
        identifier = _identifier;
    }

    public static SemtechPacket parse(ByteBuffer _raw) throws MalformedPacketException {
        _raw.order(ByteOrder.LITTLE_ENDIAN);

        if (_raw.remaining() < 4) {
            throw new MalformedPacketException("too short");
        }

        byte version = _raw.get();

        if (version != 0x02) {
            throw new MalformedPacketException("Unsupported version");
        }

        byte[] randoms = new byte[2];

        _raw.get(randoms);

        Class<? extends SemtechPacket> mapper = PacketType.from(_raw.get()).getMapper();
        try {
            return mapper.getConstructor(byte[].class, ByteBuffer.class).newInstance(randoms, _raw);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Could not create SemtechPacket", ex);
        }
    }

    public byte getVersion() {
        return version;
    }

    public byte[] getRandoms() {
        return randoms;
    }

    public PacketType getIdentifier() {
        return identifier;
    }
    
    public void toRaw(ByteBuffer _bb) throws MalformedPacketException{
        _bb.put((byte)0x02);
        _bb.put(randoms);
        _bb.put(identifier.getValue());
    };

}
