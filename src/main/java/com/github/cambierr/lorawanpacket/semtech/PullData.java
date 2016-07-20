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
import java.nio.ByteBuffer;

/**
 *
 * @author cambierr
 */
public class PullData extends SemtechPacket {

    private byte[] gatewayEui;

    public PullData(byte[] _randoms, ByteBuffer _raw) throws MalformedPacketException {
        super(_randoms, PacketType.PULL_DATA);

        if (_raw.remaining() < 8) {
            throw new MalformedPacketException("too short");
        }

        gatewayEui = new byte[8];
        _raw.get(gatewayEui);
    }

    private PullData(byte[] _randoms) {
        super(_randoms, PacketType.PULL_ACK);
    }

    public static class Builder {

        private final PullData instance;

        public Builder(byte[] _randoms) {
            instance = new PullData(_randoms);
        }

        public Builder setGatewayEui(byte[] _gatewayEui) {
            instance.gatewayEui = _gatewayEui;
            return this;
        }

        public PullData build() {
            return instance;
        }

    }

    public byte[] getGatewayEui() {
        return gatewayEui;
    }

    @Override
    public void toRaw(ByteBuffer _bb) throws MalformedPacketException {
        super.toRaw(_bb);
        _bb.put(gatewayEui);
    }

}
