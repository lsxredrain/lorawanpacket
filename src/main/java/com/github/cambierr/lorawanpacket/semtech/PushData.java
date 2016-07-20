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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cambierr
 */
public class PushData extends SemtechPacket {

    private byte[] gatewayEui;
    private List<Stat> stats;
    private List<Rxpk> rxpks;

    public PushData(byte[] _randoms, ByteBuffer _raw) throws MalformedPacketException {
        super(_randoms, PacketType.PUSH_DATA);
        _raw.order(ByteOrder.LITTLE_ENDIAN);

        if (_raw.remaining() < 8) {
            throw new MalformedPacketException("too short");
        }

        gatewayEui = new byte[8];
        _raw.get(gatewayEui);

        byte[] json = new byte[_raw.remaining()];
        _raw.get(json);
        JSONObject jo;

        try {
            jo = new JSONObject(new String(json));
        } catch (JSONException ex) {
            throw new MalformedPacketException("malformed json");
        }

        stats = new ArrayList<>();
        rxpks = new ArrayList<>();

        if (jo.has("rxpk")) {
            if (!jo.get("rxpk").getClass().equals(JSONArray.class)) {
                throw new MalformedPacketException("malformed json (rxpk)");
            }
            JSONArray rxpk = jo.getJSONArray("rxpk");

            for (int i = 0; i < rxpk.length(); i++) {
                rxpks.add(new Rxpk(rxpk.getJSONObject(i)));
            }
        }

        if (jo.has("stat")) {
            if (!jo.get("stat").getClass().equals(JSONArray.class)) {
                throw new MalformedPacketException("malformed json (stat)");
            }
            JSONArray stat = jo.getJSONArray("stat");

            for (int i = 0; i < stat.length(); i++) {
                stats.add(new Stat(stat.getJSONObject(i)));
            }
        }

    }

    private PushData(byte[] _randoms) {
        super(_randoms, PacketType.PUSH_DATA);
    }

    @Override
    public void toRaw(ByteBuffer _bb) throws MalformedPacketException {
        super.toRaw(_bb);
        _bb.put(gatewayEui);

        JSONObject json = new JSONObject();
        if (!stats.isEmpty()) {
            JSONArray stat = new JSONArray();
            for (Stat s : stats) {
                stat.put(s.toJson());
            }
            json.put("stat", stats);
        }
        if (!rxpks.isEmpty()) {
            JSONArray rxpk = new JSONArray();
            for (Rxpk s : rxpks) {
                rxpk.put(s.toJson());
            }
            json.put("rxpk", rxpks);
        }

        _bb.put(json.toString().getBytes());
    }

    public byte[] getGatewayEui() {
        return gatewayEui;
    }

    public List<Stat> getStats() {
        return Collections.unmodifiableList(stats);
    }

    public List<Rxpk> getRxpks() {
        return Collections.unmodifiableList(rxpks);
    }

    public static class Builder {

        private final PushData instance;

        public Builder(byte[] _randoms) {
            instance = new PushData(_randoms);
        }

        public PushData build() {
            return instance;
        }

        public Builder setGatewayEui(byte[] _gatewayEui) {
            instance.gatewayEui = _gatewayEui;
            return this;
        }

        public Builder setStats(List<Stat> _stats) {
            instance.stats = _stats;
            return this;
        }

        public Builder setRxpks(List<Rxpk> _rxpks) {
            instance.rxpks = _rxpks;
            return this;
        }

    }

}
