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
public class PullResp extends SemtechPacket {

    private List<Txpk> txpks;

    public PullResp(byte[] _randoms, ByteBuffer _raw) throws MalformedPacketException {
        super(_randoms, PacketType.PULL_RESP);
        _raw.order(ByteOrder.LITTLE_ENDIAN);

        if (_raw.remaining() < 1) {
            throw new MalformedPacketException("too short");
        }

        byte[] json = new byte[_raw.remaining()];
        _raw.get(json);
        JSONObject jo;

        try {
            jo = new JSONObject(new String(json));
        } catch (JSONException ex) {
            throw new MalformedPacketException("malformed json");
        }

        txpks = new ArrayList<>();

        if (!jo.has("txpk")) {
            throw new MalformedPacketException("missing json (txpk)");
        }

        if (!jo.get("txpk").getClass().equals(JSONArray.class)) {
            throw new MalformedPacketException("malformed json (txpk)");
        }
        JSONArray rxpk = jo.getJSONArray("txpk");

        for (int i = 0; i < rxpk.length(); i++) {
            txpks.add(new Txpk(rxpk.getJSONObject(i)));
        }
    }

    private PullResp(byte[] _randoms) {
        super(_randoms, PacketType.PULL_RESP);
    }

    public List<Txpk> getTxpks() {
        return Collections.unmodifiableList(txpks);
    }

    public static class Builder {

        private final PullResp instance;

        public Builder(byte[] _randoms) {
            instance = new PullResp(_randoms);
        }

        public PullResp build() {
            return instance;
        }

        public Builder setRxpks(List<Txpk> _txpks) {
            instance.txpks = _txpks;
            return this;
        }

    }

    @Override
    public void toRaw(ByteBuffer _bb) throws MalformedPacketException {
        super.toRaw(_bb);

        JSONObject json = new JSONObject();
        if (!txpks.isEmpty()) {
            JSONArray txpk = new JSONArray();
            for (Txpk s : txpks) {
                txpk.put(s.toJson());
            }
            json.put("txpk", txpks);
        }

        _bb.put(json.toString().getBytes());
    }

}
