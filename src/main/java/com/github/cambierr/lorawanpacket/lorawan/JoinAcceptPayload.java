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
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cambierr
 */
public class JoinAcceptPayload implements FRMPayload {

    private MacPayload mac;
    private byte[] payload;
    private JoinAcceptClearPayload clear;

    private byte[] appKey;

    public JoinAcceptPayload(MacPayload _mac, ByteBuffer _raw) throws MalformedPacketException {
        if (_raw.remaining() < 16) {
            throw new MalformedPacketException("length");
        }
        mac = _mac;
        _raw.order(ByteOrder.LITTLE_ENDIAN);
        payload = new byte[_raw.remaining() - 4];
        _raw.get(payload);
    }
    
    public JoinAcceptPayload(MacPayload _mac) {
        mac = _mac;
    }

    @Override
    public int length() {
        return payload.length;
    }

    @Override
    public void toRaw(ByteBuffer _bb) {
        _bb.put(payload);
    }

    public MacPayload getMac() {
        return mac;
    }

    public JoinAcceptPayload setMac(MacPayload _mac) {
        this.mac = _mac;
        encryptPayload();
        return this;
    }

    private void decryptPayload() {

        if (appKey == null) {
            throw new RuntimeException("undefined appKey");
        }
        ByteBuffer a = ByteBuffer.allocate(4 + length());
        a.order(ByteOrder.LITTLE_ENDIAN);
        a.put(payload);
        a.put(mac.getPhyPayload().getMic());
        try {
            Key aesKey = new SecretKeySpec(appKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] s = cipher.doFinal(a.array());
            clear = new JoinAcceptClearPayload(s);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Could not decrypt payload", ex);
        }
    }

    private void encryptPayload() {
        if (appKey == null) {
            throw new RuntimeException("undefined appKey");
        }
        ByteBuffer a = ByteBuffer.allocate(4 + clear.length());
        a.order(ByteOrder.LITTLE_ENDIAN);
        clear.toRaw(a);
        a.put(clear.computeMic());
        try {
            Key aesKey = new SecretKeySpec(appKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] s = cipher.doFinal(a.array());
            payload = Arrays.copyOf(s, s.length - 4);
            mac.getPhyPayload().setMic(Arrays.copyOfRange(s, s.length - 4, s.length));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Could not encrypt payload", ex);
        }
    }

    public JoinAcceptClearPayload getClearPayload() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (clear == null) {
            decryptPayload();
        }
        return clear;
    }

    public JoinAcceptPayload setAppKey(byte[] _appKey) {
        if (clear != null && payload != null) {
            throw new RuntimeException("Both encrypted and clear payloads exists. AppKey can not be changed");
        }
        appKey = _appKey;
        return this;
    }

    public byte[] getAppKey() {
        return appKey;
    }

    @Override
    public boolean validateMic() {
        if (clear == null) {
            decryptPayload();
        }
        return Arrays.equals(clear.computeMic(), clear.mic);
    }

    public JoinAcceptClearPayload setNewClearPayload() {
        clear = new JoinAcceptClearPayload();
        encryptPayload();
        return clear;
    }

    public class JoinAcceptClearPayload {

        private byte[] appNonce = new byte[3];
        private byte[] netId = new byte[3];
        private byte[] devAddr = new byte[4];
        private byte dlSettings;
        private byte rxDelay;
        private byte[] cfList;
        private byte[] mic;

        public JoinAcceptClearPayload() {

        }

        public JoinAcceptClearPayload(byte[] _raw) {
            ByteBuffer bb = ByteBuffer.wrap(_raw);
            bb.get(appNonce);
            bb.get(netId);
            bb.get(devAddr);
            dlSettings = bb.get();
            rxDelay = bb.get();
            cfList = new byte[bb.remaining() - 4];
            bb.get(cfList);
            bb.get(mic);
        }

        public byte[] getAppNonce() {
            return appNonce;
        }

        public JoinAcceptClearPayload setAppNonce(byte[] _appNonce) {
            this.appNonce = _appNonce;
            encryptPayload();
            return this;
        }

        public byte[] getNetId() {
            return netId;
        }

        public JoinAcceptClearPayload setNetId(byte[] _netId) {
            this.netId = _netId;
            encryptPayload();
            return this;
        }

        public byte[] getDevAddr() {
            return devAddr;
        }

        public JoinAcceptClearPayload setDevAddr(byte[] _devAddr) {
            this.devAddr = _devAddr;
            encryptPayload();
            return this;
        }

        public byte getDlSettings() {
            return dlSettings;
        }

        public JoinAcceptClearPayload setDlSettings(byte _dlSettings) {
            this.dlSettings = _dlSettings;
            encryptPayload();
            return this;
        }

        public byte getRxDelay() {
            return rxDelay;
        }

        public JoinAcceptClearPayload setRxDelay(byte _rxDelay) {
            this.rxDelay = _rxDelay;
            encryptPayload();
            return this;
        }

        public byte[] getCfList() {
            return cfList;
        }

        public JoinAcceptClearPayload setCfList(byte[] _cfList) throws MalformedPacketException {
            if (_cfList.length != 0 && _cfList.length != 16) {
                throw new MalformedPacketException("cfList size");
            }
            this.cfList = _cfList;
            encryptPayload();
            return this;
        }

        public void toRaw(ByteBuffer _bb) {
            _bb.put(appNonce);
            _bb.put(netId);
            _bb.put(devAddr);
            _bb.put(dlSettings);
            _bb.put(rxDelay);
            _bb.put(cfList);
        }

        public int length() {
            return appNonce.length + netId.length + devAddr.length + 1 + 1 + cfList.length;
        }

        public byte[] computeMic() {
            if (appKey == null) {
                throw new RuntimeException("undefined appKey");
            }
            //size = mhdr + length()
            ByteBuffer body = ByteBuffer.allocate(1 + length());
            body.order(ByteOrder.LITTLE_ENDIAN);
            toRaw(body);
            body.put(mac.getPhyPayload().getMHDR());

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
    }

}
