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
package com.github.cambierr.lorawanpacket.lorawan;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author cambierr
 */
public class MacPayload {

    private FHDR fhdr;
    private byte fPort;
    private FRMPayload payload;
    private PhyPayload phy;

    public MacPayload(PhyPayload _phy, ByteBuffer _raw) throws MalformedPacketException {
        phy = _phy;
        _raw.order(ByteOrder.LITTLE_ENDIAN);
        if (_raw.remaining() < 1) {
            throw new MalformedPacketException();
        }
        fhdr = new FHDR(_raw);
        //bigger than 4, since the MIC if after
        if (_raw.remaining() > 4) {
            fPort = _raw.get();
            Class<? extends FRMPayload> mapper = phy.getMType().getMapper();
            try {
                payload = mapper.getConstructor(MacPayload.class, ByteBuffer.class).newInstance(this, _raw);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException("Could not create FRMPayload", ex);
            }
        } else {
            fPort = 0;
            payload = null;
        }
    }
    
    public MacPayload(PhyPayload _phy){
        phy = _phy;
        phy.setMacPayload(this);
    }

    public int length() throws MalformedPacketException {
        if (payload == null) {
            return fhdr.length();
        }
        return fhdr.length() + 1 + payload.length();
    }

    public void toRaw(ByteBuffer _bb) throws MalformedPacketException {
        fhdr.toRaw(_bb);
        if (payload != null) {
            _bb.put(fPort);
            payload.toRaw(_bb);
        }
    }

    public FHDR getFhdr() {
        return fhdr;
    }

    public MacPayload setFhdr(FHDR _fhdr) {
        this.fhdr = _fhdr;
        return this;
    }

    public byte getfPort() {
        return fPort;
    }

    public MacPayload setfPort(byte _fPort) {
        this.fPort = _fPort;
        return this;
    }

    public FRMPayload getPayload() {
        return payload;
    }

    public MacPayload setPayload(FRMPayload _payload) {
        this.payload = _payload;
        return this;
    }

    public PhyPayload getPhyPayload() {
        return phy;
    }

    public MacPayload setPhyPayload(PhyPayload _phy) {
        this.phy = _phy;
        return this;
    }

    

}
