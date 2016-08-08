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
public class Txpk {

    private boolean imme;
    private int tmst;
    private String time;
    private double freq;
    private int rfch;
    private int powe;
    private Modulation modu;
    private Object datr;
    private String codr;
    private int fdev;
    private boolean ipol;
    private int prea;
    private int size;
    private PhyPayload data;
    private boolean ncrc;

    public Txpk(JSONObject _json) throws MalformedPacketException {

        /**
         * imme
         */
        if (!_json.has("imme")) {
            imme = false;
        } else {
            imme = _json.getBoolean("imme");
        }

        /**
         * tmst
         */
        if (!_json.has("tmst")) {
            tmst = Integer.MAX_VALUE;
        } else {
            tmst = _json.getInt("tmst");
        }

        /**
         * time
         */
        if (!_json.has("time")) {
            time = null;
        } else {
            time = _json.getString("time");
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
         * powe
         */
        if (!_json.has("powe")) {
            throw new MalformedPacketException("missing powe");
        } else {
            powe = _json.getInt("powe");
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
         * fdev
         */
        if (!_json.has("fdev")) {
            if (modu.equals(Modulation.LORA)) {
                fdev = Integer.MAX_VALUE;
            } else {
                throw new MalformedPacketException("missing fdev");
            }
        } else {
            fdev = _json.getInt("fdev");
        }

        /**
         * ipol
         */
        if (!_json.has("ipol")) {
            if (modu.equals(Modulation.FSK)) {
                ipol = false;
            } else {
                throw new MalformedPacketException("missing ipol");
            }
        } else {
            ipol = _json.getBoolean("ipol");
        }

        /**
         * prea
         */
        if (!_json.has("prea")) {
            throw new MalformedPacketException("missing prea");
        } else {
            prea = _json.getInt("prea");
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

        /**
         * ncrc
         */
        if (!_json.has("ncrc")) {
            ncrc = false;
        } else {
            ncrc = _json.getBoolean("ncrc");
        }
    }

    private Txpk() {
        imme = false;
        tmst = Integer.MIN_VALUE;
        time = null;
        freq = Double.MIN_VALUE;
        rfch = Integer.MIN_VALUE;
        powe = Integer.MIN_VALUE;
        modu = null;
        datr = null;
        codr = null;
        fdev = Integer.MIN_VALUE;
        ipol = false;
        prea = Integer.MIN_VALUE;
        size = Integer.MIN_VALUE;
        data = null;
        ncrc = false;
    }

    public static class Builder {

        private final Txpk instance;

        public Builder() {
            instance = new Txpk();
        }

        public Builder setImme(boolean _imme) {
            instance.imme = _imme;
            return this;
        }

        public Builder setTmst(int _tmst) {
            instance.tmst = _tmst;
            return this;
        }

        public Builder setTime(String _time) {
            /**
             * @todo: validate time format
             */
            instance.time = _time;
            return this;
        }

        public Builder setFreq(double _freq) {
            instance.freq = _freq;
            return this;
        }

        public Builder setRfch(int _rfch) {
            instance.rfch = _rfch;
            return this;
        }

        public Builder setPowe(int _powe) {
            instance.powe = _powe;
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

        public Builder setFdev(int _fdev) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.LORA)) {
                throw new IllegalArgumentException("fdev shoud be used for FSK modulation");
            }
            instance.fdev = _fdev;
            return this;
        }

        public Builder setIpol(boolean _ipol) {
            if (instance.getModu() == null || instance.getModu().equals(Modulation.FSK)) {
                throw new IllegalArgumentException("ipol shoud be used for LORA modulation");
            }
            instance.ipol = _ipol;
            return this;
        }

        public Builder setPrea(int _prea) {
            instance.prea = _prea;
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

        public Builder setNcrc(boolean _ncrc) {
            instance.ncrc = _ncrc;
            return this;
        }

        public Txpk build() {
            /**
             * @todo: validate config ?
             */
            return instance;
        }
    }

    public JSONObject toJson() throws MalformedPacketException {
        JSONObject output = new JSONObject();

        output.put("imme", isImme());
        output.put("tmst", getTmst());
        output.put("time", getTime());
        output.put("freq", getFreq());
        output.put("rfch", getRfch());
        output.put("powe", getPowe());
        output.put("modu", getModu().name());

        if (getModu().equals(Modulation.LORA)) {
            output.put("codr", getCodr());
            output.put("ipol", isIpol());
        } else {
            output.put("fdev", getFdev());
        }

        output.put("datr", getDatr());
        output.put("prea", getPrea());
        output.put("size", getSize());
        output.put("ncrc", isNcrc());

        ByteBuffer bb = ByteBuffer.allocate(384);
        getData().toRaw(bb);
        output.put("data", Base64.getEncoder().encodeToString(Arrays.copyOfRange(bb.array(), 0, bb.capacity() - bb.remaining())));

        return output;
    }

    public boolean isImme() {
        return imme;
    }

    public int getTmst() {
        return tmst;
    }

    public String getTime() {
        return time;
    }

    public double getFreq() {
        return freq;
    }

    public int getRfch() {
        return rfch;
    }

    public int getPowe() {
        return powe;
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

    public int getFdev() {
        return fdev;
    }

    public boolean isIpol() {
        return ipol;
    }

    public int getPrea() {
        return prea;
    }

    public int getSize() {
        return size;
    }

    public PhyPayload getData() {
        return data;
    }

    public boolean isNcrc() {
        return ncrc;
    }

}
