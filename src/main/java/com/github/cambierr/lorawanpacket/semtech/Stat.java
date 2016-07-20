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
import org.json.JSONObject;

/**
 *
 * @author cambierr
 */
public class Stat {

    private String time;
    private double lati;
    private double longi;
    private int alti;
    private int rxnb;
    private int rxok;
    private int rxfw;
    private int ackr;
    private int dwnb;
    private int txnb;

    private Stat() {
        time = null;
        lati = Double.MIN_VALUE;
        longi = Double.MIN_VALUE;
        alti = Integer.MIN_VALUE;
        rxnb = Integer.MIN_VALUE;
        rxok = Integer.MIN_VALUE;
        rxfw = Integer.MIN_VALUE;
        ackr = Integer.MIN_VALUE;
        dwnb = Integer.MIN_VALUE;
        txnb = Integer.MIN_VALUE;
    }

    public Stat(JSONObject _json) throws MalformedPacketException {

        /**
         * time
         */
        if (!_json.has("time")) {
            throw new MalformedPacketException("missing time");
        } else {
            time = _json.getString("time");
        }

        /**
         * lati
         */
        if (!_json.has("lati")) {
            lati = Double.MIN_VALUE;
        } else {
            lati = _json.getDouble("lati");
        }

        /**
         * longi
         */
        if (!_json.has("longi")) {
            lati = Double.MIN_VALUE;
        } else {
            longi = _json.getDouble("longi");
        }

        /**
         * alti
         */
        if (!_json.has("alti")) {
            alti = Integer.MIN_VALUE;
        } else {
            alti = _json.getInt("alti");
        }

        /**
         * rxnb
         */
        if (!_json.has("rxnb")) {
            throw new MalformedPacketException("missing rxnb");
        } else {
            rxnb = _json.getInt("rxnb");
        }

        /**
         * rxok
         */
        if (!_json.has("rxok")) {
            throw new MalformedPacketException("missing rxok");
        } else {
            rxok = _json.getInt("rxok");
        }

        /**
         * rxfw
         */
        if (!_json.has("rxfw")) {
            throw new MalformedPacketException("missing rxfw");
        } else {
            rxfw = _json.getInt("rxfw");
        }

        /**
         * ackr
         */
        if (!_json.has("ackr")) {
            throw new MalformedPacketException("missing ackr");
        } else {
            ackr = _json.getInt("ackr");
        }

        /**
         * dwnb
         */
        if (!_json.has("dwnb")) {
            throw new MalformedPacketException("missing dwnb");
        } else {
            dwnb = _json.getInt("dwnb");
        }

        /**
         * txnb
         */
        if (!_json.has("txnb")) {
            throw new MalformedPacketException("missing txnb");
        } else {
            txnb = _json.getInt("txnb");
        }

    }

    public String getTime() {
        return time;
    }

    public double getLati() {
        return lati;
    }

    public double getLongi() {
        return longi;
    }

    public int getAlti() {
        return alti;
    }

    public int getRxnb() {
        return rxnb;
    }

    public int getRxok() {
        return rxok;
    }

    public int getRxfw() {
        return rxfw;
    }

    public int getAckr() {
        return ackr;
    }

    public int getDwnb() {
        return dwnb;
    }

    public int getTxnb() {
        return txnb;
    }

    public static class Builder {

        private final Stat instance;

        public Builder() {
            instance = new Stat();
        }

        public Builder set() {

            return this;
        }

        public Builder setTime(String _time) {
            instance.time = _time;
            return this;
        }

        public Builder setLati(double _lati) {
            if (_lati > 90 || _lati < -90) {
                throw new IllegalArgumentException("lati must be between -90 an 90");
            }
            instance.lati = _lati;
            return this;
        }

        public Builder setLong(double _long) {
            if (_long > 180 || _long < -180) {
                throw new IllegalArgumentException("long must be between -180 an 180");
            }
            instance.longi = _long;
            return this;
        }

        public Builder setAlti(int _alti) {
            instance.alti = _alti;
            return this;
        }

        public Builder setRxnb(int _rxnb) {
            if (_rxnb < 0) {
                throw new IllegalArgumentException("rxnb must be positive");
            }
            instance.rxnb = _rxnb;
            return this;
        }

        public Builder setRxok(int _rxok) {
            if (_rxok < 0) {
                throw new IllegalArgumentException("rxok must be positive");
            }
            instance.rxok = _rxok;
            return this;
        }

        public Builder setRxfw(int _rxfw) {
            if (_rxfw < 0) {
                throw new IllegalArgumentException("rxfw must be positive");
            }
            instance.rxfw = _rxfw;
            return this;
        }

        public Builder setAckr(int _ackr) {
            if (_ackr < 0) {
                throw new IllegalArgumentException("ackr must be positive");
            }
            instance.ackr = _ackr;
            return this;
        }

        public Builder setDwnb(int _dwnb) {
            if (_dwnb < 0) {
                throw new IllegalArgumentException("dwnb must be positive");
            }
            instance.dwnb = _dwnb;
            return this;
        }

        public Builder setTxnb(int _txnb) {
            if (_txnb < 0) {
                throw new IllegalArgumentException("txnb must be positive");
            }
            instance.txnb = _txnb;
            return this;
        }

        public Stat build() {
            return instance;
        }

    }
    
    public JSONObject toJson(){
        
        JSONObject output = new JSONObject();
        
        output.put("time", time);
        output.put("lati", lati);
        output.put("long", longi);
        output.put("alti", alti);
        output.put("rxnb", rxnb);
        output.put("rxok", rxok);
        output.put("rxfw", rxfw);
        output.put("ackr", ackr);
        output.put("dwnb", dwnb);
        output.put("txnb", txnb);
        
        return output;
    }
}
