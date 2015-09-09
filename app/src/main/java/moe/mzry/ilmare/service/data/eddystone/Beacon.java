// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package moe.mzry.ilmare.service.data.eddystone;

public class Beacon {
    final String deviceAddress;
    int rssi;
    long timestamp = System.currentTimeMillis();

    byte[] uidServiceData;
    byte[] tlmServiceData;
    byte[] urlServiceData;

    class UidStatus {
        String uidValue;
        int txPower;

        String errTx;
        String errUid;
        String errRfu;

        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            if (errTx != null) {
                sb.append(errTx).append("\n");
            }
            if (errUid != null) {
                sb.append(errUid).append("\n");
            }
            if (errRfu != null) {
                sb.append(errRfu).append("\n");
            }
            return sb.toString().trim();
        }

        @Override
        public String toString() {
            return super.toString() + "[uid = " + uidValue + ", txPower = " + txPower + "]";
        }
    }

    class TlmStatus {
        String version;
        String voltage;
        String temp;
        String advCnt;
        String secCnt;

        String errIdentialFrame;
        String errVersion;
        String errVoltage;
        String errTemp;
        String errPduCnt;
        String errSecCnt;
        String errRfu;

        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            if (errIdentialFrame != null) {
                sb.append(errIdentialFrame).append("\n");
            }
            if (errVersion != null) {
                sb.append(errVersion).append("\n");
            }
            if (errVoltage != null) {
                sb.append(errVoltage).append("\n");
            }
            if (errTemp != null) {
                sb.append(errTemp).append("\n");
            }
            if (errPduCnt != null) {
                sb.append(errPduCnt).append("\n");
            }
            if (errSecCnt != null) {
                sb.append(errSecCnt).append("\n");
            }
            if (errRfu != null) {
                sb.append(errRfu).append("\n");
            }
            return sb.toString().trim();
        }

        @Override
        public String toString() {
            return super.toString() + "[version = " + version + ", voltage = " + voltage
                    + ", temp = " + temp + ", advCnt = " + advCnt + ", secCnt = " + secCnt + "]";
        }
    }

    class UrlStatus {
        String urlValue;
        String urlNotSet;
        String urlNotInvariant;
        String txPower;

        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            if (txPower != null) {
                sb.append(txPower).append("\n");
            }
            if (urlNotSet != null) {
                sb.append(urlNotSet).append("\n");
            }
            if (urlNotInvariant != null) {
                sb.append(urlNotInvariant).append("\n");
            }
            return sb.toString().trim();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (urlValue != null) {
                sb.append(urlValue).append("\n");
            }
            return sb.append(getErrors()).toString().trim();
        }
    }

    class FrameStatus {
        String nullServiceData;
        String invalidFrameType;

        public String getErrors() {
            StringBuilder sb = new StringBuilder();
            if (nullServiceData != null) {
                sb.append(nullServiceData).append("\n");
            }
            if (invalidFrameType != null) {
                sb.append(invalidFrameType).append("\n");
            }
            return sb.toString().trim();
        }

        @Override
        public String toString() {
            return getErrors();
        }
    }

    boolean hasUidFrame;
    UidStatus uidStatus = new UidStatus();

    boolean hasTlmFrame;
    TlmStatus tlmStatus = new TlmStatus();

    boolean hasUrlFrame;
    UrlStatus urlStatus = new UrlStatus();

    FrameStatus frameStatus = new FrameStatus();

    public Beacon(String deviceAddress, int rssi) {
        this.deviceAddress = deviceAddress;
        this.rssi = rssi;
    }

    public Beacon updateRssi(int rssi) {
        this.rssi = rssi;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    @Override
    public String toString() {
        return super.toString() + "[device = " + deviceAddress + ", rssi = " + rssi
                + ", distance = " + distanceFromRssi() + ", hasUidFrame = " + hasUidFrame
                + ", uidFrame = " + uidStatus + ", hasTlmFrame = " + hasTlmFrame
                + ", tlmFrame = " + tlmStatus + "]";
    }

    public double distanceFromRssi() {
        int pathLoss = uidStatus.txPower - rssi;
        return Math.pow(10, (pathLoss - 41) / 20.0);
    }

    /**
     * Performs a case-insensitive contains test of s on the device address (with or without the
     * colon separators) and/or the UID value, and/or the URL value.
     */
    boolean contains(String s) {
        return s == null
                || s.isEmpty()
                || deviceAddress.replace(":", "").toLowerCase().contains(s.toLowerCase())
                || (uidStatus.uidValue != null
                && uidStatus.uidValue.toLowerCase().contains(s.toLowerCase()))
                || (urlStatus.urlValue != null
                && urlStatus.urlValue.toLowerCase().contains(s.toLowerCase()));
    }
}
