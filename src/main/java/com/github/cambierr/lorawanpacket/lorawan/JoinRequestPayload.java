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
public class JoinRequestPayload implements FRMPayload {

    private MacPayload mac;
    private byte[] appEUI = new byte[8];
    private byte[] devEUI = new byte[8];
    private byte[] devNonce = new byte[2];

    private byte[] appKey;

    public JoinRequestPayload(MacPayload _mac, ByteBuffer _raw) throws MalformedPacketException {
        if (_raw.remaining() < 22) {
            throw new MalformedPacketException("length");
        }
        mac = _mac;
        _raw.order(ByteOrder.LITTLE_ENDIAN);
        _raw.get(appEUI);
        _raw.get(devEUI);
        _raw.get(devNonce);
    }

    public JoinRequestPayload(MacPayload _mac) {
        mac = _mac;
    }

    public byte[] computeMic() {
        if (appKey == null) {
            throw new RuntimeException("undefined appKey");
        }
        //size = mhdr + length()
        ByteBuffer body = ByteBuffer.allocate(1 + length());
        body.order(ByteOrder.LITTLE_ENDIAN);

        body.put(mac.getPhyPayload().getMHDR());
        toRaw(body);

        AesCmac aesCmac;
        try {
            aesCmac = new AesCmac();
            aesCmac.init(new SecretKeySpec(appKey, "AES"));
            aesCmac.updateBlock(body.array());
            return Arrays.copyOfRange(aesCmac.doFinal(), 0, 4);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Could not compute AesCmac", ex);
        }
    }

    @Override
    public int length() {
        return 18;
    }

    @Override
    public void toRaw(ByteBuffer _bb) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        _bb.put(getAppEUI());
        _bb.put(getDevEUI());
        _bb.put(getDevNonce());
    }

    public MacPayload getMac() {
        return mac;
    }

    public JoinRequestPayload setMac(MacPayload _mac) {
        this.mac = _mac;
        return this;
    }

    public byte[] getAppEUI() {
        return appEUI;
    }

    public JoinRequestPayload setAppEUI(byte[] _appEUI) {
        this.appEUI = _appEUI;
        return this;
    }

    public byte[] getDevEUI() {
        return devEUI;
    }

    public JoinRequestPayload setDevEUI(byte[] _devEUI) {
        this.devEUI = _devEUI;
        return this;
    }

    public byte[] getDevNonce() {
        return devNonce;
    }

    public JoinRequestPayload setDevNonce(byte[] _devNonce) {
        this.devNonce = _devNonce;
        return this;
    }

    public JoinRequestPayload setAppKey(byte[] _appKey) {
        appKey = _appKey;
        return this;
    }

    public byte[] getAppKey() {
        return appKey;
    }

    @Override
    public boolean validateMic() {
        return Arrays.equals(computeMic(), mac.getPhyPayload().getMic());
    }

}
