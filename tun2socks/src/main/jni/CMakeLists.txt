# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)


# Declares and names the project.

project("tun2socks")

set(LOCAL_PATH ${CMAKE_CURRENT_SOURCE_DIR})
set(ROOT_PATH ${LOCAL_PATH})
set(LOCAL_C_INCLUDES ${LOCAL_PATH}/tun2socks)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


add_definitions(
        #        -std=gnu99
        -DBADVPN_THREADWORK_USE_PTHREAD -DBADVPN_LINUX -DBADVPN_BREACTOR_BADVPN -D_GNU_SOURCE
        -DBADVPN_USE_SIGNALFD -DBADVPN_USE_EPOLL
        -DBADVPN_LITTLE_ENDIAN -DBADVPN_THREAD_SAFE
        -DNDEBUG -DANDROID
)

include_directories(
        ${LOCAL_PATH}
        ${LOCAL_PATH}/lwip/src/include
        ${LOCAL_PATH}/lwip/custom
        ${LOCAL_PATH}/libancillary
)

add_executable( # Sets the name of the library.
        tun2socks
        # Sets the library as a shared library.
        #SHARED

        # Provides a relative path to your source file(s).
        libancillary/fd_recv.c
        libancillary/fd_send.c

        # Provides a relative path to your source file(s).
        base/BLog_syslog.c
        system/BReactor_badvpn.c
        system/BSignal.c
        system/BConnection_common.c
        system/BConnection_unix.c
        system/BTime.c
        system/BUnixSignal.c
        system/BNetwork.c
        flow/StreamRecvInterface.c
        flow/PacketRecvInterface.c
        flow/PacketPassInterface.c
        flow/StreamPassInterface.c
        flow/SinglePacketBuffer.c
        flow/BufferWriter.c
        flow/PacketBuffer.c
        flow/PacketStreamSender.c
        flow/PacketPassConnector.c
        flow/PacketProtoFlow.c
        flow/PacketPassFairQueue.c
        flow/PacketProtoEncoder.c
        flow/PacketProtoDecoder.c
        socksclient/BSocksClient.c
        tuntap/BTap.c
        lwip/src/core/udp.c
        lwip/src/core/memp.c
        lwip/src/core/init.c
        lwip/src/core/pbuf.c
        lwip/src/core/tcp.c
        lwip/src/core/tcp_out.c
        lwip/src/core/netif.c
        lwip/src/core/def.c
        lwip/src/core/ip.c
        lwip/src/core/mem.c
        lwip/src/core/tcp_in.c
        lwip/src/core/stats.c
        lwip/src/core/inet_chksum.c
        lwip/src/core/timeouts.c
        lwip/src/core/ipv4/icmp.c
        lwip/src/core/ipv4/igmp.c
        lwip/src/core/ipv4/ip4_addr.c
        lwip/src/core/ipv4/ip4_frag.c
        lwip/src/core/ipv4/ip4.c
        lwip/src/core/ipv4/autoip.c
        lwip/src/core/ipv6/ethip6.c
        lwip/src/core/ipv6/inet6.c
        lwip/src/core/ipv6/ip6_addr.c
        lwip/src/core/ipv6/mld6.c
        lwip/src/core/ipv6/dhcp6.c
        lwip/src/core/ipv6/icmp6.c
        lwip/src/core/ipv6/ip6.c
        lwip/src/core/ipv6/ip6_frag.c
        lwip/src/core/ipv6/nd6.c
        lwip/custom/sys.c
        tun2socks/tun2socks.c
        base/DebugObject.c
        base/BLog.c
        base/BPending.c
        system/BDatagram_unix.c
        flowextra/PacketPassInactivityMonitor.c
        tun2socks/SocksUdpGwClient.c
        udpgw_client/UdpGwClient.c
        test/tun2socks.cpp
        )

set_target_properties(
        tun2socks
        PROPERTIES
        OUTPUT_NAME  #设置输出文件名称（可执行文件）
        "libtun2socks.so")

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library(dl-lib NAMES dl)

find_library(log-lib NAMES log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        tun2socks

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${dl-lib}
        )