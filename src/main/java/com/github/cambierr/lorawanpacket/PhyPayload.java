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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cambierr
 */
public class PhyPayload {

    public byte mhdr;
    public MacPayload macPayload;
    public byte[] mic = new byte[4];
    public Direction dir;

    public PhyPayload(ByteBuffer _raw, Direction _dir, byte[] _nwkSKey) throws MalformedPacketException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        _raw.order(ByteOrder.LITTLE_ENDIAN);
        if (_raw.remaining() < 12) {
            throw new MalformedPacketException();
        }
        dir = _dir;
        mhdr = _raw.get();
        macPayload = new MacPayload(_raw, _dir);
        _raw.get(mic);
        if (!Arrays.equals(mic, computeMic(_nwkSKey))) {
            throw new MalformedPacketException("mic");
        }
    }

    public final byte[] computeMic(byte[] _nwkSKey) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        ByteBuffer body = ByteBuffer.allocate(1 + macPayload.length() + 16);
        body.order(ByteOrder.LITTLE_ENDIAN);

        body.put((byte) 0x49);
        body.put(new byte[]{0x00, 0x00, 0x00, 0x00});
        body.put(dir.value());
        body.put(macPayload.fhdr.devAddr);
        body.putShort(macPayload.fhdr.fCnt);
        body.put(new byte[]{0x00, 0x00});
        body.put((byte) 0x00);
        body.put((byte) (1 + macPayload.length()));

        body.put(mhdr);
        macPayload.toRaw(body);

        AesCmac aesCmac = new AesCmac();
        aesCmac.init(new SecretKeySpec(_nwkSKey, "AES"));
        aesCmac.updateBlock(body.array());
        return Arrays.copyOfRange(aesCmac.doFinal(), 0, 4);
    }

}
