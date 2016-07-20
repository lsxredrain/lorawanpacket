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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cambierr
 */
public class TxAck extends SemtechPacket {

    private Error error;

    public TxAck(byte[] _randoms, ByteBuffer _raw) throws MalformedPacketException {
        super(_randoms, PacketType.TX_ACK);

        byte[] txt = new byte[_raw.remaining()];
        _raw.get(txt);

        JSONObject jo;

        try {
            jo = new JSONObject(new String(txt));
        } catch (JSONException ex) {
            throw new MalformedPacketException("malformed json");
        }

        if (!jo.has("txpk_ack") || !jo.get("txpk_ack").getClass().equals(JSONObject.class)) {
            throw new MalformedPacketException("malformed json");
        }

        if (!jo.getJSONObject("txpk_ack").has("error")) {
            throw new MalformedPacketException("malformed json");
        }

        error = Error.parse(jo.getJSONObject("txpk_ack").getString("error"));
    }

    private TxAck(byte[] _randoms) {
        super(_randoms, PacketType.TX_ACK);
    }

    public Error getError() {
        return error;
    }

    public static enum Error {

        NONE,
        TOO_LATE,
        TOO_EARLY,
        COLLISION_PACKET,
        COLLISION_BEACON,
        TX_FREQ,
        TX_POWER,
        GPS_UNLOCKED;

        public static Error parse(String _str) throws MalformedPacketException {
            try {
                return valueOf(_str);
            } catch (IllegalArgumentException ex) {
                throw new MalformedPacketException("Error");
            }
        }

    }

    public static class Builder {

        private final TxAck instance;

        public Builder(byte[] _randoms) {
            instance = new TxAck(_randoms);
        }

        public Builder setErro(Error _error) {
            instance.error = _error;
            return this;
        }

        public TxAck build() {
            return instance;
        }

    }

    @Override
    public void toRaw(ByteBuffer _raw) throws MalformedPacketException {
        super.toRaw(_raw);
        _raw.put(new JSONObject().put("txpk_ack", new JSONObject().put("error", error.name())).toString().getBytes());
    }

}
