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
import com.github.cambierr.lorawanpacket.lorawan.PhyPayload;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import org.json.JSONObject;

/**
 *
 * @author cambierr
 */
public class Rxpk {

    private String time;
    private int tmst;
    private double freq;
    private int chan;
    private int rfch;
    private int stat;
    private Modulation modu;
    private Object datr;
    private String codr;
    private int rssi;
    private double lsnr;
    private int size;
    private PhyPayload data;

    public Rxpk(JSONObject _json) throws MalformedPacketException {

        /**
         * tmst
         */
        if (!_json.has("tmst")) {
            throw new MalformedPacketException("missing tmst");
        } else {
            tmst = _json.getInt("tmst");
        }

        /**
         * time
         */
        if (!_json.has("time")) {
            throw new MalformedPacketException("missing time");
        } else {
            time = _json.getString("time");
        }

        /**
         * chan
         */
        if (!_json.has("chan")) {
            throw new MalformedPacketException("missing chan");
        } else {
            chan = _json.getInt("chan");
        }

        /**
         * rfch
         */
        if (!_json.has("rfch")) {
            throw new MalformedPacketException("missing rfch");
        } else {
            rfch = _json.getInt("rfch");
        }

        /**
         * freq
         */
        if (!_json.has("freq")) {
            throw new MalformedPacketException("missing freq");
        } else {
            freq = _json.getDouble("stat");
        }

        /**
         * stat
         */
        if (!_json.has("stat")) {
            throw new MalformedPacketException("missing stat");
        } else {
            stat = _json.getInt("stat");
            if (stat > 1 || stat < -1) {
                throw new MalformedPacketException("stat must be equal to -1, 0, or 1");
            }
        }

        /**
         * modu
         */
        if (!_json.has("modu")) {
            throw new MalformedPacketException("missing modu");
        } else {
            modu = Modulation.parse(_json.getString("modu"));
        }

        /**
         * datr
         */
        if (!_json.has("datr")) {
            throw new MalformedPacketException("missing datr");
        } else {
            switch (modu) {
                case FSK:
                    datr = _json.getInt("datr");
                    break;
                case LORA:
                    datr = _json.getString("datr");
                    break;
            }
        }

        /**
         * codr
         */
        if (!_json.has("codr")) {
            if (modu.equals(Modulation.FSK)) {
                codr = null;
            } else {
                throw new MalformedPacketException("missing codr");
            }
        } else {
            codr = _json.getString("codr");
        }

        /**
         * rssi
         */
        if (!_json.has("rssi")) {
            throw new MalformedPacketException("missing rssi");
        } else {
            rssi = _json.getInt("rssi");
        }

        /**
         * lsnr
         */
        if (!_json.has("lsnr")) {
            if (modu.equals(Modulation.FSK)) {
                lsnr = Double.MIN_VALUE;
            } else {
                throw new MalformedPacketException("missing lsnr");
            }
        } else {
            lsnr = _json.getDouble("lsnr");
        }

        /**
         * size
         */
        if (!_json.has("size")) {
            throw new MalformedPacketException("missing size");
        } else {
            size = _json.getInt("size");
        }

        /**
         * data
         */
        if (!_json.has("data")) {
            throw new MalformedPacketException("missing data");
        } else {
            byte[] raw;

            try {
                raw = Base64.getDecoder().decode(_json.getString("data"));
            } catch (IllegalArgumentException ex) {
                throw new MalformedPacketException("malformed data");
            }

            data = new PhyPayload(ByteBuffer.wrap(raw));
        }
    }

    private Rxpk() {
        time = null;
        tmst = Integer.MIN_VALUE;
        freq = Double.MIN_VALUE;
        chan = Integer.MIN_VALUE;
        rfch = Integer.MIN_VALUE;
        stat = Integer.MIN_VALUE;
        modu = null;
        datr = null;
        codr = null;
        rssi = Integer.MIN_VALUE;
        lsnr = Double.MIN_VALUE;
        size = Integer.MIN_VALUE;
        data = null;
    }

    public String getTime() {
        return time;
    }

    public int getTmst() {
        return tmst;
    }

    public double getFreq() {
        return freq;
    }

    public int getChan() {
        return chan;
    }

    public int getRfch() {
        return rfch;
    }

    public int getStat() {
        return stat;
    }

    public Modulation getModu() {
        return modu;
    }

    public Object getDatr() {
        return datr;
    }

    public String getCodr() {
        return codr;
    }

    public int getRssi() {
        return rssi;
    }

    public double getLsnr() {
        return lsnr;
    }

    public int getSize() {
        return size;
    }

    public PhyPayload getData() {
        return data;
    }

    public static class Builder {

        private final Rxpk instance;

        public Builder() {
            instance = new Rxpk();
        }

        public Builder setTime(String _time) {
            instance.time = _time;
            return this;
        }

        public Builder setTmst(int _tmst) {
            instance.tmst = _tmst;
            return this;
        }

        public Builder setFreq(double _freq) {
            instance.freq = _freq;
            return this;
        }

        public Builder setChan(int _chan) {
            instance.chan = _chan;
            return this;
        }

        public Builder setRfch(int _rfch) {
            instance.rfch = _rfch;
            return this;
        }

        public Builder setStat(int _stat) {
            if (_stat > 1 || _stat < -1) {
                throw new IllegalArgumentException("stat must be equal to -1, 0, or 1");
            }
            instance.stat = _stat;
            return this;
        }

        public Builder setModu(Modulation _modu) {
            instance.modu = _modu;
            return this;
        }

        public Builder setDatr(int _datr) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.LORA)) {
                throw new IllegalArgumentException("integer datr shoud be used for FSK modulation");
            }
            instance.datr = _datr;
            return this;
        }

        public Builder setDatr(String _datr) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.FSK)) {
                throw new IllegalArgumentException("string datr shoud be used for LORA modulation");
            }
            instance.datr = _datr;
            return this;
        }

        public Builder setCodr(String _codr) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.FSK)) {
                throw new IllegalArgumentException("codr shoud be used for LORA modulation");
            }
            instance.codr = _codr;
            return this;
        }

        public Builder setRssi(int _rssi) {
            instance.rssi = _rssi;
            return this;
        }

        public Builder setLsnr(double _lsnr) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.FSK)) {
                throw new IllegalArgumentException("lsnr shoud be used for LORA modulation");
            }
            instance.lsnr = _lsnr;
            return this;
        }

        public Builder setSize(int _size) {
            instance.size = _size;
            return this;
        }

        public Builder setData(PhyPayload _data) {
            instance.data = _data;
            return this;
        }

        public Rxpk build() {
            return instance;
        }

    }

    public JSONObject toJson() throws MalformedPacketException {
        JSONObject output = new JSONObject();

        time = null;
        tmst = Integer.MIN_VALUE;
        freq = Double.MIN_VALUE;
        chan = Integer.MIN_VALUE;
        rfch = Integer.MIN_VALUE;
        stat = Integer.MIN_VALUE;
        modu = null;
        datr = null;
        codr = null;
        rssi = Integer.MIN_VALUE;
        lsnr = Double.MIN_VALUE;
        size = Integer.MIN_VALUE;
        data = null;

        output.put("time", time);
        output.put("tmst", tmst);
        output.put("freq", freq);
        output.put("chan", chan);
        output.put("rfch", rfch);
        output.put("stat", stat);
        output.put("modu", modu.name());
        
        if (modu.equals(Modulation.LORA)) {
            output.put("codr", codr);
            output.put("lsnr", lsnr);
        }
        
        output.put("datr", datr);
        output.put("rssi", rssi);
        output.put("size", size);
        
        ByteBuffer bb = ByteBuffer.allocate(384);
        data.toRaw(bb);
        output.put("data", Base64.getEncoder().encodeToString(Arrays.copyOfRange(bb.array(), 0, bb.capacity() - bb.remaining())));
        
        return output;
    }

}
