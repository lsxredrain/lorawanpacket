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
package com.github.cambierr.lorawanpacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author cambierr
 */
public class PhyPayload {

    private byte mhdr;
    private MacPayload macPayload;
    private Direction direction;
    private byte[] mic;

    public PhyPayload(ByteBuffer _raw, Direction _dir) throws MalformedPacketException {
        _raw.order(ByteOrder.LITTLE_ENDIAN);
        if (_raw.remaining() < 12) {
            throw new MalformedPacketException();
        }
        direction = _dir;
        mhdr = _raw.get();
        macPayload = new MacPayload(this, _raw);
    }

    public MType getMType() throws MalformedPacketException {
        return MType.from(mhdr);
    }

    public MajorVersion getMajorVersion() throws MalformedPacketException {
        return MajorVersion.from(mhdr);
    }

    public byte getMHDR() {
        return mhdr;
    }

    public PhyPayload setMHDR(byte _mhdr) {
        mhdr = _mhdr;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public PhyPayload setDirection(Direction _dir) {
        direction = _dir;
        return this;
    }

    public MacPayload getMacPayload() {
        return macPayload;
    }

    public PhyPayload setMacPayload(MacPayload _macPayload) {
        macPayload = _macPayload;
        return this;
    }

    public void toRaw(ByteBuffer _bb) {
        _bb.put(mhdr);
        macPayload.toRaw(_bb);
        _bb.put(mic);
    }

    public byte[] getMic() {
        return mic;
    }

    public PhyPayload setMic(byte[] _mic) {
        this.mic = _mic;
        return this;
    }

}
