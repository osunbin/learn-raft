package com.bin.raft.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InetUtils {

    private static final int IPV4_MAX_OCTET_VALUE = 255;

    public static int ip2Int(String ipString) {
        // 取 ip 的各段
        String[] ipSlices = ipString.split("\\.");
        int rs = 0;
        for (int i = 0; i < ipSlices.length; i++) {
            // 将 ip 的每一段解析为 int，并根据位置左移 8 位
            int intSlice = Integer.parseInt(ipSlices[i]) << 8 * i;
            // 或运算
            rs = rs | intSlice;
        }
        return rs;
    }

    public static String int2Ip(int ipInt) {
        String[] ipString = new String[4];
        for (int i = 0; i < 4; i++) {
            // 每 8 位为一段，这里取当前要处理的最高位的位置
            int pos = i * 8;
            // 取当前处理的 ip 段的值
            int and = ipInt & (255 << pos);
            // 将当前 ip 段转换为 0 ~ 255 的数字，注意这里必须使用无符号右移
            ipString[i] = String.valueOf(and >>> pos);
        }
        return String.join(".", ipString);
    }



    public static String getIP() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            InetAddress internalIP = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                Enumeration addresses = netInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        byte[] ipByte = ip.getAddress();

                        if (ipByte.length == 4) {
                            if (ipCheck(ipByte)) {
                                if (ip.isSiteLocalAddress()) {
                                    // 如果是site-local地址，就是它了 就是我们要找的
                                    // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                                    return ip.getHostAddress();
                                }

                                if (internalIP == null) {
                                    internalIP = ip;
                                }
                            }
                        }
                    }
                }
            }
            if (internalIP != null) {
                return internalIP.getHostAddress();
            } else {
                throw new RuntimeException("Can not get local ip");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can not get local ip", e);
        }
    }



    private static boolean ipCheck(byte[] ip) {
        if (ip.length != 4) {
            throw new RuntimeException("illegal ipv4 bytes");
        }

        return isValidInet4Address(ipToIPv4Str(ip));
    }


    private static String ipToIPv4Str(byte[] ip) {
        if (ip.length != 4) {
            return null;
        }
        return new StringBuilder().append(ip[0] & 0xFF).append(".").append(
                        ip[1] & 0xFF).append(".").append(ip[2] & 0xFF)
                .append(".").append(ip[3] & 0xFF).toString();
    }


    private static boolean isValidInet4Address(final String inet4Address) {
        // verify that address conforms to generic IPv4 format
        final String[] groups = match(inet4Address);

        if (groups == null) {
            return false;
        }

        // verify that address subgroups are legal
        for (final String ipSegment : groups) {
            if (ipSegment == null || ipSegment.isEmpty()) {
                return false;
            }

            int iIpSegment = 0;

            try {
                iIpSegment = Integer.parseInt(ipSegment);
            } catch (final NumberFormatException e) {
                return false;
            }

            if (iIpSegment > IPV4_MAX_OCTET_VALUE) {
                return false;
            }

            if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
                return false;
            }

        }

        return true;
    }


    private static final String IPV4_REGEX =
            "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";

    private static Pattern pattern = Pattern.compile(IPV4_REGEX);

    private static String[] match(final String value) {
        if (value == null) {
            return null;
        }

        final Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            final int count = matcher.groupCount();
            final String[] groups = new String[count];
            for (int j = 0; j < count; j++) {
                groups[j] = matcher.group(j + 1);
            }
            return groups;
        }
        return null;
    }

}
