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

/**
 *
 * @author cambierr
 */
public class FHDR {

    private byte[] devAddr = new byte[4];
    private byte fCtrl;
    private short fCnt;
    private byte[] fOpts;

    public FHDR(ByteBuffer _raw) {
        _raw.get(devAddr);
        fCtrl = _raw.get();
        fCnt = _raw.getShort();
        fOpts = new byte[fCtrl & 0xf];
        _raw.get(fOpts);
    }
    
    public FHDR(){
        
    }

    public int length() {
        return devAddr.length + 1 + 2 + fOpts.length;
    }

    public void toRaw(ByteBuffer _bb) {
        _bb.put(devAddr);
        _bb.put(fCtrl);
        _bb.putShort(fCnt);
        _bb.put(fOpts);
    }

    public byte[] getDevAddr() {
        return devAddr;
    }

    public FHDR setDevAddr(byte[] _devAddr) {
        this.devAddr = _devAddr;
        return this;
    }

    public byte getfCtrl() {
        return fCtrl;
    }

    public FHDR setfCtrl(byte _fCtrl) {
        this.fCtrl = _fCtrl;
        return this;
    }

    public short getfCnt() {
        return fCnt;
    }

    public FHDR setfCnt(short _fCnt) {
        this.fCnt = _fCnt;
        return this;
    }

    public byte[] getfOpts() {
        return fOpts;
    }

    public FHDR setfOpts(byte[] _fOpts) {
        this.fOpts = _fOpts;
        return this;
    }
}
