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

/**
 *
 * @author cambierr
 */
public enum MType {

    JOIN_REQUEST((byte) 0x00, JoinRequestPayload.class, Direction.UP),
    JOIN_ACCEPT((byte) 0x01, JoinAcceptPayload.class, Direction.DOWN),
    UNCONF_DATA_UP((byte) 0x02, DataPayload.class, Direction.UP),
    UNCONF_DATA_DOWN((byte) 0x03, DataPayload.class, Direction.DOWN),
    CONF_DATA_UP((byte) 0x04, DataPayload.class, Direction.UP),
    CONF_DATA_DOWN((byte) 0x05, DataPayload.class, Direction.DOWN),
    RFU((byte) 0x06, RFUPayload.class, null),
    PROPRIETARY((byte) 0x07, ProprietaryPayload.class, null);

    private MType(byte _value, Class<? extends FRMPayload> _mapper, Direction _direction) {
        value = _value;
        mapper = _mapper;
        direction = _direction;
    }

    private final byte value;
    private final Class<? extends FRMPayload> mapper;
    private final Direction direction;

    public static MType from(byte _mhdr) throws MalformedPacketException {
        byte mType = (byte) ((_mhdr >> 5) & 0x07);
        for (MType v : values()) {
            if (v.value == mType) {
                return v;
            }
        }
        throw new MalformedPacketException("MType");
    }
    
    public Direction getDirection(){
        return direction;
    }

    public Class<? extends FRMPayload> getMapper() {
        if (mapper.equals(RFUPayload.class)) {
            if (rfuMapper == null) {
                throw new RuntimeException("Missing mapper for MType " + name());
            } else {
                return rfuMapper;
            }
        }
        if (mapper.equals(ProprietaryPayload.class)) {
            if (proprietaryMapper == null) {
                throw new RuntimeException("Missing mapper for MType " + name());
            } else {
                return proprietaryMapper;
            }
        }
        return mapper;
    }

    private static Class<? extends RFUPayload> rfuMapper;
    private static Class<? extends ProprietaryPayload> proprietaryMapper;

    public void setRfuPayloadMapper(Class<? extends RFUPayload> _handler) {
        rfuMapper = _handler;
    }

    public void setProprietaryPayloadMapper(Class<? extends ProprietaryPayload> _handler) {
        proprietaryMapper = _handler;
    }
}
