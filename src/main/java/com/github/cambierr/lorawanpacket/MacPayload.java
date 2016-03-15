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
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cambierr
 */
public class MacPayload {

    public FHDR fhdr;
    public byte fPort;
    public byte[] payload;
    public byte[] clearPayload;
    public Direction dir;

    public MacPayload(ByteBuffer _raw, Direction _dir) throws MalformedPacketException {
        if (_raw.remaining() < 1) {
            throw new MalformedPacketException();
        }
        dir = _dir;
        fhdr = new FHDR(_raw);
        //bigger than 4, since the MIC if after
        if (_raw.remaining() > 4) {
            fPort = _raw.get();
            payload = new byte[_raw.remaining() - 4];
            _raw.get(payload);
        } else {
            fPort = 0;
            payload = new byte[0];
        }
    }

    public MacPayload(ByteBuffer _raw, Direction _dir, byte[] _appSKey) throws MalformedPacketException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        this(_raw, _dir);
        clearPayload = getClearPayLoad(_appSKey);
    }

    public int length() {
        return fhdr.length() + 1 + payload.length;
    }

    public void toRaw(ByteBuffer _bb) {
        fhdr.toRaw(_bb);
        _bb.put(fPort);
        _bb.put(payload);
    }

    private byte[] getClearPayLoad(byte[] _appSKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        int k = (int) Math.ceil(payload.length / 16.0);
        ByteBuffer a = ByteBuffer.allocate(16 * k);
        a.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 1; i <= k; i++) {
            a.put((byte) 0x01);
            a.put(new byte[]{0x00, 0x00, 0x00, 0x00});
            a.put(dir.value());
            a.put(fhdr.devAddr);
            a.putShort(fhdr.fCnt);
            a.put(new byte[]{0x00, 0x00});
            a.put((byte) 0x00);
            a.put((byte) i);
        }
        Key aesKey = new SecretKeySpec(_appSKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] s = cipher.doFinal(a.array());
        byte[] paddedPayload = new byte[16 * k];
        System.arraycopy(payload, 0, paddedPayload, 0, payload.length);
        byte[] plainPayload = new byte[payload.length];
        for (int i = 0; i < payload.length; i++) {
            plainPayload[i] = (byte) (s[i] ^ paddedPayload[i]);
        }
        return plainPayload;
    }
}
